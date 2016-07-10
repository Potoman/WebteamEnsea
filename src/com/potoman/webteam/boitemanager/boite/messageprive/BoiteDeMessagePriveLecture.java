package org.example.webteam.boitemanager.boite.messageprive;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.example.task.ATGetMessagePrive;
import org.example.task.ATSupprimerMessagePrive;
import org.example.task.IWorkFinishOfAsyncTask;
import org.example.webteam.R;
import org.example.webteam.R.array;
import org.example.webteam.R.drawable;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;
import org.example.webteam.boitemanager.boite.BoiteDeMessage;
import org.example.webteam.boitemanager.boite.BoiteDeMessageLecture;
import org.example.webteam.boitemanager.message.Message;
import org.example.webteam.boitemanager.message.messageprive.MessagePrive;
import org.example.webteam.eleve.FicheEleve;
import org.example.webteam.loggin.Root;

import potoman.tools.L;
import potoman.webteam.bdd.BddMessageManager;
import potoman.webteam.bdd.BddMessagePriveManager;
import potoman.webteam.constant.Webteam;
import potoman.webteam.exception.ExceptionLireMessagePrive;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BoiteDeMessagePriveLecture extends BoiteDeMessageLecture {
	
	private Button myButtonVoirFicheProfil = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {

	    requestWindowFeature(Window.PROGRESS_INDETERMINATE_OFF);
		messageBdd = new BddMessagePriveManager(this);
		setContentView(R.layout.boite_de_message_lecture);
		
		super.onCreate(savedInstanceState);

		myButtonSupprimer.setEnabled(true);
		
		myButtonVoirFicheProfil = (Button) findViewById(R.id.b_message_lecture_voir_profil);
		myButtonVoirFicheProfil.setOnClickListener(this);
		
	    setTitle("Message privé > Lecture");
		
		display();
	}

	@Override
	public void getMessageOTA() {
	    myGetMessage = getAsyncTaskMessage();
	    if (message != null && message.getContenuLoad() == Message.CONTENU_NOT_LOAD) {
			myProgressDialog = ProgressDialog.show(BoiteDeMessagePriveLecture.this, getResources().getStringArray(R.array.waitingLectureMessagePrive)[0], Webteam.getPhraseDAmbiance(), true, true, new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					cancelAT();
					finish();
				}
			});
	    }
	    else {
			setProgressBarIndeterminateVisibility(true);
	    }
		myGetMessage.execute(getApplicationContext());
	}
	
	public void display() {
		((TextView) findViewById(R.id.tv_message_lecture_titre)).setText(message.getTitre());
		if (((MessagePrive) message).getIdFrom() == PreferenceManager.getDefaultSharedPreferences(this).getInt(Webteam.ID, 0)) {
			((TextView) findViewById(R.id.tv_message_lecture_from_to_which)).setText("Destinataire : ");
			((TextView) findViewById(R.id.tv_message_lecture_from_to)).setText(((MessagePrive)message).getPseudoTo());
		}
		else {
			((TextView) findViewById(R.id.tv_message_lecture_from_to_which)).setText("Emetteur : ");
			((TextView) findViewById(R.id.tv_message_lecture_from_to)).setText(((MessagePrive)message).getPseudoFrom());
		}
		SimpleDateFormat sdfDayDateTime = new SimpleDateFormat("E dd/MM/yyyy à HH'h'mm");
    	((TextView) findViewById(R.id.tv_message_lecture_time)).setText(sdfDayDateTime.format(new Date(Integer.parseInt(message.getTime())*1000)));
		switch (message.getContenuLoad()) {
		case MessagePrive.CONTENU_LOAD:
			L.v("LireMessagePrive", "data : " + message.getData());
			myWebView = (WebView) findViewById(R.id.wv_message_lecture_data);
			myWebView.getSettings().setJavaScriptEnabled(true);
			String contenu = message.getData();
			contenu = contenu.replaceAll("&lt;fieldset class=\"citation\"&gt;&lt;legend&gt;Citation&lt;/legend&gt;", "<fieldset class=\"citation\"><legend>Citation</legend>");
			contenu = contenu.replaceAll("&lt;/fieldset&gt;", "</fieldset>");
			myWebView.loadData("<html><body>" + contenu + "</body></html>", "text/html; charset=utf-8", "UTF-8");
			break;
		case MessagePrive.CONTENU_NOT_LOAD:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v == myButtonRepondre) {
			Intent monIntent = new Intent(this, BoiteDeMessagePriveEcriture.class);
			Bundle variableDePassage = new Bundle();
			if (((MessagePrive)message).getIdFrom() == PreferenceManager.getDefaultSharedPreferences(this).getInt(Webteam.ID, 0)) {
				variableDePassage.putString("pseudoMessagePrive", ((MessagePrive)message).getPseudoTo());
			}
			else {
				variableDePassage.putString("pseudoMessagePrive", ((MessagePrive)message).getPseudoFrom());
			}
			variableDePassage.putString("titreMessagePrive", message.getTitre());
			variableDePassage.putString("texteMessagePrive", message.getData());
			variableDePassage.putInt("idMessagePrive", message.getId());
		    monIntent.putExtras(variableDePassage);
			startActivityForResult(monIntent, REQUEST_CODE_ECRIRE_MESSAGE);
		}
		else if (v == myButtonSupprimer) {
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
			builder.setMessage("Voulez-vous vraiment supprimer ce message ?").setPositiveButton("1L", dialogClickListener)
			    .setNegativeButton("0L", dialogClickListener).setTitle("Supprimation d'un message...").setIcon(R.drawable.icon).show();
		}
		else if (v == myButtonVoirFicheProfil) {
			Intent monIntent = new Intent(this, FicheEleve.class);
			Bundle variableDePassage = new Bundle();
			if (((MessagePrive)message).getIdFrom() == PreferenceManager.getDefaultSharedPreferences(this).getInt(Webteam.ID, 0)) {
				variableDePassage.putInt("idProfil", ((MessagePrive)message).getIdTo());
			}
			else {
				variableDePassage.putInt("idProfil", ((MessagePrive)message).getIdFrom());
			}
		    monIntent.putExtras(variableDePassage); 
			startActivity(monIntent);
		}
	}
	
	@Override
	public AsyncTask<Context, Void, Integer> getAsyncTaskMessage() {
		return new ATGetMessagePrive(this, message);
	}
	
	@Override
	public AsyncTask<Context, Void, Integer> getAsyncTaskSupprimer() {
		return new ATSupprimerMessagePrive(this, message);
	}
	
	@Override
	public void cancelAT() {
		if (myGetMessage != null) {
			myGetMessage.cancel(true);
		}
		if (myGetSupprimerMessage != null) {
			myGetSupprimerMessage.cancel(true);
		}
	}
	
	protected void suppressionMessage() {
		myGetSupprimerMessage = getAsyncTaskSupprimer();
    	if (myGetSupprimerMessage.getStatus() == AsyncTask.Status.FINISHED) {
    		//throw new ExceptionLireMessagePrive(ExceptionLireMessagePrive.STATE_FINISH_ASYNCH_TASK);
    	} else if (myGetSupprimerMessage.getStatus() == AsyncTask.Status.PENDING) {
			myProgressDialog = ProgressDialog.show(BoiteDeMessagePriveLecture.this, getResources().getStringArray(R.array.waitingSupprimerMessagePrive)[0], Webteam.getPhraseDAmbiance(), true, true, new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					cancelAT();
					finish();
				}
			});
			myGetSupprimerMessage.execute(getApplicationContext());
    	} else if (myGetSupprimerMessage.getStatus() == AsyncTask.Status.RUNNING) {
    		L.v("BoiteDeMessageLecture", "suppressionMessage : RUNNING");	//On ne fait rien.
    	}
	}
	
	@Override
	public void update(Object observable, Object data) {
		if (observable == myGetMessage) {
			if (myProgressDialog != null) {
				myProgressDialog.dismiss();
			}
			else {
				setProgressBarIndeterminateVisibility(false);
			}
			switch (message.getContenuLoad()) {
			case MessagePrive.CONTENU_LOAD:
				//L.v("LireMessagePrive", "Contenu chargé !");
				messageBdd.open();
				messageBdd.updateMessageContenuLoad(message);
				messageBdd.close();
				break;
			case MessagePrive.CONTENU_NOT_LOAD:
//				Toast.makeText(this, "Erreur dans l'erreur du contenu du message", Toast.LENGTH_SHORT).show();
//				myWebView = (WebView)findViewById(R.id.wv_message_lecture_data);
//				myWebView.getSettings().setJavaScriptEnabled(true);
//				myWebView.loadData("<html><body>Contenu illisible depuis le serveur de la webteam...</body></html>", "text/html", null);
				Toast.makeText(this, "Impossible de charger le contenu du message.", Toast.LENGTH_SHORT).show();
				finish();
				break;
			}
			display();
		}
	}
}



