package com.potoman.webteam.task;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.potoman.tools.CallService;
import com.potoman.tools.IObserver;
import com.potoman.tools.L;
import com.potoman.tools.UrlService;
import com.potoman.webteam.boitemanager.message.Message;
import com.potoman.webteam.constant.Webteam;
import com.potoman.webteam.exception.ExceptionService;
import com.potoman.webteam.loggin.Root;
import com.potoman.webteam.ragot.Ragot;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class ATGetRagot extends ATGeneric<Context, Void, List<Ragot>> {

	public ATGetRagot(IObserver observer) {
		super(observer);
	}

	@Override
	protected List<Ragot> doInBackground(Context... params) {
		List<Ragot> myListRagot = new ArrayList<Ragot>();
		JSONObject reponseJSON = null;
		
		try {
			L.v("ATGetRagot", "doInBackground : on va chercher les ragots sur le serveur de la webteam...");
			reponseJSON = CallService.getJsonGeth(params[0], UrlService.createUrlRagot(
					PreferenceManager.getDefaultSharedPreferences(((Context)params[0])).getString(Webteam.PSEUDO, ""),
					PreferenceManager.getDefaultSharedPreferences(((Context)params[0])).getString(Webteam.PASSWORD, "")
					));
		} catch (final ExceptionService e) {
			e.printStackTrace();
			return null;
		}
		
		if (reponseJSON == null) {
			return null;
		}
		
		int index = 0;
		try {
			JSONArray contenuJSON = reponseJSON.getJSONArray("contenu");
			while (!contenuJSON.isNull(index)) {
	
				myListRagot.add(new Ragot(contenuJSON.getJSONObject(index).getString("ragot"),
					contenuJSON.getJSONObject(index).getInt("id_posteur"),
					contenuJSON.getJSONObject(index).getString("pseudo"),
					contenuJSON.getJSONObject(index).getLong("time"),
					0, 0, 0,
					contenuJSON.getJSONObject(index).getString("new")));
				index++;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		int erreur = 0;
		try {
			erreur = (Integer) reponseJSON.getInt("erreur");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		if (erreur != 1) {
			return null;
		}
		
		return myListRagot;
	}

	@Override
	protected void onPostExecute(List<Ragot> result) {
		L.w("GetRagot", "FINISH !");
		if (isCancelled()) {
			return;
		}
		getObserver().update(this, null);
		super.onPostExecute(result);
	}
	
}
