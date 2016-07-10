package org.example.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.example.webteam.boitemanager.boite.BoiteDeMessage;
import org.example.webteam.boitemanager.message.Message;
import org.example.webteam.boitemanager.message.email.Email;

import potoman.tools.CallService;
import potoman.tools.L;
import potoman.tools.Ref;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ATGetEmail extends AsyncTask<Context, Void, Integer> {

	private static final int STATE_PARSE_EXACT_TIME = 0;
	private static final int STATE_PARSE_EXACT_TIME_PART_TWO = 1;
	private static final int STATE_PARSE_EMAIL = 2;
	private static final int STATE_PARSE_DATA = 3;
	private static final int STATE_PARSE_FICHIER_JOINT = 4;
	private static final int STATE_PARSE_END = 5;
	
	private static final String GET_EMAIL_IN_BOITE = "https://silver.ensea.fr/src/read_body.php?mailbox=";
	private static final String GET_EMAIL_MIDDLE = "&passed_id=";
	private static final String GET_EMAIL_END = "&startMessage=1&show_more=1";
	
	private IWorkFinishOfAsyncTask iWorkFinishOfAsyncTask = null;
	private Message message = null;
	private DefaultHttpClient httpClient = null;

	private int state = STATE_PARSE_EXACT_TIME;
	
	private String key = "";
	private Ref<String> smToken = null;
	
	public ATGetEmail(IWorkFinishOfAsyncTask iWorkFinishOfAsyncTask, final Message message, DefaultHttpClient httpClient, String key, Ref<String> smToken) {
		this.iWorkFinishOfAsyncTask = iWorkFinishOfAsyncTask;
		this.message = message;
		this.httpClient = httpClient;
		this.key = key;
		this.smToken = smToken;
	}
	
	@Override
	protected Integer doInBackground(Context... params) {
		
		HttpResponse httpResponse = null;
		
		httpResponse = CallService.askForSomething(GET_EMAIL_IN_BOITE + key + GET_EMAIL_MIDDLE + message.getId() + GET_EMAIL_END, httpClient, false);
		
		String normal = "";
		

		//Pattern patternForDateExact = Pattern.compile("</tr><tr><td align=\"right\" valign=\"top\" width=\"20%\"><b>Date:&nbsp;&nbsp;</b></td>");
		Pattern patternForDateExactPartTwo = Pattern.compile(">(.*)<");
		String direction = "";
		Pattern patternForEmail = Pattern.compile("(<a href=\"/src/read_body.php\\?mailbox=" + key + "&amp;passed_id=" + message.getId() + "&amp;startMessage=1&amp;show_more=0\">moins</a>)");
		Pattern patternForEmailSingle = Pattern.compile("(<td align=\"left\" valign=\"top\" width=\"80%\">)");
		Pattern patternForEmailSingleWithTitle = Pattern.compile("&lt;(.*)&gt;");
		//Pattern patternForPiecesJointes = Pattern.compile("<b>Pi(.{1})ces jointes&nbsp;:</b>");
		Pattern patternForUrlOfPiecesJointes = Pattern.compile("<a href=\"(.+&amp;ent_id=[0-9]+&amp;passed_ent_id=[0-9]+)(&amp;absolute_dl=true)?\">(.+)</a>&nbsp;</td>");
		
		Pattern patternSmToken = Pattern.compile(".*smtoken=([a-zA-Z0-9]+)&amp;.*");
		
		//Pattern patternForEmailPartTwo = Pattern.compile("<br />(.*)<br />");
//		Pattern patternForDateAndNonLu = Pattern.compile("<td bgcolor=\"#[A-Za-z0-9]{6}\" align=\"center\" nowrap><b>(.*)</b></td>");
//		Pattern patternForTitre = Pattern.compile(".*startMessage=1\" ( title=\".*\")?>(.*)</a>");
		
		
		String time = "";
		String emailTo = "";
		String pieceJointe = "";
		
		try {
		    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "iso-8859-1"), 8);
			String ligne = "";
			int countUntil30 = 0;
			String allEmail = "";
			
			List<String> bufferFor7Line = new ArrayList<String>();
			bufferFor7Line.add("");
			bufferFor7Line.add("");
			bufferFor7Line.add("");
			bufferFor7Line.add("");
			bufferFor7Line.add("");
			bufferFor7Line.add("");
			bufferFor7Line.add("");
			bufferFor7Line.add("");
			//bufferedReader.read();
			while((ligne = bufferedReader.readLine()) != null) {
				
				L.v("ATGetEmail", "ligne > " + ligne);
				Matcher m = null;
				Matcher n = null;
				switch (state) {
				case STATE_PARSE_EXACT_TIME:
					m = patternSmToken.matcher(ligne);
					while (m.find()) {
						smToken.set(m.group(1));
						L.e("ATGetEmail", "Smtoken = " + smToken);
					}
					if (ligne.equals("</tr><tr><td align=\"right\" valign=\"top\" width=\"20%\"><b>Date:&nbsp;&nbsp;</b></td>")) {
						state = STATE_PARSE_EXACT_TIME_PART_TWO;
					}
					break;
				case STATE_PARSE_EXACT_TIME_PART_TWO:
					m = patternForDateExactPartTwo.matcher(ligne);
					while (m.find()) {
						time = m.group(1);
						//etatBoiteDeReception = Message.ETAT_NON_LU;
						L.e("ATGetMail", "DATE EXTRAIT : " + m.group(1));
						state = STATE_PARSE_EMAIL;
					}
					break;
				case STATE_PARSE_EMAIL:
					//String maString = new String(ligne.getBytes("UTF-8"), "UTF-8");
//					etatBoiteDeReception = Message.ETAT_SUPPRIME;
					boolean flag = false;
					m = patternForEmail.matcher(ligne);
					while (m.find()) {
						flag = true;
						ligne = ligne.replaceAll("\\(<a href=\"/src/read_body.php\\?mailbox=" + direction + "&amp;passed_id=" + message.getId() + "&amp;startMessage=1&amp;show_more=0\">moins</a>\\)", "");
						ligne = ligne.replaceAll("<td align=\"left\" valign=\"top\" width=\"80%\">", "");
						ligne = ligne.replaceAll("</td>", "");
						ligne = ligne.replaceAll("&nbsp;", "");
						emailTo = "{\"emails\":[{\"email\":\"" + ligne.replaceAll("<br />", "\"},{\"email\":\"") + "\"}]}";
						L.w("ATGetEmail", "emailTo : " + emailTo);
						state = STATE_PARSE_DATA;						
					}
					if (flag) {
						break;
					}
					m = patternForEmailSingle.matcher(ligne);
					while (m.find()) {
						ligne = ligne.replaceAll("<td align=\"left\" valign=\"top\" width=\"80%\">", "");
						ligne = ligne.replaceAll("</td>", "");
						n = patternForEmailSingleWithTitle.matcher(ligne);
						boolean flagBis = false;
						while (n.find()) {
							emailTo = "{\"emails\":[{\"email\":\"" + n.group(1) + "\"}]}";
							L.w("ATGetEmail", "" + emailTo);
							flagBis = true;
						}
						if (!flagBis) {
							emailTo = "{\"emails\":[{\"email\":\"" + ligne + "\"}]}";
							L.w("ATGetEmail", emailTo);
						}
						state = STATE_PARSE_DATA;
					}
					break;
				case STATE_PARSE_DATA:
					countUntil30++;
					L.d("ATGetEmail", "countUntil27 : " + countUntil30);
					if (countUntil30 == 30) {
						ligne = ligne.replaceAll("<tr><td align=\"left\"><br />", "");
						L.d("ATGetEmail", "Première ligne dans l'email : " + ligne);
						allEmail = allEmail + ligne;
					}
					if (countUntil30 > 30) { //Buffer car les 7 dernières lignes ne sont pas à prendre en compte... Je sais, c'est très geek =)
						bufferFor7Line.add(0, ligne);
						if (ligne.matches(".*<b>Pièces jointes&nbsp;:</b>.*")) {
							try {
								L.e("ATGetEmail", "4>" + bufferFor7Line.get(4));
								L.e("ATGetEmail", "5>" + bufferFor7Line.get(5));
								L.e("ATGetEmail", "6>" + bufferFor7Line.get(6));
								L.e("ATGetEmail", "7>" + bufferFor7Line.get(7));
								allEmail = allEmail + bufferFor7Line.get(7) + bufferFor7Line.get(6) + bufferFor7Line.get(5) + bufferFor7Line.get(4);
							}
							catch (Exception e) {
								e.printStackTrace();
							}
							state = STATE_PARSE_FICHIER_JOINT;
							m = patternForUrlOfPiecesJointes.matcher(ligne);
							while (m.find()) {
								L.e("ATGetEmail", "PIECE JOINTE !");
								L.i("ATGetEmail", "{\"piecesJointes\":[{\"titre\":\"" + m.group(3) + "\",\"url\":\"" + m.group(1).replace("..", "https://silver.ensea.fr") + "\"}");
								pieceJointe = "{\"piecesJointes\":[{\"titre\":\"" + m.group(3) + "\",\"url\":\"" + m.group(1).replace("..", "https://silver.ensea.fr") + "\"}";
							}
						}
						else if (ligne.matches(".*<small>Delete &amp; Prev&nbsp;\\|&nbsp;Delete &amp; Next</small>.*")) {
							if (pieceJointe.equals("")) {
								// Si nous arrivons à la fin de notre email et qu'aucune piève jointe n'as été trouvé :
								L.e("ATGetEmail", "0>" + bufferFor7Line.get(0));
								L.e("ATGetEmail", "1>" + bufferFor7Line.get(1));
								L.e("ATGetEmail", "2>" + bufferFor7Line.get(2));
								L.e("ATGetEmail", "3>" + bufferFor7Line.get(3));
								L.e("ATGetEmail", "4>" + bufferFor7Line.get(4));
								L.e("ATGetEmail", "5>" + bufferFor7Line.get(5));
								L.e("ATGetEmail", "6>" + bufferFor7Line.get(6));
								L.e("ATGetEmail", "7>" + bufferFor7Line.get(7));
								allEmail = allEmail + bufferFor7Line.get(7) + bufferFor7Line.get(6) + bufferFor7Line.get(5) + bufferFor7Line.get(4);
							}
							state = STATE_PARSE_END;
							break;
						}
						else {
							if (bufferFor7Line.size() > 7) {
								L.i("ATGetEmail", "ajout d'une ligne : " + bufferFor7Line.get(7));
								allEmail = allEmail + bufferFor7Line.get(7);
							}
						}
					}
					break;
				case STATE_PARSE_FICHIER_JOINT:
					m = patternForUrlOfPiecesJointes.matcher(ligne);
					while (m.find()) {
						L.w("ATGetEmail", ">Nouvelle pièce jointe<");
						L.i("ATGetEmail", ",{\"titre\":\"" + m.group(3) + "\",");
						L.i("ATGetEmail", "\"url\":\"" + m.group(1).replace("..", "https://silver.ensea.fr") + "\"}");
						pieceJointe = pieceJointe + ",{\"titre\":\"" + m.group(3) + "\",\"url\":\"" + m.group(1).replace("..", "https://silver.ensea.fr") + "\"}";
					}
					break;
				case STATE_PARSE_END:
					break;
				}
				normal = normal + ligne;
			}
			if (!pieceJointe.equals("")) {
				L.i("ATGetEmail", "]}");
				pieceJointe = pieceJointe + "]}";
			}
			
			message.setTime(time);
			((Email) message).setEmailTo(emailTo);
			message.setData(allEmail);
			message.setContenuLoad(Message.CONTENU_LOAD);
			((Email) message).setPieceJointe(pieceJointe);
			message.setEtatBoite(Message.ETAT_LU);
			
			bufferedReader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			L.e("ATGetEmail", "ERREUR DANS LA LECTURE DE LA REPONSE !!! ");
			return null;
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Integer result) {

		if (isCancelled()) {
			return;
		}
		L.d("ATGetEmail", "onPostExecute");
		iWorkFinishOfAsyncTask.workFinish(IWorkFinishOfAsyncTask.AT_GET_EMAIL);
		super.onPostExecute(result);
	}
	
	
}
