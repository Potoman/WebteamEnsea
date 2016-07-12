package com.potoman.webteam.task;

import org.json.JSONException;
import org.json.JSONObject;

import com.potoman.tools.CallService;
import com.potoman.tools.L;
import com.potoman.tools.UrlService;
import com.potoman.webteam.exception.ExceptionService;
import com.potoman.webteam.task.ATConnexionWebteam.RetourConnexion;

import android.content.Context;
import android.os.AsyncTask;

public class ATConnexionWebteam extends AsyncTask<Context, Void, RetourConnexion> {

	public class RetourConnexion {
		public String version = "";
		public String pseudo = "";
		public String password = "";
		public int id = -1;
		
		public RetourConnexion(String version, String pseudo, String password, int id) {
			this.version = version;
			this.pseudo = pseudo;
			this.password = password;
			this.id = id;
		}
	}
	
	private IWorkFinishOfAsyncTask iWorkFinishOfAsyncTask = null;
	
	private String pseudo = "";
	private String password = "";
	
	public ATConnexionWebteam(IWorkFinishOfAsyncTask iWorkFinishOfAsyncTask, String pseudo, String password) {
		this.iWorkFinishOfAsyncTask = iWorkFinishOfAsyncTask;
		this.pseudo = pseudo;
		this.password = password;
	}
	
	/**
	 * 
	 * String : null : erreur de connexion
	 * "" : connexion ok, pas de Mise à jour de l'appli.
	 * !"" : connexion ok, mise à jour de l'appli.
	 * 
	 */
	@Override
	protected RetourConnexion doInBackground(Context... params) {
		JSONObject reponseJSON = null;
		
		try {
			L.v("GetBoiteDEnvois", "doInBackground : on va chercher les message sur le serveur de la webteam...");
			reponseJSON = CallService.getJsonGeth(params[0], UrlService.createUrlConnexion(
					pseudo, password
					));
		} catch (final ExceptionService e) {
			stateConnection = STATE_NOT_INTERNET;
			e.printStackTrace();
			return null;
		}
		try {
			if (reponseJSON != null) {
		    	int erreur = 0;
		    	try {
					erreur = (Integer) reponseJSON.getInt("erreur");
				} catch (JSONException e) {
					L.v("Root", "Exception JSON");
					e.printStackTrace();
				}
		    	switch (erreur) {
		    	case 15:
		    		return new RetourConnexion(reponseJSON.getString("version"), reponseJSON.getString("pseudo"), password, reponseJSON.getInt("id"));
		    	case 1:
		    		return new RetourConnexion("", reponseJSON.getString("pseudo"), password, reponseJSON.getInt("id"));
	    		default:
					stateConnection = STATE_WRONG_LOGGIN;
	    			return null;
		    	}
	    	}
			else {
				stateConnection = STATE_WRONG_LOGGIN;
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			stateConnection = STATE_ERROR_IN_PARSING_JSON;
			return null;
		}
	}

	
	@Override
	protected void onPostExecute(RetourConnexion result) {
		super.onPostExecute(result);
		if (isCancelled()) {
			return;
		}
		iWorkFinishOfAsyncTask.workFinish(stateConnection);
	}
	
	private int stateConnection = STATE_OK;
	
	public static final int STATE_OK = 0;
	public static final int STATE_NOT_INTERNET = 1;
	public static final int STATE_WRONG_LOGGIN = 2;
	public static final int STATE_ERROR_IN_PARSING_JSON = 3;
}
