package org.example.task;

import org.example.webteam.boitemanager.message.Message;
import org.json.JSONException;
import org.json.JSONObject;

import potoman.tools.CallService;
import potoman.tools.L;
import potoman.tools.UrlService;
import potoman.webteam.constant.Webteam;
import potoman.webteam.exception.ExceptionService;
import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class ATSupprimerMessagePrive extends AsyncTask<Context, Void, Integer> {

	public final static int MP_DELETED = 1;
	public final static int MP_NOT_DELETED = 0;
	
	private IWorkFinishOfAsyncTask iWorkFinishOfAsyncTask = null;
	private Message message = null;
	
	public ATSupprimerMessagePrive(IWorkFinishOfAsyncTask iWorkFinishOfAsyncTask, Message message) {
		this.iWorkFinishOfAsyncTask = iWorkFinishOfAsyncTask;
		this.message = message;
	}
	
	@Override
	protected Integer doInBackground(Context... params) {
		
		JSONObject reponseJSON = null;
		try {
			L.v("GetMessagePrive", "doInBackground : on va supprimer le message sur le serveur de la webteam...");
			reponseJSON = CallService.getJsonGeth(params[0], UrlService.createUrlSupprimerMessagePrive(
					PreferenceManager.getDefaultSharedPreferences(((Context)params[0])).getString(Webteam.PSEUDO, ""),
					PreferenceManager.getDefaultSharedPreferences(((Context)params[0])).getString(Webteam.PASSWORD, "")
					) + "&id=" + message.getId() + "&boite=" + (message.getNomBoite().equals(Webteam.BOITE_DE_RECEPTION) ? Webteam.LECTURE_MESSAGE_PRIVE_FROM_BOITE_DE_RECEPTION : Webteam.LECTURE_MESSAGE_PRIVE_FROM_BOITE_D_ENVOIS));
		} catch (final ExceptionService e) {
			e.printStackTrace();
			return MP_NOT_DELETED;
		}
		if (reponseJSON != null) {
			try {
				int erreur = 0;
				erreur = (Integer) reponseJSON.getInt("erreur");
				if (erreur == 1) {
					return MP_DELETED;
				}
				else {
					return MP_NOT_DELETED;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				L.v("GetMessagePrive", "Erreur lecture JSON");
				return MP_NOT_DELETED;
			}
		}
		else {
			return MP_NOT_DELETED;
		}
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		L.v("GetMessagePrive", "onPostExecute...");

		if (isCancelled()) {
			return;
		}
		switch (result) {
		case MP_DELETED:
			iWorkFinishOfAsyncTask.workFinish(IWorkFinishOfAsyncTask.AT_SUPPRIMER_MESSAGE_PRIVE_OK);
			break;
		case MP_NOT_DELETED:
			iWorkFinishOfAsyncTask.workFinish(IWorkFinishOfAsyncTask.AT_SUPPRIMER_MESSAGE_PRIVE_KO);
			break;
		}
		super.onPostExecute(result);
	}
}
