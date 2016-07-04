package org.example.task.clubphoto;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import potoman.tools.CallService;
import potoman.tools.IObserver;
import potoman.webteam.bdd.data.FolderClubPhoto;
import potoman.webteam.bdd.data.UrlImgClubPhoto;
import android.content.Context;
import android.os.AsyncTask;

public class ATGetCategoryClubPhoto extends AsyncTask<Context, Void, Boolean> {
	
	private IObserver observer = null;
	private HttpClient httpClient = null;
	private FolderClubPhoto folder = null;
	private String pseudoClubPhoto = "";
	private String passwordClubPhoto = "";
	
	private final List<UrlImgClubPhoto> listUrlImg = new ArrayList<UrlImgClubPhoto>();
	
	public ATGetCategoryClubPhoto(IObserver observer, HttpClient httpClient, FolderClubPhoto folder, String pseudoClubPhoto, String passwordClubPhoto) {
		this.observer = observer;
		this.httpClient = httpClient;
		this.folder = folder;
		this.pseudoClubPhoto = pseudoClubPhoto;
		this.passwordClubPhoto = passwordClubPhoto;
	}
	
	@Override
	protected Boolean doInBackground(Context... params) {
		CallService.askForResponse(AUrlClubPhoto.URL_ROOT + AUrlClubPhoto.URL_CONNEXION, httpClient, true, 
				"method", 
				"pwg.session.login", 
				"password", 
				passwordClubPhoto, 
				"username", 
				pseudoClubPhoto);
		for (int page = 0; page < Math.ceil((double) (int) folder.getCountPhotoInFolder() / (double) 500); page++) {
			JSONObject json = CallService.askForJSON(AUrlClubPhoto.URL_ROOT + AUrlClubPhoto.URL_FOLDER.replace("<ID>", "" + folder.getCategory()).replace("<PAGE>", "" + page), httpClient, true);
			if (isCancelled()) {
				return false;
			}
			if (json == null) {
				return false;
			}
			try {	
				JSONObject result = json.getJSONObject("result");
				JSONObject images = result.getJSONObject("images");
				JSONArray content = images.getJSONArray("_content");
				for (int i = 0; i < content.length(); i++) {
					JSONObject jsonImg = content.getJSONObject(i);
					
					int idImg = jsonImg.getInt("id");
					
					JSONObject jsonDerivates = jsonImg.getJSONObject("derivatives");
					JSONObject jsonSquare = jsonDerivates.getJSONObject("square");
					String url = jsonSquare.getString("url");
					listUrlImg.add(new UrlImgClubPhoto(idImg, folder.getCategory(), url));
					// Il faut remplacer -sq par -me pour avoir une vrai image.
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		
		return true;
	}
	
	public FolderClubPhoto getCategory() {
		return folder;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (isCancelled()) {
			return;
		}
		observer.update(this, listUrlImg);
	}
}
