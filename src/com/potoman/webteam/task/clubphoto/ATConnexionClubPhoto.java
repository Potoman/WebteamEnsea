package com.potoman.webteam.task.clubphoto;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Observer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.potoman.tools.CallService;
import com.potoman.tools.IObserver;
import com.potoman.tools.L;
import com.potoman.webteam.bdd.data.FolderClubPhoto;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Xml;

public class ATConnexionClubPhoto extends AsyncTask<Context, Void, Boolean>{
	
	private static final String WRONG_LOGGIN = "Mot de passe invalide !";
	
	private IObserver observer = null;
	private String pseudoClubPhoto = "";
	private String passwordClubPhoto = "";
	private HttpClient httpClient = null;
	
	public ATConnexionClubPhoto(IObserver observer, String pseudoClubPhoto, String passwordClubPhoto, HttpClient httpClient) {
		this.observer = observer;
		this.pseudoClubPhoto = pseudoClubPhoto;
		this.passwordClubPhoto = passwordClubPhoto;
		this.httpClient = httpClient;
	}
	
	@Override
	protected Boolean doInBackground(Context... params) {
		if (!pseudoClubPhoto.equals("") && !passwordClubPhoto.equals("")) {
			JSONObject json = CallService.askForJSON(AUrlClubPhoto.URL_ROOT + AUrlClubPhoto.URL_CONNEXION, httpClient, true, 
					"method", 
					"pwg.session.login", 
					"password", 
					passwordClubPhoto, 
					"username", 
					pseudoClubPhoto);
			if (json == null) {
				return false;
			}
			String stat = null;
			try {
				stat = json.getString("stat");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (stat != null && stat.equals("ok")) {
				return true;
			}
			return false;
		}
		return false;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		observer.update(this, null);
		super.onPostExecute(result);
	}

}
