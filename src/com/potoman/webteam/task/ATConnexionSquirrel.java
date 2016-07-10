package org.example.task;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.example.webteam.boitemanager.Squirrel;

import potoman.tools.CallService;
import potoman.tools.IObserver;
import potoman.tools.L;

import android.content.Context;
import android.database.Observable;
import android.os.AsyncTask;

public class ATConnexionSquirrel extends AsyncTask<Context, Void, Integer> {
	
	//public static final String displayPage = "https://silver.ensea.fr/";
	public static final String redirect = "https://silver.ensea.fr/src/login.php";
	
	public static final String connexionToSquirrel = "https://silver.ensea.fr/src/redirect.php";
	public static final String connexionStep2IfOkLogin = "https://silver.ensea.fr/src/webmail.php";
	
//	private static final String menuLeft = "https://silver.ensea.fr/src/left_main.php";
//
//	private static final String menuRight = "https://silver.ensea.fr/src/right_main.php";
	
	private IObserver observer = null;

	private String pseudoSquirrel = "";
	private String passwordSquirrel = "";
	
	private HttpClient httpClient = null;
	
//	private boolean lastMessage = false;
	
//	private boolean toRead = false;
	
	private int reponseAtAnswer = -1;
	
	public ATConnexionSquirrel(IObserver observer, String pseudoSquirrel, String passwordSquirrel, HttpClient httpClient, final int reponseAtAnswer) {
		this.observer = observer;
		this.pseudoSquirrel = pseudoSquirrel;
		this.passwordSquirrel = passwordSquirrel;
		this.httpClient = httpClient;
		this.reponseAtAnswer = reponseAtAnswer;
	}
	
	@Override
	protected Integer doInBackground(Context... params) {
		

		HttpResponse httpResponse = CallService.askForSomething(redirect, httpClient, false);
		String normal = "";
		try {
			StringBuffer stringBuffer = null;
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "iso-8859-1"), 8);
			String ligne = "";
			//bufferedReader.read();
			while ((ligne = bufferedReader.readLine()) != null) {
			}
			bufferedReader.close();
		} catch (Exception e) {
			L.e("Caligula", "ERREUR DANS LA LECTURE DE LA REPONSE !!! ");
			e.printStackTrace();
			return Squirrel.RESULT_CONNEXION_KO;
		}
		
		//iWorkFinishOfAsyncTask.incrementMyProgressDialog();
	    List<String> nameData = new ArrayList<String>();
		nameData.add("js_autodetect_results");
		nameData.add("just_logged_in");
		nameData.add("login_username");
		nameData.add("secretkey");
		List<String> valeurData = new ArrayList<String>();
		valeurData.add("1");
		valeurData.add("1");
		valeurData.add(pseudoSquirrel);
		valeurData.add(passwordSquirrel);
		
		httpResponse = CallService.askForSomethingWithPost(connexionToSquirrel, httpClient, nameData, valeurData, false);
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent(), "iso-8859-1"), 8);
			String ligne = "";
			//bufferedReader.read();
			while ((ligne = bufferedReader.readLine()) != null) {
				if (ligne.matches(".*Utilisateur inconnu ou mot de passe incorrect.*")) {
					return Squirrel.RESULT_CONNEXION_KO;
				}
			}
			bufferedReader.close();
		} catch (Exception e) {
			L.e("Caligula", "ERREUR DANS LA LECTURE DE LA REPONSE !!! ");
			e.printStackTrace();
			return Squirrel.RESULT_CONNEXION_KO;
		}
		
		L.v("ATConnexionSquirrel", "Connection réussi");
		return Squirrel.RESULT_CONNEXION_OK;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if (isCancelled()) {
			return;
		}
		observer.update(this, reponseAtAnswer);
	}
}

