package com.potoman.webteam.boitemanager.boite;

import java.util.concurrent.ExecutionException;

import org.example.webteam.R;

import com.potoman.tools.IObserver;
import com.potoman.tools.L;
import com.potoman.webteam.TempData;
import com.potoman.webteam.bdd.BddMessageManager;
import com.potoman.webteam.boitemanager.boite.messageprive.BoiteDeMessagePrive;
import com.potoman.webteam.boitemanager.message.Message;
import com.potoman.webteam.boitemanager.message.messageprive.MessagePrive;
import com.potoman.webteam.task.IWorkFinishOfAsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Observable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public abstract class BoiteDeMessageLecture extends Activity implements IWorkFinishOfAsyncTask, OnClickListener, IObserver {
	
	protected Message message = null;
	
	protected Object data = null;

	protected ProgressDialog myProgressDialog = null;

	protected Button myButtonRepondre = null;
    protected Button myButtonSupprimer = null;
    
    protected BddMessageManager messageBdd = null;
    
    protected AsyncTask<Context, Void, Integer> myGetMessage = null;
    protected AsyncTask<Context, Void, Integer> myGetSupprimerMessage = null;
    
    
    public final static int SUPPRIMER_MESSAGE = 0;
    public final static int CHARGEMENT_MESSAGE = 1;

    public final static int REQUEST_CODE_ECRIRE_MESSAGE = 0;
		public static final int REQUEST_CODE_ECRIRE_MESSAGE_ANSWER_OK = 0;
		public static final int REQUEST_CODE_ECRIRE_MESSAGE_ANSWER_KO = 1; 
    
	protected WebView myWebView = null;

	protected String nomBoite = "";
	protected String urlBoite = "";

	protected boolean forceSynchro = false;
	
	
	
	protected void onCreate(android.os.Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
	    
		myButtonRepondre = (Button)findViewById(R.id.b_message_lecture_repondre);
		myButtonSupprimer = (Button)findViewById(R.id.b_message_lecture_supprimer);
		myButtonSupprimer.setEnabled(false);
	
		myButtonRepondre.setOnClickListener(this);
		myButtonSupprimer.setOnClickListener(this);
		
		Intent intent = getIntent();
		int idMessage = intent.getIntExtra("idMessage", 0);
		nomBoite = intent.getStringExtra("nomBoite");
		urlBoite = intent.getStringExtra("urlBoite");
		L.i("BoiteDeMessageLecture", "idMessage = " + idMessage);
		L.i("BoiteDeMessageLecture", "NomBoite = " + nomBoite);
		if (nomBoite == null) {
			finish();
		}
		
		data = getLastNonConfigurationInstance();
		if (data == null) {
			messageBdd.open();
			message = messageBdd.getMessage(idMessage, nomBoite);
			messageBdd.close();
		}
		else {
			message = ((TempData) data).monMessage;
		}
		if (message == null) {
			getMessageOTA();
		}
		else {
			if (message.getContenuLoad() == Message.CONTENU_NOT_LOAD) {
				getMessageOTA();
			}
			else {
				myButtonSupprimer.setEnabled(true);
			}
		}
	}

	public Message getMessage() {
		return message;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		TempData temp = new TempData();
		temp.monMessage = message;
		return temp;
	}
	
	
	
	public void workFinish(int cible) {
		switch (cible) {
		case IWorkFinishOfAsyncTask.AT_SUPPRIMER_MESSAGE_PRIVE_OK:
			messageBdd.open();
			messageBdd.deleteMessage(message.getId(), message.getNomBoite());
			messageBdd.close();
			setResult(BoiteDeMessage.RESULT_CODE_LECTURE_MESSAGE_DELETED);
			finish();
			Toast.makeText(this, "Message supprimé ! :)", Toast.LENGTH_SHORT).show();
			break;
		case IWorkFinishOfAsyncTask.AT_SUPPRIMER_MESSAGE_PRIVE_KO:
			Toast.makeText(this, "Message non supprimé ! :(", Toast.LENGTH_SHORT).show();
			break;
		case IWorkFinishOfAsyncTask.AT_GET_EMAIL:
			switch (message.getContenuLoad()) {
			case MessagePrive.CONTENU_LOAD:
				L.v("LireMessagePrive", "Contenu chargé ! " + message.getContenuLoad());
				messageBdd.open();
				messageBdd.updateMessageContenuLoad(message);
				messageBdd.close();
				myButtonSupprimer.setEnabled(true);
				break;
			case MessagePrive.CONTENU_NOT_LOAD:
				Toast.makeText(this, "Erreur dans l'erreur du contenu du message", Toast.LENGTH_SHORT).show();
				myWebView = (WebView)findViewById(R.id.wv_message_lecture_data);
				myWebView.getSettings().setJavaScriptEnabled(true);
				myWebView.loadData("<html><body>Contenu illisible depuis le serveur de la webteam...</body></html>", "text/html", null);
				break;
			}
			display();
			break;
		case IWorkFinishOfAsyncTask.AT_SUPPRIMER_EMAIL:
			try {
				if (myGetSupprimerMessage.get() == null) {
					Toast.makeText(this, "Impossible de supprimer l'email", Toast.LENGTH_SHORT).show();
				}
				else {
					messageBdd.open();
					messageBdd.deleteMessage(message.getId(), nomBoite);
					messageBdd.close();
					setResult(BoiteDeMessage.RESULT_CODE_LECTURE_MESSAGE_DELETED);
					finish();
					Toast.makeText(this, "Email supprimé ! :)", Toast.LENGTH_SHORT).show();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			break;
		}
		if (myProgressDialog != null)
			myProgressDialog.dismiss();
	}
	
	public String getNomBoite() {
		return nomBoite;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case REQUEST_CODE_ECRIRE_MESSAGE:
			switch (resultCode) {
			case REQUEST_CODE_ECRIRE_MESSAGE_ANSWER_OK:
				forceSynchro = true;
				break;
			case REQUEST_CODE_ECRIRE_MESSAGE_ANSWER_KO:
				break;
			}
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	L.v("BoiteDeMessageLecture", "touche BACK !");
	    	if (forceSynchro) {
		    	L.v("BoiteDeMessageLecture", "forceSynchro = true");
	    		switch (message.getContenuLoad()) {
	    		case MessagePrive.CONTENU_LOAD:
	    			setResult(BoiteDeMessagePrive.RESULT_CODE_LECTURE_MESSAGE_CONTENU_LOAD_AND_ANSWER);
	    			break;
	    		case MessagePrive.CONTENU_NOT_LOAD:
	    			setResult(BoiteDeMessagePrive.RESULT_CODE_LECTURE_MESSAGE_CONTENU_NOT_LOAD_AND_ANSWER);
	    			break;
	    		}
	    	}
	    	else {
		    	L.v("BoiteDeMessageLecture", "forceSynchro = false : " + message.getContenuLoad());
	    		setResult(message.getContenuLoad());
	    	}
	    	finish();
	    	return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	public abstract void cancelAT();
	
	public abstract void display();
	public abstract void getMessageOTA();
	
	public abstract AsyncTask<Context, Void, Integer> getAsyncTaskMessage();
	public abstract AsyncTask<Context, Void, Integer> getAsyncTaskSupprimer();
	
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
}

