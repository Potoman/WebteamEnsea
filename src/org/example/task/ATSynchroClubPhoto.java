package org.example.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.example.webteam.boitemanager.Squirrel;

import potoman.tools.CallService;
import potoman.tools.IObserver;
import potoman.tools.L;
import potoman.webteam.bdd.data.FolderClubPhoto;
import potoman.webteam.constant.Webteam;
import android.content.Context;
import android.os.AsyncTask;

public class ATSynchroClubPhoto extends AsyncTask<Context, Void, Integer> {

	private static final String URL_ROOT = "http://photo.asso-ensea.net/";
	private static final String URL_LOG = "http://photo.asso-ensea.net/identification.php";
	private static final String URL_LIST_CATEGORY = "http://photo.asso-ensea.net/index.php?/categories";
	private static final String URL_CATEGORY = "http://photo.asso-ensea.net/index.php?/category/";
	
	private IObserver observer = null;
	private String pseudoClubPhoto = "";
	private String passwordClubPhoto = "";
	private HttpClient httpClient = null;
	
	Map<Integer, String> mapNameFolder = new HashMap<Integer, String>();
	Map<Integer, Set<Integer>> mapCategory = new HashMap<Integer, Set<Integer>>();
	
	public ATSynchroClubPhoto(IObserver observer, String pseudoClubPhoto, String passwordClubPhoto, HttpClient httpClient) {
		this.observer = observer;
		this.pseudoClubPhoto = pseudoClubPhoto;
		this.passwordClubPhoto = passwordClubPhoto;
		this.httpClient = httpClient;
	}
	
	@Override
	protected Integer doInBackground(Context... params) {

		HttpResponse response = CallService.askForSomething(URL_ROOT, httpClient, true);
		
		if (!pseudoClubPhoto.equals("") && !passwordClubPhoto.equals("")) {
			List<String> listName = Arrays.asList("login", 
					"password", 
					"username");
			List<String> listValue = Arrays.asList("Valider",
					passwordClubPhoto, 
					pseudoClubPhoto);
			CallService.askForSomethingWithPost(URL_LOG, httpClient, listName, listValue, true);
		}
		
		boolean parsingOk = getBodyFolder(mapCategory, null);
		
		L.i("ATSynchroClubPhoto", "count category = " + mapCategory.size());
//		for (Entry<Integer, Set<Integer>> entry : mapCategory.entrySet()) {
//			L.v("ATSynchroClubPhoto", "Folder = " + entry.getKey());
//			for (Integer categoryInFolder : entry.getValue()) {
//				L.v("ATSynchroClubPhoto", "in Folder = " + categoryInFolder);
//			}
//		}
		
		return null;
	}

	@Override
	protected void onPostExecute(Integer result) {
		
		super.onPostExecute(result);
	}
	
	private Boolean getBodyFolder(final Map<Integer, Set<Integer>> mapCategory, final Integer category) {
		L.e("ATSynchroClubPhoto", "getBodyFolder - category = " + category);
		Set<Integer> setCageroryTemp = listCategoryInFolder(category);
		if (setCageroryTemp == null) {
			return false;
		}
		setCageroryTemp.removeAll(mapCategory.keySet());
		if (category != null) {
			mapCategory.put(category, setCageroryTemp);
		}
		
//		for (Integer categoryFound : setCageroryTemp) {
//			L.e("ATSynchroClubPhoto", "Category into = " + categoryFound);
//		}
		
		for (Integer categoryFound : setCageroryTemp) {
			if (!mapCategory.containsKey(categoryFound)) {
				getBodyFolder(mapCategory, categoryFound);
			}
		}
		
		return true;
	}
	
	private Set<Integer> listCategoryInFolder(Integer category) {
		HttpResponse response = null;
		if (category == null) {
			response = CallService.askForSomething(URL_LIST_CATEGORY, httpClient, true);
		}
		else {
			response = CallService.askForSomething(URL_CATEGORY + category, httpClient, true);
		}
		
		Set<Integer> setCategory = new HashSet<Integer>();

		//<a href="index.php?/category/750"  title="1 photo dans cet album / 18 photos dans 2 sous-albums">Concours Ensearque de Photographie 2012</a>
		Pattern patternForEmailSingle = Pattern.compile("<a href=\"index\\.php\\?/category/([0-9]+)\">(.*)</a>");
		//<a href="index.php?/category/79">Festivals</a>

		CallService.setParam(response, true);
		String normal = "";
		try {
			StringBuffer stringBuffer = null;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "utf-8"), 8);
			String ligne = "";
			//bufferedReader.read();
			while ((ligne = bufferedReader.readLine()) != null) {
				//L.v("ATSynchroClubPhoto", "ligne > " + ligne);
				Matcher m = patternForEmailSingle.matcher(ligne);
				while (m.find()) {
					String categoryFound = m.group(1);
					String nameFolder = m.group(2);
					mapNameFolder.put(Integer.parseInt(categoryFound), nameFolder);
					setCategory.add(Integer.parseInt(categoryFound));
					//L.i("ATSynchroClubPhoto", "category parsed = " + categoryFound); // + ", name = " + nameFolder);
				}
			}
			bufferedReader.close();
		} catch (Exception e) {
			L.e("ATSynchroClubPhoto", "listCategoryInFolder - ERREUR DANS LA LECTURE DE LA REPONSE !!!");
			e.printStackTrace();
			return null;
		}
		return setCategory;
	}

}
