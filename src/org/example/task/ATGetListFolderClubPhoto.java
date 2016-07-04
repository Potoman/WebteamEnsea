package org.example.task;

import java.util.Set;

import org.apache.http.client.HttpClient;

import potoman.tools.IObserver;
import potoman.webteam.bdd.data.FolderClubPhoto;

import android.content.Context;
import android.os.AsyncTask;

public class ATGetListFolderClubPhoto extends AsyncTask<Context, Void, Set<FolderClubPhoto>> {

	private static final String URL_ROOT = "http://photo.asso-ensea.net/";
	private static final String URL_LOG = "http://photo.asso-ensea.net/identification.php";
	
	private IObserver observer = null;
	private String pseudoClubPhoto = "";
	private String passwordClubPhoto = "";
	private HttpClient httpClient = null;
		
	public ATGetListFolderClubPhoto(IObserver observer, String pseudoClubPhoto, String passwordClubPhoto, HttpClient httpClient) {
		this.observer = observer;
		this.pseudoClubPhoto = pseudoClubPhoto;
		this.passwordClubPhoto = passwordClubPhoto;
		this.httpClient = httpClient;
	}
	
	@Override
	protected Set<FolderClubPhoto> doInBackground(Context... params) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	protected void onPostExecute(Set<FolderClubPhoto> result) {
		observer.update(this, null);
		super.onPostExecute(result);
	}
}
