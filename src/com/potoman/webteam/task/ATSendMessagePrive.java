package com.potoman.webteam.task;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.potoman.tools.CallService;
import com.potoman.tools.L;
import com.potoman.tools.UrlService;
import com.potoman.webteam.boitemanager.boite.messageprive.BoiteDeMessagePriveEcriture;
import com.potoman.webteam.constant.Webteam;
import com.potoman.webteam.exception.ExceptionService;
import com.potoman.webteam.loggin.Root;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class ATSendMessagePrive extends AsyncTask<Context, Void, Integer> {
	private int resultOK = MP_NOT_SEND;

	public final static int MP_SEND = 1;
	public final static int MP_NOT_SEND = 0;
	
	private IWorkFinishOfAsyncTask iWorkFinishOfAsyncTask = null;
	
	public ATSendMessagePrive(IWorkFinishOfAsyncTask iWorkFinishOfAsyncTask) {
		this.iWorkFinishOfAsyncTask = iWorkFinishOfAsyncTask;
		resultOK = MP_NOT_SEND;
	}
	
	@Override
	protected Integer doInBackground(Context... params) {
		JSONObject reponseJSON = null;
		try {
			L.v("ATSendMessagePrive", "doInBackground : on va envoyer le message priv�...");
			List<String> nameData = new ArrayList<String>();
			nameData.add("pseudo");
			nameData.add("titre");
			nameData.add("texte");
			//nameData.add("idReponse");
			List<String> valeurData = new ArrayList<String>();
			valeurData.add(((BoiteDeMessagePriveEcriture)iWorkFinishOfAsyncTask).myEditTextPseudo.getText().toString());
			valeurData.add(((BoiteDeMessagePriveEcriture)iWorkFinishOfAsyncTask).myEditTextTitre.getText().toString());
			valeurData.add(((BoiteDeMessagePriveEcriture)iWorkFinishOfAsyncTask).myEditTextMessage.getText().toString());
			//valeurData.add("" + myBoiteDeMessageEcrire.idDestinataireMessagePrive);
			reponseJSON = CallService.getJsonPost(params[0], UrlService.createUrlEcrireMessagePrive(
					PreferenceManager.getDefaultSharedPreferences(((Context)params[0])).getString(Webteam.PSEUDO, ""),
					PreferenceManager.getDefaultSharedPreferences(((Context)params[0])).getString(Webteam.PASSWORD, "")
					), nameData, valeurData);
		} catch (final ExceptionService e) {
			e.printStackTrace();
			return MP_NOT_SEND;
		}
		if (reponseJSON != null) {
			try {
				int erreur = 0;
				erreur = (Integer) reponseJSON.getInt("erreur");
				if (erreur == 1) {
					return MP_SEND;
				}
				else if (erreur == 54) {
					Toast.makeText((BoiteDeMessagePriveEcriture)iWorkFinishOfAsyncTask, "Ce pseudo d'éxiste pas !", Toast.LENGTH_SHORT).show();
					return MP_NOT_SEND;
				}
				else
					return MP_NOT_SEND;
				
			} catch (JSONException e) {
				e.printStackTrace();
				L.v("ATSendMessagePrive", "Erreur lecture JSON");
				return MP_NOT_SEND;
			}
		}
		else
			return MP_NOT_SEND;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		L.v("ATSendMessagePrive", "onPostExecute...");

		if (isCancelled()) {
			return;
		}
		resultOK = result;
		switch (result) {
		case MP_SEND:
			Toast.makeText((BoiteDeMessagePriveEcriture)iWorkFinishOfAsyncTask, "Message envoyé :)", Toast.LENGTH_SHORT).show();
			break;
		case MP_NOT_SEND:
			Toast.makeText((BoiteDeMessagePriveEcriture)iWorkFinishOfAsyncTask, "Message non envoyé :(", Toast.LENGTH_SHORT).show();
			break;
		}
		iWorkFinishOfAsyncTask.workFinish(0);
		super.onPostExecute(result);
	}
	
	public int getResultOK() {
		return resultOK;
	}
}
