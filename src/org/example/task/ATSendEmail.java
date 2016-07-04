package org.example.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;

import potoman.tools.CallService;
import potoman.tools.IObserver;
import potoman.tools.L;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ATSendEmail extends AsyncTask<Context, Void, Boolean> {

	private IObserver observer = null;
	private Map<String, String> mapValue = null;
	private DefaultHttpClient httpClient = null;

	private static final String URL_SEND_EMAIL = "https://silver.ensea.fr/src/compose.php";
	
	public ATSendEmail(IObserver observer, Map<String, String> mapValue, DefaultHttpClient httpClient) {
		this.observer = observer;
		this.mapValue = mapValue;
		this.httpClient = httpClient;
	}
	
	@Override
	protected Boolean doInBackground(Context... params) {
		Boolean forReturn = false;
		HttpResponse httpResponse = null;

		Pattern patternForGoodSend = Pattern.compile("<br /><center><b>(Your mail has been sent\\.)</center></b>.*");
		
		httpResponse = CallService.askForSomethingWithPost(URL_SEND_EMAIL, httpClient, new ArrayList<String>(mapValue.keySet()), new ArrayList<String>(mapValue.values()), false);
		try {
		    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "iso-8859-1"), 8);
			String ligne = "";
			
			while((ligne = bufferedReader.readLine()) != null) {
				L.v("ATSendEmail", "ligne > |" + ligne + "|");
				Matcher m = patternForGoodSend.matcher(ligne);
				while (m.find()) {
					forReturn = true;
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			L.e("ATGetEmail", "ERREUR DANS LA LECTURE DE LA REPONSE !!! ");
		}
		return forReturn;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		if (isCancelled()) {
			return;
		}
		L.d("ATGetEmail", "onPostExecute");
		observer.update(this, null);
		super.onPostExecute(result);
	}
}
