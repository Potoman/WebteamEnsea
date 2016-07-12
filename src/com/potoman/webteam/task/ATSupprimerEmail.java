package com.potoman.webteam.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;

import com.potoman.tools.CallService;
import com.potoman.tools.L;
import com.potoman.webteam.boitemanager.boite.BoiteDeMessage;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ATSupprimerEmail extends AsyncTask<Context, Void, Integer> {

	public final static int EMAIL_DELETED = 1;
	
	private DefaultHttpClient httpClient = null;
	private IWorkFinishOfAsyncTask iWorkFinishOfAsyncTask = null;
	private int idMessageToDelete = 0;
	private String urlBoite = "";
	private String smToken = "";
	
	private final static String urlDelete = "https://silver.ensea.fr/src/delete_message.php?mailbox=";
	private final static String urlDeleteMiddle = "&message=";
	private final static String urlDeleteMiddle2 = "&smtoken=";
	private final static String urlDeleteEnd = "&sort=0&startMessage=1";
	
	public ATSupprimerEmail(IWorkFinishOfAsyncTask iWorkFinishOfAsyncTask, DefaultHttpClient httpClient, final int idMessageToDelete, final String urlBoite, final String smToken) {
		this.iWorkFinishOfAsyncTask = iWorkFinishOfAsyncTask;
		this.httpClient = httpClient;
		if (httpClient == null) {
			L.e("ATSupprimerEmail", "http null !!!");
		}
		this.idMessageToDelete = idMessageToDelete;
		this.urlBoite = urlBoite;
		this.smToken = smToken;
	}
	
	@Override
	protected Integer doInBackground(Context... arg0) {
		
		HttpResponse httpResponse = null;
		
		httpResponse = CallService.askForSomething(urlDelete + urlBoite + urlDeleteMiddle + idMessageToDelete + urlDeleteMiddle2 + smToken + urlDeleteEnd, httpClient, false);
		
		try {
		    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "iso-8859-1"), 8);
		    
			String ligne = "";
			while ((ligne = bufferedReader.readLine()) != null) {
				L.v("ATSupressionEmail", "ligne > " + ligne);
				if (ligne.matches(".*<b>ERREUR</b>.*")) {
					return null;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			L.e("ATSupprimerEmail", "ERREUR DANS LA LECTURE DE LA REPONSE !!! ");
			return null;
		}
		
		
		return EMAIL_DELETED;
	}
	
	@Override
	protected void onPostExecute(Integer result) {

		if (isCancelled()) {
			return;
		}
		L.v("GetMessagePrive", "onPostExecute...");
		iWorkFinishOfAsyncTask.workFinish(IWorkFinishOfAsyncTask.AT_SUPPRIMER_EMAIL);
		super.onPostExecute(result);
	}

}
