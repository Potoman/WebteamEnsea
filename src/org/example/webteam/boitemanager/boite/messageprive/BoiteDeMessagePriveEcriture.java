package org.example.webteam.boitemanager.boite.messageprive;

import org.example.task.ATSendMessagePrive;
import org.example.task.IWorkFinishOfAsyncTask;
import org.example.webteam.R;
import org.example.webteam.R.array;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;

import potoman.tools.L;
import potoman.webteam.constant.Webteam;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class BoiteDeMessagePriveEcriture extends Activity implements OnClickListener, IWorkFinishOfAsyncTask {

	public EditText myEditTextPseudo = null;
	public EditText myEditTextTitre = null;
	public EditText myEditTextMessage = null;
	private Button	myButtonSendMessage = null;

	private ProgressDialog myProgressDialog = null;
	
	private ATSendMessagePrive myGetSendMessagePrive = null;
	
	private boolean answer = false;
	
	//public int idDestinataireMessagePrive = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setTitle("Message privé > Ecriture");
		
		setContentView(R.layout.boite_de_message_ecrire);
		
		myEditTextPseudo = (EditText)findViewById(R.id.et_message_ecrire_pseudo);
		myEditTextTitre = (EditText)findViewById(R.id.et_message_ecrire_titre);
		myEditTextMessage = (EditText)findViewById(R.id.et_message_ecrire_texte);
		myButtonSendMessage = (Button)findViewById(R.id.b_message_ecrire_send);
		
		myButtonSendMessage.setOnClickListener(this);
		
		Intent intent = getIntent();
		String pseudoDestinataireMessagePrive = intent.getStringExtra("pseudoMessagePrive");
		String titreDestinataireMessagePrive = intent.getStringExtra("titreMessagePrive");
		String texteDestinataireMessagePrive = intent.getStringExtra("texteMessagePrive");
		//idDestinataireMessagePrive = intent.getIntExtra("idMessagePrive", -1); Futur utilisation ^^
		
		
		if (pseudoDestinataireMessagePrive != null) {
			answer = true;
			myEditTextPseudo.setText(pseudoDestinataireMessagePrive);
			if (titreDestinataireMessagePrive.length() >= 5) {
				if (!titreDestinataireMessagePrive.subSequence(0, 5).equals("Re : "))
					myEditTextTitre.setText("Re : " + titreDestinataireMessagePrive);
				else
					myEditTextTitre.setText(titreDestinataireMessagePrive);
			}
			else
				myEditTextTitre.setText("Re : " + titreDestinataireMessagePrive);
			//myEditTextMessage.setText("&lt;fieldset class=\"citation\"&gt;&lt;legend&gt;Citation&lt;/legend&gt;" + texteDestinataireMessagePrive + "&lt;/fieldset&gt;");
			myEditTextMessage.setText("<fieldset class=\"citation\"><legend>Citation</legend>" + texteDestinataireMessagePrive + "</fieldset>");
		}
	}

	@Override
	public void onClick(View v) {
		envoieMessage();
	}
	
	protected void envoieMessage() {
		myGetSendMessagePrive = new ATSendMessagePrive(this);//.getInstance(this);
	    if (myGetSendMessagePrive != null) {
	    	if (myGetSendMessagePrive.getStatus() == AsyncTask.Status.FINISHED) {
	    		//throw new ExceptionLireMessagePrive(ExceptionLireMessagePrive.STATE_FINISH_ASYNCH_TASK);
	    	} else if (myGetSendMessagePrive.getStatus() == AsyncTask.Status.PENDING) {
    			myProgressDialog = ProgressDialog.show(BoiteDeMessagePriveEcriture.this, getResources().getStringArray(R.array.waitingEcrireMessagePrive)[0], Webteam.getPhraseDAmbiance(), true, true, new OnCancelListener() {
    				public void onCancel(DialogInterface dialog) {
    					finish();
    				}
    			});
    			myGetSendMessagePrive.execute(getApplicationContext());
	    	} else if (myGetSendMessagePrive.getStatus() == AsyncTask.Status.RUNNING) {
	    		L.v("BoiteDeMessageEcrire", "suppressionMessage : RUNNING");	//On ne fait rien.
	    	}
	    }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	if (answer)
	    		setResult(BoiteDeMessagePriveLecture.REQUEST_CODE_ECRIRE_MESSAGE_ANSWER_KO);
	    	else
	    		setResult(BoiteDeMessagePrive.RESULT_CODE_ECRITURE_KO);
	    	finish();
	    	return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public void workFinish(int cible) {
		if (myProgressDialog != null)
			myProgressDialog.dismiss();
		if (myGetSendMessagePrive.getResultOK() == ATSendMessagePrive.MP_SEND) {
			if (answer)
				setResult(BoiteDeMessagePriveLecture.REQUEST_CODE_ECRIRE_MESSAGE_ANSWER_OK);
			else
				setResult(BoiteDeMessagePrive.RESULT_CODE_ECRITURE_OK);
			finish();
		}
	}

	@Override
	public String getStringForWaiting() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfIncrementForMyProgressDialog() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void incrementMyProgressDialog() {
		// TODO Auto-generated method stub
		
	}
}
