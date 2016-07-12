package com.potoman.webteam.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;

import com.potoman.tools.CallService;
import com.potoman.tools.IObserver;
import com.potoman.webteam.boitemanager.message.Message;
import com.potoman.webteam.boitemanager.message.email.Email;
import com.potoman.webteam.constant.Webteam;
import com.potoman.webteam.loggin.Root;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class ATGetBoiteSquirrel extends AsyncTask<Context, Void, List<Message>> {

	private static final int STATE_PARSE_EMAIL_AND_ID = 0;
	private static final int STATE_PARSE_DATE = 1;
	private static final int STATE_PARSE_TITRE = 2;
	//private static final int STATE_PARSE_STOP = 3;
	
	private IObserver observer = null;
	private DefaultHttpClient httpClient = null;
	private String pseudoSquirrel = "";
	private String key = "";
	private String nomBoite = "";
	
	private int state = STATE_PARSE_EMAIL_AND_ID;
	
	private static final String urlBoite = "https://silver.ensea.fr/src/right_main.php?PG_SHOWALL=1&sort=0&startMessage=1&mailbox=";
	
	private int lastMessageInBoite = -1;
	
	public ATGetBoiteSquirrel(IObserver observer, DefaultHttpClient httpClient, final int lastMessageInBoite, final String key, final String nomBoite) {
		this.observer = observer;
		this.httpClient = httpClient;
		this.state = STATE_PARSE_EMAIL_AND_ID;
		this.lastMessageInBoite = lastMessageInBoite;
		this.key = key;
		this.nomBoite = nomBoite;
	}
	
	@Override
	protected List<Message> doInBackground(Context... params) {
		
		HttpResponse httpResponse = CallService.askForSomething(urlBoite + key, httpClient, false);
		//iWorkFinishOfAsyncTask.incrementMyProgressDialog();
		String normal = "";
		
		List<Message> listMessage = new ArrayList<Message>();
		
		Pattern patternForEmailAndId = Pattern.compile("<td bgcolor=\"#[A-Za-z0-9]{6}\" align=\"left\" title=\"(.*)\"><label for=\"msg([0-9]+)\">");
		Pattern patternForDateAndLu = Pattern.compile("<td bgcolor=\"#[A-Za-z0-9]{6}\" align=\"center\" nowrap>(.*)</td>");
		Pattern patternForDateAndNonLu = Pattern.compile("<td bgcolor=\"#[A-Za-z0-9]{6}\" align=\"center\" nowrap><b>(.*)</b></td>");
		Pattern patternForTitre = Pattern.compile(".*startMessage=1\" ( title=\".*\")?>(.*)</a>");
		
		String emailFrom = "";
		int id = 0;
		String date = "";
		String titre = "";
		int etatBoite = Message.ETAT_NON_LU;
		
		try {
		    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "iso-8859-1"), 8);
			String ligne = "";
			boolean flagContinue = true;
			//bufferedReader.read();
			while ((ligne = bufferedReader.readLine()) != null && flagContinue) {
				Matcher m = null;
				switch (state) {
				case STATE_PARSE_EMAIL_AND_ID:
					m = patternForEmailAndId.matcher(ligne);
					while (m.find()) {
						emailFrom = m.group(1);
						id = Integer.parseInt(m.group(2));
						if (id <= lastMessageInBoite) {
							flagContinue = false;
							break;
						}
//						L.e("ATGetBoiteDeReceptionSquirrel", "LIEN EXTRAIT : " + m.group(1));
//						L.e("ATGetBoiteDeReceptionSquirrel", "ID EXTRAIT : " + m.group(2));
						state = STATE_PARSE_DATE;
					}
					break;
				case STATE_PARSE_DATE:
					//String maString = new String(ligne.getBytes("UTF-8"), "UTF-8");
					etatBoite = Message.ETAT_SUPPRIME;
					m = patternForDateAndNonLu.matcher(ligne);
					while (m.find()) {
						date = m.group(1);
						etatBoite = Message.ETAT_NON_LU;
						//L.e("ATGetBoiteDeReceptionSquirrel", "DATE EXTRAIT : " + m.group(1) + ", AND NON LU");
						state = STATE_PARSE_TITRE;
					}
					if (etatBoite == Message.ETAT_SUPPRIME) {
						m = patternForDateAndLu.matcher(ligne);
						while (m.find()) {
							date = m.group(1);
							etatBoite = Message.ETAT_LU;
							//L.e("ATGetBoiteDeReceptionSquirrel", "DATE EXTRAIT : " + m.group(1) + ", AND LU");
							state = STATE_PARSE_TITRE;
						}
					}
					break;
				case STATE_PARSE_TITRE:
					m = patternForTitre.matcher(ligne);
					while (m.find()) {
						//L.e("ATGetBoiteDeReceptionSquirrel", "TITRE EXTRAIT : " + m.group(2));
						titre = m.group(2);
						state = STATE_PARSE_EMAIL_AND_ID;
						
						listMessage.add(new Email(id, 
								emailFrom, 
								"{\"emails\":[{\"email\":\"" + PreferenceManager.getDefaultSharedPreferences(params[0]).getString(Webteam.PSEUDO_WEBMAIL, "") + "@ensea.fr\"}]}", 
								CallService.replaceAccentHtmlToText(titre), 
								date, 
								etatBoite,
								nomBoite,
								PreferenceManager.getDefaultSharedPreferences(params[0]).getString(Webteam.PSEUDO_WEBMAIL, "")));
					}
					break;
				}
				normal = normal + ligne;
			}
			bufferedReader.close();
		}
		catch (Exception e) {
			//L.e("Caligula", "ERREUR DANS LA LECTURE DE LA REPONSE !!! ");
			return null;
		}
		//CallService.lireHttpResponse(CallService.askForSomething("https://silver.ensea.fr/src/read_body.php?mailbox=INBOX&passed_id=3083&startMessage=1&show_more=1", httpClient));
		return listMessage;
	}
	
	@Override
	protected void onPostExecute(List<Message> result) {
		super.onPostExecute(result);
		if (isCancelled()) {
			return;
		}
		observer.update(this, nomBoite);
	}
}
