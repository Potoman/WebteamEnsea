package com.potoman.webteam.task;

import org.json.JSONException;
import org.json.JSONObject;

import com.potoman.tools.CallService;
import com.potoman.tools.IObserver;
import com.potoman.tools.L;
import com.potoman.tools.UrlService;
import com.potoman.webteam.boitemanager.boite.BoiteDeMessage;
import com.potoman.webteam.boitemanager.boite.messageprive.BoiteDeMessagePrive;
import com.potoman.webteam.boitemanager.boite.messageprive.BoiteDeMessagePriveLecture;
import com.potoman.webteam.boitemanager.message.Message;
import com.potoman.webteam.boitemanager.message.messageprive.MessagePrive;
import com.potoman.webteam.constant.Webteam;
import com.potoman.webteam.exception.ExceptionService;
import com.potoman.webteam.loggin.Root;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;


public class ATGetMessagePrive extends AsyncTask<Context, Void, Integer> { //Object : [0] : context, [1] : int, id du message.

	private IObserver observer = null;
	private Message message = null;
	
	public ATGetMessagePrive(IObserver observer, Message message) {
		this.observer = observer;
		this.message = message;
	}
	
	@Override
	protected Integer doInBackground(Context... params) {
		
		JSONObject reponseJSON = null;
		try {
			L.v("GetMessagePrive", "doInBackground : on va chercher le message sur le serveur de la webteam...");
			reponseJSON = CallService.getJsonGeth(params[0], 
					UrlService.createUrlLireMessagePrive(
							PreferenceManager.getDefaultSharedPreferences(params[0]).getString(Webteam.PSEUDO, ""), 
							PreferenceManager.getDefaultSharedPreferences(params[0]).getString(Webteam.PASSWORD, ""))
					+ "&id=" + message.getId() + "&boite=" + (message.getNomBoite().equals(Webteam.BOITE_DE_RECEPTION) ? Webteam.LECTURE_MESSAGE_PRIVE_FROM_BOITE_DE_RECEPTION : Webteam.LECTURE_MESSAGE_PRIVE_FROM_BOITE_D_ENVOIS));

		} catch (final ExceptionService e) {
			e.printStackTrace();
			return Message.CONTENU_NOT_LOAD;
		}
		if (reponseJSON != null) {
			try {
				int erreur = 0;
				erreur = (Integer) reponseJSON.getInt("erreur");
				JSONObject contenuJSON = null;
				if (erreur == 1) {
					contenuJSON = reponseJSON.getJSONObject("contenu");	
				}
				else {
					return MessagePrive.CONTENU_NOT_LOAD;
				}
				L.w("GetMessagePrive", "texte : " + contenuJSON.getString("texte"));
				message.setData(CallService.replaceAccentHtmlToText(contenuJSON.getString("texte")));
				message.setContenuLoad(Message.CONTENU_LOAD);
				message.setEtatBoite(Message.ETAT_LU);
			} catch (JSONException e) {
				e.printStackTrace();
				L.v("GetMessagePrive", "Erreur lecture JSON");
				return MessagePrive.CONTENU_NOT_LOAD;
			}
			L.v("GetMessagePrive", "beforeReturnInExecute...");
			return Message.CONTENU_LOAD;
		}
		else
			return Message.CONTENU_NOT_LOAD;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		L.v("GetMessagePrive", "onPostExecute...");

		if (isCancelled()) {
			return;
		}
		
		observer.update(this, null);
	}
}
