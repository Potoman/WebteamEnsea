package com.potoman.webteam.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;

import com.potoman.tools.CallService;
import com.potoman.tools.IObserver;
import com.potoman.tools.L;
import com.potoman.tools.Ref;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ATInitEmailForSend extends AsyncTask<Context, Void, Map<String, String>> {

	public static final String KEY_SM_TOKEN = "smtoken";
	public static final String KEY_START_MESSAGE = "startMessage";
	public static final String KEY_SESSION = "session";
	public static final String KEY_PASSED_ID = "passed_id";
	public static final String KEY_SEND_TO = "send_to";
	public static final String KEY_SEND_TO_CC = "send_to_cc";
	public static final String KEY_SEND_TO_BCC = "send_to_bcc";
	public static final String KEY_SUBJECT = "subject";
	public static final String KEY_MAIL_PRIO = "mailprio";
	public static final String KEY_BODY = "body";
	public static final String KEY_SEND = "send";
	public static final String KEY_MAX_FILE_SIZE = "MAX_FILE_SIZE";
	public static final String KEY_ATTACH_FILE = "attachfile";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_SMACTION = "smaction";
	public static final String KEY_MAILBOX = "mailbox";
	public static final String KEY_COMPOSE_SESSION = "composesession";
	public static final String KEY_QUERY_STRING = "querystring";
	
	
	private static final String INIT_EMAIL_FOR_SEND = "https://silver.ensea.fr/src/compose.php?mailbox=INBOX&startMessage=1";

	private IObserver observer = null;
	private DefaultHttpClient httpClient = null;
	
	public ATInitEmailForSend(IObserver observer, DefaultHttpClient httpClient) {
		this.observer = observer;
		this.httpClient = httpClient;
	}
	
	@Override
	protected Map<String, String> doInBackground(Context... params) {
		L.v("ATInitEmailForSend", "doInBackground");
		
		Map<String, String> mapVariable = new HashMap<String, String>();
		mapVariable.put(KEY_SM_TOKEN, "");
		mapVariable.put(KEY_START_MESSAGE, "");
		mapVariable.put(KEY_SESSION, "");
		mapVariable.put(KEY_PASSED_ID, "");
		mapVariable.put(KEY_SEND_TO, "");
		mapVariable.put(KEY_SEND_TO_CC, "");
		mapVariable.put(KEY_SEND_TO_BCC, "");
		mapVariable.put(KEY_SUBJECT, "");
		mapVariable.put(KEY_MAIL_PRIO, "3");
		mapVariable.put(KEY_BODY, "");
		mapVariable.put(KEY_SEND, "Envoyer");
		mapVariable.put(KEY_MAX_FILE_SIZE, "");
		mapVariable.put(KEY_ATTACH_FILE, "");
		mapVariable.put(KEY_USERNAME, "");
		mapVariable.put(KEY_SMACTION, "");
		mapVariable.put(KEY_MAILBOX, "");
		mapVariable.put(KEY_COMPOSE_SESSION, "");
		mapVariable.put(KEY_QUERY_STRING, "mailbox=INBOX&amp;startMessage=1");
		
		HttpResponse httpResponse = null;
		
		httpResponse = CallService.askForSomething(INIT_EMAIL_FOR_SEND, httpClient, false);
		
		try {
		    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "iso-8859-1"), 8);
			String ligne = "";
			
			String keyToFind = KEY_SM_TOKEN;
			
			Pattern patternForTitre = Pattern.compile("<input type=\"hidden\" name=\"([a-zA-Z_]+)\" value=\"([0-9a-zA-Z&=;]+)\" />");
			
			while((ligne = bufferedReader.readLine()) != null) {
				L.v("ATInitEmailForSend", "ligne > |" + ligne + "|");
				Matcher m = patternForTitre.matcher(ligne);
				while (m.find()) {
					String key = m.group(1);
					String value = m.group(2);
					if (mapVariable.containsKey(key)) {
						mapVariable.put(key, value);
						L.d("ATInitEmailForSend", "key = " + key + ", value = " + value);
					}
				}	
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			L.e("ATGetEmail", "ERREUR DANS LA LECTURE DE LA REPONSE !!! ");
			return null;
		}
		
		return mapVariable;
	}

	
	@Override
	protected void onPostExecute(Map<String, String> result) {
		if (isCancelled()) {
			return;
		}
		L.d("ATGetEmail", "onPostExecute");
		observer.update(this, null);
		super.onPostExecute(result);
	}
}
