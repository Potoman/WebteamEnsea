package org.example.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.example.webteam.boitemanager.message.Message;

import potoman.tools.CallService;
import potoman.tools.IObserver;
import potoman.tools.L;
import potoman.webteam.constant.Webteam;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class ATGetListeBoiteSquirrel extends ATGeneric<Context, Void, Map<String, String>>{

	private DefaultHttpClient httpClient = null;
	

	private static final String boiteDEnvois = "https://silver.ensea.fr/src/left_main.php";
	
	public ATGetListeBoiteSquirrel(IObserver observer, DefaultHttpClient httpClient) {
		super(observer);
		this.httpClient = httpClient;
	}

	@Override
	protected Map<String, String> doInBackground(Context... params) {
		HttpResponse httpResponse = CallService.askForSomething(boiteDEnvois, httpClient, false);
		Map<String, String> mapNomBoite = new HashMap<String, String>();
		Pattern patternForNameBoite = Pattern.compile("<span style=\"white-space: nowrap;\"><tt>&nbsp;&nbsp;</tt>(<b>)?<a href=\"right_main\\.php\\?PG_SHOWALL=0&amp;sort=0&amp;startMessage=1&amp;mailbox=(.*)\" target=\"right\" .*\">([\\w ]+)</(font></)?a>(</b>)?(&nbsp;<small>\\([0-9]+\\)</small>)?(</span><br />)?");
		
		try {
		    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "iso-8859-1"), 8);
			String ligne = "";
			//bufferedReader.read();
			while((ligne = bufferedReader.readLine()) != null) {
				//L.v("ATGetListeBoiteSquirrel", "Ligne>" + ligne);
				Matcher m = patternForNameBoite.matcher(ligne);
				while (m.find()) {
					//Log.e("ATGetListBotieSquirrel", "key = " + m.group(3) + ", value = " + m.group(2));
					mapNomBoite.put(m.group(3), m.group(2)); // En clé on met le nom de la boite, et ensuite en valeur on met la clé pour l'url.
				}
			}
			bufferedReader.close();
			return mapNomBoite;
		}
		catch (Exception e) {
			e.printStackTrace();
			L.e("Caligula", "ERREUR DANS LA LECTURE DE LA REPONSE !!! ");
			return null;
		}
	}
	
	@Override
	protected void onPostExecute(Map<String, String> result) {
		if (isCancelled()) {
			return;
		}
		getObserver().update(this, null);
		
		super.onPostExecute(result);
	}
}
