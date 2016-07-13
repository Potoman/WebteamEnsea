package com.potoman.webteam.boitemanager.boite.email;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.impl.client.DefaultHttpClient;

import com.potoman.tools.CallService;
import com.potoman.tools.IObserver;
import com.potoman.tools.L;
import com.potoman.webteam.R;
import com.potoman.webteam.boitemanager.Squirrel;
import com.potoman.webteam.boitemanager.boite.BoiteDeMessage;
import com.potoman.webteam.constant.Webteam;
import com.potoman.webteam.task.ATConnexionSquirrel;
import com.potoman.webteam.task.ATInitEmailForSend;
import com.potoman.webteam.task.ATSendEmail;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class BoiteDeMessageEmailEcriture extends Activity implements OnClickListener, IObserver {

	public EditText myEditTextEmail = null;
	public EditText myEditTextTitre = null;
	public EditText myEditTextMessage = null;
	private Button	myButtonSendMessage = null;

	private ProgressDialog myProgressDialog = null;
	
	private ATConnexionSquirrel myConnexionSquirrel = null;
	private ATInitEmailForSend myInitEmailForSend = null;
	private ATSendEmail mySendEmail = null;
	
	private boolean answer = false;
	
	private DefaultHttpClient httpClient = null;

	Map<String, String> mapField = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_PROGRESS);
		setProgressBarVisibility(true);
		setProgress(0);
		
		setContentView(R.layout.boite_de_message_email_ecriture);
		
		setTitle("Email > Ecriture");
		
		myEditTextEmail = (EditText)findViewById(R.id.et_message_email_ecrire_email);
		myEditTextTitre = (EditText)findViewById(R.id.et_message_email_ecrire_titre);
		myEditTextMessage = (EditText)findViewById(R.id.et_message_email_ecrire_texte);
		myButtonSendMessage = (Button)findViewById(R.id.b_message_email_ecrire_send);
		myButtonSendMessage.setText("Envoyer l'email");
		myButtonSendMessage.setEnabled(false);
		
		myButtonSendMessage.setOnClickListener(this);
		
		Intent intent = getIntent();
		String emailDestinataireMessagePrive = intent.getStringExtra("emailMessageEmail");
		String titreDestinataireMessagePrive = intent.getStringExtra("titreMessageEmail");
		String texteDestinataireMessagePrive = intent.getStringExtra("texteMessageEmail");
		//idDestinataireMessagePrive = intent.getIntExtra("idMessageEmail", -1); Futur utilisation ^^
		String pseudoSquirrel = intent.getStringExtra("pseudoSquirrel");
		String passwordSquirrel = intent.getStringExtra("passwordSquirrel");
		
		
		if (emailDestinataireMessagePrive != null) {
			answer = true;
			myEditTextEmail.setText(emailDestinataireMessagePrive);
			if (titreDestinataireMessagePrive.length() >= 5) {
				if (!titreDestinataireMessagePrive.subSequence(0, 5).equals("Re : "))
					myEditTextTitre.setText("Re : " + titreDestinataireMessagePrive);
				else
					myEditTextTitre.setText(titreDestinataireMessagePrive);
			}
			else
				myEditTextTitre.setText("Re : " + titreDestinataireMessagePrive);
			//myEditTextMessage.setText("&lt;fieldset class=\"citation\"&gt;&lt;legend&gt;Citation&lt;/legend&gt;" + texteDestinataireMessagePrive + "&lt;/fieldset&gt;");
			myEditTextMessage.setText("------------------------\n\n" + texteDestinataireMessagePrive + "");
		}
		
		httpClient = new DefaultHttpClient();
		CallService.setParam(httpClient, false);
		L.e("BoiteDeMessageEmailEcriture", "Before launch connect");
		myConnexionSquirrel = new ATConnexionSquirrel(this, 
				PreferenceManager.getDefaultSharedPreferences(this).getString(Webteam.PSEUDO_WEBMAIL, ""), 
				PreferenceManager.getDefaultSharedPreferences(this).getString(Webteam.PASSWORD_WEBMAIL, ""), 
				httpClient, 
				0);
		myConnexionSquirrel.execute(this);
	}

	@Override
	public void onClick(View v) {
		myButtonSendMessage.setEnabled(false);
		mapField.put(ATInitEmailForSend.KEY_SEND_TO, myEditTextEmail.getText().toString().trim());
		mapField.put(ATInitEmailForSend.KEY_SUBJECT, myEditTextTitre.getText().toString().trim());
		mapField.put(ATInitEmailForSend.KEY_BODY, myEditTextMessage.getText().toString().trim());
		
		mySendEmail = new ATSendEmail(this, mapField, httpClient);
		mySendEmail.execute(this);
	}

	@Override
	public void update(Object observable, Object data) {
		if (observable == myConnexionSquirrel) {
			setProgress(5000);
			try {
				Integer result = myConnexionSquirrel.get();
				if (result == Squirrel.RESULT_CONNEXION_OK) {
					L.e("BoiteDeMessageEmailEcriture", "before init");
					myInitEmailForSend = new ATInitEmailForSend(this, httpClient);
					myInitEmailForSend.execute(this);
				}
				else {
					Toast.makeText(this, "Impossible de se connecter à la page d'envois d'email.", Toast.LENGTH_SHORT).show();
					setResult(BoiteDeMessage.RESULT_CODE_ECRITURE_KO);
					finish();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				Toast.makeText(this, "Impossible de se connecter à la page d'envois d'email.", Toast.LENGTH_SHORT).show();
				setResult(BoiteDeMessage.RESULT_CODE_ECRITURE_KO);
				finish();
			} catch (ExecutionException e) {
				e.printStackTrace();
				Toast.makeText(this, "Impossible de se connecter à la page d'envois d'email.", Toast.LENGTH_SHORT).show();
				setResult(BoiteDeMessage.RESULT_CODE_ECRITURE_KO);
				finish();
			}
		}
		else if (observable == myInitEmailForSend) {
			setProgress(10000);
			try {
				mapField = myInitEmailForSend.get();
				if (mapField == null) {
					Toast.makeText(this, "Impossible d'initialiser la page d'envois d'email.", Toast.LENGTH_SHORT).show();
					setResult(BoiteDeMessage.RESULT_CODE_ECRITURE_KO);
					finish();
				}
				else {
					myButtonSendMessage.setEnabled(true);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		else if (observable == mySendEmail) {
			try {
				Boolean result = mySendEmail.get();
				if (result) {
					Toast.makeText(this, "Votre email a bien été envoyé avec succés.", Toast.LENGTH_SHORT).show();
					setResult(BoiteDeMessage.RESULT_CODE_ECRITURE_OK);
					finish();
				}
				else {
					Toast.makeText(this, "Votre email n'a pas pus être envoyé.", Toast.LENGTH_SHORT).show();
					setResult(BoiteDeMessage.RESULT_CODE_ECRITURE_KO);
					finish();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

}
