package com.potoman.webteam.trombinoscope;

import java.util.ArrayList;
import java.util.List;

import org.example.webteam.R;
import org.example.webteam.R.array;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;
import org.example.webteam.R.menu;
import org.json.JSONException;
import org.json.JSONObject;

import com.potoman.tools.CallService;
import com.potoman.tools.L;
import com.potoman.tools.UrlService;
import com.potoman.webteam.Preferences;
import com.potoman.webteam.constant.Webteam;
import com.potoman.webteam.exception.ExceptionService;
import com.potoman.webteam.loggin.Root;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class Trombinoscope extends Activity implements OnClickListener {
	JSONObject reponseJSON = null;
    private static int CODE_RETOUR = 1;
    private Button bRecherche;
    private EditText etNom = null;
    private EditText etPrenom = null;
    private EditText etPseudo = null;
    private EditText etEmail = null;
    private EditText etTelephone = null;
	private Spinner spClasse;
	private Spinner spSexe;
	private ArrayAdapter<CharSequence> aaSpClasse;
	private ArrayAdapter<CharSequence> aaSpSexe;
	private SharedPreferences preferences = null;
	ProgressDialog myProgressDialog;
	final Handler uiThreadCallback = new Handler();

	private static final int SEXE_TOUT = 0;
	private static final int SEXE_HOMME = 1;
	private static final int SEXE_FEMME = 2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.trombinoscope);
	    this.setTitle("Webteam > Trombinoscope");

		bRecherche = (Button)findViewById(R.id.bRecherche);
		bRecherche.setOnClickListener(this);
	    initSpinner();
	    initEditText();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		initVisibility();
	}
	

	public void initButton() {
		bRecherche = (Button)findViewById(R.id.bRecherche);
		bRecherche.setOnClickListener(this);
	}
	
	public void initSpinner() {
		spClasse = (Spinner)findViewById(R.id.spClasse); 
		aaSpClasse = ArrayAdapter.createFromResource(this, R.array.classes_array, android.R.layout.simple_spinner_item);
		aaSpClasse.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);        
		spClasse.setAdapter(aaSpClasse);
		spSexe = (Spinner)findViewById(R.id.spSexe); 
		aaSpSexe = ArrayAdapter.createFromResource(this, R.array.sexe_array, android.R.layout.simple_spinner_item);
		aaSpSexe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);        
		spSexe.setAdapter(aaSpSexe);
	}
	
	public void initEditText() {
		etNom = (EditText)findViewById(R.id.etNom);
		etPrenom = (EditText)findViewById(R.id.etPrenom);
		etPseudo = (EditText)findViewById(R.id.etPseudo);
		etEmail = (EditText)findViewById(R.id.etEmail);
		etTelephone = (EditText)findViewById(R.id.etTelephone);
	}
	
	public void initVisibility() {
		if (preferences.getBoolean("trombinom", true)) {
			etNom.setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.tvNom)).setVisibility(View.VISIBLE);
		}
		else {
			etNom.setVisibility(View.GONE);
			((TextView)findViewById(R.id.tvNom)).setVisibility(View.GONE);
		}
		if (preferences.getBoolean("trombiprenom", true)) {
			etPrenom.setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.tvPrenom)).setVisibility(View.VISIBLE);
		}
		else {
			etPrenom.setVisibility(View.GONE);
			((TextView)findViewById(R.id.tvPrenom)).setVisibility(View.GONE);
		}
		if (preferences.getBoolean("trombipseudo", false)) {
			etPseudo.setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.tvPseudo)).setVisibility(View.VISIBLE);
		}
		else {
			etPseudo.setVisibility(View.GONE);
			((TextView)findViewById(R.id.tvPseudo)).setVisibility(View.GONE);
		}
		if (preferences.getBoolean("trombiemail", false)) {
			etEmail.setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.tvEmail)).setVisibility(View.VISIBLE);
		}
		else {
			etEmail.setVisibility(View.GONE);
			((TextView)findViewById(R.id.tvEmail)).setVisibility(View.GONE);
		}
		if (preferences.getBoolean("trombitelephone", false)) {
			etTelephone.setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.tvTelephone)).setVisibility(View.VISIBLE);
		}
		else {
			etTelephone.setVisibility(View.GONE);
			((TextView)findViewById(R.id.tvTelephone)).setVisibility(View.GONE);
		}
		if (preferences.getBoolean("trombiclasse", true)) {
			spClasse.setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.tvClasse)).setVisibility(View.VISIBLE);
		}
		else {
			spClasse.setVisibility(View.GONE);
			((TextView)findViewById(R.id.tvClasse)).setVisibility(View.GONE);
		}
		if (preferences.getBoolean("trombisexe", true)) {
			spSexe.setVisibility(View.VISIBLE);
			((TextView)findViewById(R.id.tvSexe)).setVisibility(View.VISIBLE);
		}
		else {
			spSexe.setVisibility(View.GONE);
			((TextView)findViewById(R.id.tvSexe)).setVisibility(View.GONE);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.trombinoscope_search_, menu);
	return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent monIntent = null;
		switch (item.getItemId()) {
		case R.id.itemPreferences:
			monIntent = new Intent(this,Preferences.class);
			startActivityForResult(monIntent, CODE_RETOUR);
		return true;
		}
	return super.onMenuItemSelected(featureId, item);
	}

	public void onClick(View v) {
		if (v == bRecherche) {
			List<String> nameData = new ArrayList<String>();
			List<String> valeurData = new ArrayList<String>();
				
		if (preferences.getBoolean("trombinom", true)) {
			nameData.add("nom");
			valeurData.add(((TextView)findViewById(R.id.etNom)).getText().toString());
		}
		if (preferences.getBoolean("trombiprenom", true)) {
			nameData.add("prenom");
			//nameData.add("prenomBis");
			//valeurData.add(((TextView)findViewById(R.id.etPrenom)).getText().toString());
			valeurData.add(((TextView)findViewById(R.id.etPrenom)).getText().toString());
			/*try {
				valeurData.add(new String(((TextView)findViewById(R.id.etPrenom)).getText().toString().getBytes("ISO-8859-1"), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				L.v("Trombinoscope", "Erreur, encodage ISO->UTF-8");
				e.printStackTrace();
			}*/
			
			
			
		}
		if (preferences.getBoolean("trombipseudo", false)) {
			nameData.add("pseudo");
			valeurData.add(((TextView)findViewById(R.id.etPseudo)).getText().toString());
		}
		if (preferences.getBoolean("trombiemail", false)) {
			nameData.add("trombiemail");
			valeurData.add(((TextView)findViewById(R.id.etEmail)).getText().toString());
		}
		if (preferences.getBoolean("trombitelephone", false)) {
			nameData.add("telephone");
			valeurData.add(((TextView)findViewById(R.id.etTelephone)).getText().toString());
		}
		if (preferences.getBoolean("trombiclasse", true)) {
			nameData.add("classe");
			valeurData.add(((Spinner)findViewById(R.id.spClasse)).getSelectedItemPosition() + "");
		}
		if (preferences.getBoolean("sexe", true)) {
			switch (((Spinner)findViewById(R.id.spSexe)).getSelectedItemPosition()) {
			case SEXE_TOUT:
				break;
			case SEXE_HOMME:
				nameData.add("sexe");
				valeurData.add("homme");
				break;
			case SEXE_FEMME:
				nameData.add("sexe");
				valeurData.add("femme");
				break;
			}
		}
		//Toast.makeText(this, "Essai : " + ((TextView)findViewById(R.id.tvTelephone)).getText().toString() + " mlkmlkmlk : " + ((Spinner)findViewById(R.id.spClasse)).getSelectedItemPosition(), Toast.LENGTH_LONG).show();
		nameData.add("action");
		valeurData.add("recherche");
		threadJSON(UrlService.createUrlTrombinoscope(
				PreferenceManager.getDefaultSharedPreferences(this).getString(Webteam.PSEUDO, ""), 
				PreferenceManager.getDefaultSharedPreferences(this).getString(Webteam.PASSWORD, "")), 
				getResources().getStringArray(R.array.waitingTrombinoscope)[0], Webteam.getPhraseDAmbiance(), nameData, valeurData);
		}
	}
	
	public void threadJSON(final String url, String titleProgressDialog, String messageProgressDialog, final List<String> nameData, final List<String> valeurData) {//JSONObject en retour normalement.
		myProgressDialog = ProgressDialog.show(Trombinoscope.this, titleProgressDialog, messageProgressDialog, true, true, new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
			}
		});
		final Runnable runInUIThread = new Runnable() {
			public void run() {
				resultatTrombinoscope();
				}
		};
       	new Thread() {
       		@Override public void run() {
       			try {
					reponseJSON = CallService.getJsonPost(Trombinoscope.this, url, nameData, valeurData);
				} catch (final ExceptionService e) {
					reponseJSON = null;
					uiThreadCallback.post(new Runnable() {public void run() {Toast.makeText(Trombinoscope.this, e.toString(), Toast.LENGTH_SHORT).show();}});
					e.printStackTrace();
				}       					
				L.v("Trombinoscope", "Pass !");
       			uiThreadCallback.post(new Runnable() {public void run() {resultatTrombinoscope();}});
	       	    myProgressDialog.dismiss();
       	    }
       	}.start();
	}
	
	void resultatTrombinoscope() {
		if (reponseJSON != null) {
			int erreur = 0;
			try {
				erreur = (Integer) reponseJSON.getInt("erreur");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (erreur == 1) {
				Intent monIntent = new Intent(this,TrombiResult.class);
				Bundle variableDePassage = new Bundle();
				variableDePassage.putCharSequence("resultatRecherche", (CharSequence)(reponseJSON.toString()));
				monIntent.putExtras(variableDePassage); 
				startActivity(monIntent);
			}
			else
				if (erreur == 41)
					Toast.makeText(this, "Aucun résultat pour cette recherche.", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(this, "Impossible d'éxecuter cette recherche.", Toast.LENGTH_SHORT).show();
		}
	}
	
}

	
	