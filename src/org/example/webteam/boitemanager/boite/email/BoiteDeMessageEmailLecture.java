package org.example.webteam.boitemanager.boite.email;

import org.apache.http.impl.client.DefaultHttpClient;
import org.example.task.ATConnexionSquirrel;
import org.example.task.ATGetEmail;
import org.example.task.ATSupprimerEmail;
import org.example.task.IWorkFinishOfAsyncTask;
import org.example.webteam.R;
import org.example.webteam.R.array;
import org.example.webteam.R.drawable;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;
import org.example.webteam.boitemanager.boite.BoiteDeMessage;
import org.example.webteam.boitemanager.boite.BoiteDeMessageLecture;
import org.example.webteam.boitemanager.message.Message;
import org.example.webteam.boitemanager.message.email.Email;
import org.example.webteam.boitemanager.message.messageprive.MessagePrive;

import potoman.tools.CallService;
import potoman.tools.L;
import potoman.tools.Ref;
import potoman.webteam.bdd.BddSquirrelManager;
import potoman.webteam.constant.Webteam;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

public class BoiteDeMessageEmailLecture extends BoiteDeMessageLecture {
    
	public DefaultHttpClient httpClient = null;
	
	private AsyncTask<Context, Void, Integer> myConnexionSquirrel = null;
	
	private Ref<String> smToken = new Ref<String>("");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

	    requestWindowFeature(Window.PROGRESS_INDETERMINATE_OFF);
		messageBdd = new BddSquirrelManager(this);
		setContentView(R.layout.boite_de_message_email_lecture);
		
		super.onCreate(savedInstanceState);
		
	    this.setTitle("Email > Lecture");
	    
