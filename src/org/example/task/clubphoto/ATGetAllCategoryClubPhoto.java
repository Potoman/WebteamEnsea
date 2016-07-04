package org.example.task.clubphoto;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import potoman.tools.CallService;
import potoman.tools.IObserver;
import potoman.tools.L;
import potoman.webteam.bdd.data.FolderClubPhoto;
import android.content.Context;
import android.os.AsyncTask;

public class ATGetAllCategoryClubPhoto extends AsyncTask<Context, Void, Boolean> {
	
	private IObserver observer = null;
	private HttpClient httpClientLogged = null;
	private Integer category = null;
	
	private final Map<Integer, FolderClubPhoto> mapFolderClubPhoto = new HashMap<Integer, FolderClubPhoto>();
	
	public ATGetAllCategoryClubPhoto(IObserver observer, HttpClient httpClientLogged) {
		this.observer = observer;
		this.httpClientLogged = httpClientLogged;
	}
	
	public Integer getCategory() {
		return category;
	}
	
	@Override
	protected Boolean doInBackground(Context... params) {
		
		JSONObject json = CallService.askForJSON(AUrlClubPhoto.URL_ROOT + AUrlClubPhoto.URL_LIST_FOLDER, httpClientLogged, true);
		
		if (json == null) {
			return false;
		}
		try {	
			JSONObject result = json.getJSONObject("result");
			JSONArray categories = result.getJSONArray("categories");
			for (int i = 0; i < categories.length(); i++) {
				JSONObject categorie = categories.getJSONObject(i);
				int id = categorie.getInt("id");
				String upperCategories = categorie.getString("id_uppercat");
				Integer upperCat = upperCategories == null ? null : upperCategories.equals("null") ? null : Integer.parseInt(upperCategories);
				String title = categorie.getString("name");
				int countSubFolder = categorie.getInt("nb_categories");
				int countImg = categorie.getInt("nb_images");
				int countSubPhoto = categorie.getInt("total_nb_images");
				int depth = categorie.getString("global_rank").split("\\.").length;
				FolderClubPhoto fcp = new FolderClubPhoto(id, upperCat, title, countImg, countSubFolder, countSubPhoto, depth);
				mapFolderClubPhoto.put(id, fcp);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (isCancelled()) {
			return;
		}
		observer.update(this, mapFolderClubPhoto);
	}
}
