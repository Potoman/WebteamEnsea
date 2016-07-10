package org.example.task;

import java.util.ArrayList;
import java.util.List;

import org.example.webteam.boitemanager.message.Message;
import org.example.webteam.boitemanager.message.messageprive.MessagePrive;
import org.example.webteam.loggin.Root;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import potoman.tools.CallService;
import potoman.tools.IObserver;
import potoman.tools.L;
import potoman.tools.UrlService;
import potoman.webteam.constant.Webteam;
import potoman.webteam.exception.ExceptionService;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class ATGetBoiteDeMessagePrive extends AsyncTask<Context, Void, List<Message>>{

	private IObserver observer = null;
	private String nomBoite = "";
	
	public ATGetBoiteDeMessagePrive(IObserver observer, String nomBoite) {
		this.observer = observer;
		this.nomBoite = nomBoite;
	}
	
	@Override
	protected List<Message> doInBackground(Context... params) {
		L.w("GetBoiteDeReception", "EXECUTE !");
		List<Message> myListMessage = new ArrayList<Message>();
		
		JSONObject reponseJSON = null;
		try {
			if (nomBoite.equals(Webteam.BOITE_DE_RECEPTION)) {
				reponseJSON = CallService.getJsonGeth(params[0], UrlService.createUrlBoiteDeReception(
						PreferenceManager.getDefaultSharedPreferences(params[0]).getString(Webteam.PSEUDO, ""),
						PreferenceManager.getDefaultSharedPreferences(params[0]).getString(Webteam.PASSWORD, "")
						));
			}
			else {
				reponseJSON = CallService.getJsonGeth(params[0], UrlService.createUrlBoiteDEnvoie(
						PreferenceManager.getDefaultSharedPreferences(params[0]).getString(Webteam.PSEUDO, ""),
						PreferenceManager.getDefaultSharedPreferences(params[0]).getString(Webteam.PASSWORD, "")
						));
			}
		} catch (final ExceptionService e) {
			reponseJSON = null;
			e.printStackTrace();
		}
		if (reponseJSON != null) {
			try {
				JSONArray contenuJSON = reponseJSON.getJSONArray("contenu");
				int index = 0;
				
				int id = 0;
				String titre = "";
				int idTo = 0;
				String pseudoTo = "";
				int idFrom = 0;
				String pseudoFrom = "";
				String time = "";
				int etatLu = 0;
				while (!contenuJSON.isNull(index)) {
					id = contenuJSON.getJSONObject(index).getInt("id");
					titre = contenuJSON.getJSONObject(index).getString("titre");
					if (nomBoite.equals(Webteam.BOITE_DE_RECEPTION)) {
						idFrom = contenuJSON.getJSONObject(index).getInt("id_emeteur");
						pseudoFrom = contenuJSON.getJSONObject(index).getString("pseudo_emeteur");
						idTo = PreferenceManager.getDefaultSharedPreferences(params[0]).getInt(Webteam.ID, 0);
						pseudoTo = PreferenceManager.getDefaultSharedPreferences(params[0]).getString(Webteam.PSEUDO, "");
					}
					else {
						idFrom = PreferenceManager.getDefaultSharedPreferences(params[0]).getInt(Webteam.ID, 0);
						pseudoFrom = PreferenceManager.getDefaultSharedPreferences(params[0]).getString(Webteam.PSEUDO, "");
						idTo = contenuJSON.getJSONObject(index).getInt("id_receveur");
						pseudoTo = contenuJSON.getJSONObject(index).getString("pseudo_receveur");
					}
					time = contenuJSON.getJSONObject(index).getString("time");
					etatLu = contenuJSON.getJSONObject(index).getInt("lu");
					
					myListMessage.add(new MessagePrive(id,
							nomBoite,
							idFrom, 
							pseudoFrom, 
							idTo, 
							pseudoTo, 
							CallService.replaceAccentHtmlToText(titre), 
							"",
							time, 
							Message.CONTENU_NOT_LOAD,
							etatLu));
					//On vérifie que si le message existe, on récupère ses états... A savoir : Réponse, favoris, lu/non-Lu
					index++;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				L.v("GetBoiteDeReception", "Erreur lecture JSON");
				return null;
			}
			L.v("GetBoiteDeReception", "beforeReturnInExecute...");
			return myListMessage;
		}
		else {
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(List<Message> result) {
		super.onPostExecute(result);
		L.w("GetBoiteDeReception", "FINISH !");

		if (isCancelled()) {
			return;
		}
		
		observer.update(this, nomBoite);
		
	}
}