		display();
	}

	@Override
	public void getMessageOTA() {
		if (urlBoite == null && message.getContenuLoad() != Message.CONTENU_LOAD) {
			finish();
			Toast.makeText(this, "Le mail ne peut pas se charger car vous n'êtes pas connecté au serveur.", Toast.LENGTH_SHORT).show();
			return;
		}
		if (urlBoite == null) {
			return;
		}
		myConnexionSquirrel = getAsyncTaskConnexion(IWorkFinishOfAsyncTask.AT_CONNEXION_SQUIRREL_FOR_READ);
		if (message.getContenuLoad() != Message.CONTENU_LOAD) {
			myProgressDialog = ProgressDialog.show(BoiteDeMessageEmailLecture.this, getResources().getStringArray(R.array.waitingLectureMessageEmail)[0], Webteam.getPhraseDAmbiance(), true, true, new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					cancelAT();
					finish();
				}
			});
		}
		else {
			setProgressBarIndeterminateVisibility(true);
		}
		myConnexionSquirrel.execute(getApplicationContext());
	}
	
	@Override
	public void cancelAT() {
		if (myConnexionSquirrel != null) {
			myConnexionSquirrel.cancel(true);
		}
		if (myGetMessage != null) {
			myGetMessage.cancel(true);
		}
		if (myGetSupprimerMessage != null) {
			myGetSupprimerMessage.cancel(true);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (myConnexionSquirrel != null) {
			cancelAT();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onClick(View v) {
		if (v == myButtonRepondre) {
			Intent monIntent = new Intent(this, BoiteDeMessageEmailEcriture.class);
			Bundle variableDePassage = new Bundle();
			variableDePassage.putString("emailMessageEmail", ((Email) message).getEmailFrom());
			
			variableDePassage.putString("titreMessageEmail", message.getTitre());
			variableDePassage.putString("texteMessageEmail", message.getData());
			//variableDePassage.putInt("idMessageEmail", message.getId());
		    monIntent.putExtras(variableDePassage);
			startActivityForResult(monIntent, REQUEST_CODE_ECRIRE_MESSAGE);
		}
		else if (v == myButtonSupprimer) {
			if (urlBoite == null) {
				Toast.makeText(this, "Vous ne pouvez pas supprimer ce mail car vous n'êtes pas connecté au serveur.", Toast.LENGTH_SHORT).show();
				return;
			}
		    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			    @Override
			    public void onClick(DialogInterface dialog, int which) {
			        switch (which){
			        case DialogInterface.BUTTON_POSITIVE:
			            suppressionMessage();
			            break;
			        case DialogInterface.BUTTON_NEGATIVE:
			            //No button clicked
			            break;
			        }
			    }
			};
			
			AlertDialog.Builder builder = new AlertDialog.Builder(this); 
			builder.setMessage("Voulez-vous vraiment supprimer cet email ?").setPositiveButton("1L", dialogClickListener)
			    .setNegativeButton("0L", dialogClickListener).setTitle("Supprimation d'un email...").setIcon(R.drawable.icon).show();
		}
	}
	
	public void display() {
		((TextView)findViewById(R.id.tv_message_lecture_titre)).setText(message.getTitre());
		((TextView)findViewById(R.id.tv_message_lecture_from)).setText(((Email) message).getEmailFrom());
		String emailToDisplay = ((Email) message).getEmailTo().get(0);
		for (int i = 1; i < ((Email) message).getEmailTo().size(); i++) {
			emailToDisplay = emailToDisplay + ", " + ((Email) message).getEmailTo().get(i);
		}
		((TextView)findViewById(R.id.tv_message_lecture_to)).setText(emailToDisplay);
		//SimpleDateFormat sdfDayDateTime = new SimpleDateFormat("E dd/MM/yyyy à HH'h'mm");
    	((TextView)findViewById(R.id.tv_message_lecture_time)).setText(message.getTime());
		switch (message.getContenuLoad()) {
		case MessagePrive.CONTENU_LOAD:
			if (myWebView != null) {
				break;
			}
			L.v("BoiteDeMessageEmailLecture", "data : " + message.getData());
			myWebView = (WebView) findViewById(R.id.wv_message_lecture_data);
			myWebView.getSettings().setJavaScriptEnabled(true);
			String contenu = message.getData();
			L.e("BoiteDeMEssageEmaiLecture", "Affichage du contenu de l'email avant son affichage dans la WebView : " + message.getData());
			
//			contenu = contenu.replaceAll("&lt;fieldset class=\"citation\"&gt;&lt;legend&gt;Citation&lt;/legend&gt;", "<fieldset class=\"citation\"><legend>Citation</legend>");
//			contenu = contenu.replaceAll("&lt;/fieldset&gt;", "</fieldset>");
			//myWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
			contenu = contenu.replace("#", "&#23;");
			contenu = contenu.replace("%", "&#25;");
			contenu = contenu.replace("\\", "&#27;");
			contenu = contenu.replace("?", "&#3f;");
			contenu = contenu.replace("<pre>", "<p>");
			contenu = contenu.replace("</pre>", "</p>");
			myWebView.loadData("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><html><body>" + contenu + "</body></html>", "text/html; charset=utf-8", "UTF-8"); //us-ascii
//			myWebView.setMinimumWidth(getWindow().getWindowManager().getDefaultDisplay().getWidth());
//			myWebView.setVerticalScrollBarEnabled(false);
//			myWebView.setHorizontalScrollBarEnabled(false);
			break;
		case MessagePrive.CONTENU_NOT_LOAD:
			break;
		}
	}

	public AsyncTask<Context, Void, Integer> getAsyncTaskConnexion(final int reponseAtAnswer) {
		httpClient = new DefaultHttpClient();
		CallService.setParam(httpClient, false);
		return new ATConnexionSquirrel(this, PreferenceManager.getDefaultSharedPreferences(this).getString(Webteam.PSEUDO_WEBMAIL, ""), PreferenceManager.getDefaultSharedPreferences(this).getString(Webteam.PASSWORD_WEBMAIL, ""), httpClient, reponseAtAnswer);
	}
	
	@Override
	public AsyncTask<Context, Void, Integer> getAsyncTaskMessage() {
		return new ATGetEmail(this, message, httpClient, urlBoite, smToken);
	}

	@Override
	public AsyncTask<Context, Void, Integer> getAsyncTaskSupprimer() {
		return new ATSupprimerEmail(this, httpClient, message.getId(), urlBoite, smToken.get());
	}

	@Override
	public String getStringForWaiting() {
		return null;
	}

	@Override
	public int getNumberOfIncrementForMyProgressDialog() {
		return 0;
	}

	@Override
	public void incrementMyProgressDialog() {
	}
	
	protected void suppressionMessage() {
		if (urlBoite == null) {
			Toast.makeText(this, "Vous ne pouvez pas supprimer cet Email car vous n'êtes pas connecté au serveur.", Toast.LENGTH_SHORT).show();
			return;
		}
		if (smToken.get() == null || smToken.get().equals("")) {
			Toast.makeText(this, "Pour la suppression d'un Email, il faut attendre la réponse du chargement de l'Email...", Toast.LENGTH_SHORT).show();
			return;
		}
		myGetSupprimerMessage = getAsyncTaskSupprimer();
		myProgressDialog = ProgressDialog.show(BoiteDeMessageEmailLecture.this, getResources().getStringArray(R.array.waitingLectureMessageEmailSupprimer)[0], Webteam.getPhraseDAmbiance(), true, true, new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				cancelAT();
				finish();
			}
		});
		myGetSupprimerMessage.execute(getApplicationContext());
	}
	
	@Override
	public void update(Object observable, Object data) {
		if (myProgressDialog != null) {
			myProgressDialog.dismiss();
		}
		else {
			setProgressBarIndeterminateVisibility(false);
		}
		Integer cible = (Integer) data;
		switch (cible) {
		case IWorkFinishOfAsyncTask.AT_CONNEXION_SQUIRREL_FOR_READ:
			myGetMessage = getAsyncTaskMessage();
			myGetMessage.execute(getApplicationContext());
			break;
		case IWorkFinishOfAsyncTask.AT_CONNEXION_SQUIRREL_FOR_DELETE:
			myGetSupprimerMessage = getAsyncTaskSupprimer();
			myGetSupprimerMessage.execute(getApplicationContext());
			break;
		}
	}
}
