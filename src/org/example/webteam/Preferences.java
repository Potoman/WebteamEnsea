package org.example.webteam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.example.webteam.caligula.Caligula;
import org.example.webteam.caligula.UrlCaligula;

import potoman.tools.CallService;
import potoman.tools.L;
import potoman.tools.ManipOnTime;
import potoman.tools.ObjectToDay;
import potoman.webteam.bdd.BddUrlCaligulaManager;
import potoman.webteam.constant.Webteam;
import potoman.webteam.exception.ExceptionResponseHttpNull;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.Toast;

public class Preferences extends PreferenceActivity implements OnPreferenceChangeListener, OnPreferenceClickListener {
		
	//private static final int FORCE = 1;
	private static final int NAME = 0;
	private static final int URL = 1;
	private static final int ID = 2;

	final Handler uiThreadCallback = new Handler();
	
	private BddUrlCaligulaManager urlCaligulaBdd = null;
	private DefaultHttpClient httpClient = null;
    ProgressDialog myProgressDialog;
	
	ListPreference maLPPhoto = null;
	ListPreference maLPCaligula = null;
	Preference maDateDuLundiDeLaSemaine1 = null;

	private int mYear = 0;
	private int mMonth = 0;
	private int mDay = 0;
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {         
            mYear = year;
            mMonth = monthOfYear - 1;
            mDay = dayOfMonth;
            final GregorianCalendar myGC = new GregorianCalendar(mYear, mMonth, mDay);
            int month = mMonth + 1;
            maDateDuLundiDeLaSemaine1.setSummary(ObjectToDay.displayDay(myGC.get(GregorianCalendar.DAY_OF_WEEK)) + " " + mDay + " " + ObjectToDay.displayMonth(month) + " " + mYear);
            Editor editor = maDateDuLundiDeLaSemaine1.getEditor();
            editor.putString("firstMonday", mDay + "/" + month + "/" + mYear);
            editor.commit();
        }
		
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
		maDateDuLundiDeLaSemaine1 = (Preference)findPreference("firstMonday");
		maDateDuLundiDeLaSemaine1.setOnPreferenceClickListener(this);
		
		final GregorianCalendar myGC = ManipOnTime.stringOfDateToCalendar(maDateDuLundiDeLaSemaine1.getSharedPreferences().getString("firstMonday", "1/1/2000"));
		mYear = myGC.get(GregorianCalendar.YEAR);
		mMonth = myGC.get(GregorianCalendar.MONTH);
		mDay = myGC.get(GregorianCalendar.DAY_OF_MONTH);
		int month = mMonth + 1;
		maDateDuLundiDeLaSemaine1.setSummary(ObjectToDay.displayDay(myGC.get(GregorianCalendar.DAY_OF_WEEK)) + " " + mDay + " " + ObjectToDay.displayMonth(month) + " " + mYear);
		
		maLPCaligula = (ListPreference)findPreference("listPref");
		maLPCaligula.setOnPreferenceChangeListener(this);
		
		maLPPhoto = (ListPreference)findPreference("listPrefQuality");
		maLPPhoto.setOnPreferenceChangeListener(this);
		
		L.v("Valeur de la liste au début", "Valeur de la liste au début : " + maLPPhoto.getValue());
		
		maLPPhoto.setSummary(getResources().getStringArray(R.array.photo_name)[Integer.parseInt(maLPPhoto.getValue())] + "%");
		if (Integer.parseInt(Build.VERSION.SDK) >= 11) {
			maLPPhoto.setSummary("" + getResources().getStringArray(R.array.photo_name)[Integer.parseInt(maLPPhoto.getValue())] + "%");
		}
		else {
			maLPPhoto.setSummary("" + getResources().getStringArray(R.array.photo_name)[Integer.parseInt(maLPPhoto.getValue())]);
		}
		urlCaligulaBdd = new BddUrlCaligulaManager(this);
		urlCaligulaBdd.open();
		L.v("Preferences", "mlkmlkmlk " + maLPCaligula.getValue());
		
		List<UrlCaligula> myList = urlCaligulaBdd.getAllUrlCaligula();
		if (myList.isEmpty()) {
			getUrlForCaligula();
		}
		else {
			UrlCaligula myUrlCaligula = urlCaligulaBdd.getUrlCaligula(Integer.parseInt(maLPCaligula.getValue()));
			if (myUrlCaligula == null) {
				maLPCaligula.setValue("" + myList.get(0).getId());
				maLPCaligula.setSummary(myList.get(0).getName());
			}
			else {
				maLPCaligula.setSummary(myUrlCaligula.getName());
			}
			maLPCaligula.setEntries(listToCharSequence(myList, NAME));
			maLPCaligula.setEntryValues(listToCharSequence(myList, ID));
		}
	}
	
	protected CharSequence[] listToCharSequence(List<UrlCaligula> myList, int type) {
		List<String> myListBetween = new ArrayList<String>();
		for (int i = 0; i < myList.size(); i++) {
			switch (type) {
			case NAME:
				myListBetween.add(myList.get(i).getName());	
				break;
			case URL:
				myListBetween.add(myList.get(i).getUrl());
				break;
			case ID:
				myListBetween.add("" + myList.get(i).getId());
				break;
			}
		}
		return myListBetween.toArray(new CharSequence[myListBetween.size()]);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		urlCaligulaBdd.close();
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		L.w("", "new Value = " + newValue);
		if (preference == maLPCaligula)
			maLPCaligula.setSummary(urlCaligulaBdd.getUrlCaligula(Integer.parseInt((String) newValue)).getName());
		if (preference == maLPPhoto) {
			L.e("", getResources().getStringArray(R.array.photo_name)[Integer.parseInt(maLPPhoto.getValue())]);
			if (Integer.parseInt(Build.VERSION.SDK) >= 11) {
				maLPPhoto.setSummary("" + getResources().getStringArray(R.array.photo_name)[Integer.parseInt((String) newValue)] + "%");
    		}
			else {
				maLPPhoto.setSummary("" + getResources().getStringArray(R.array.photo_name)[Integer.parseInt((String) newValue)]);
			}
		}
		return true;
	}
	
	public void getUrlForCaligula() {
		urlCaligulaBdd.clear();
		L.v("Preferences", "getUrlForCaligula");
		httpClient = new DefaultHttpClient();
		
		CallService.setParam(httpClient, false);
	    if (CallService.isConnected(this))
	    	go();
	    else {
	    	Toast.makeText(this, getResources().getStringArray(R.array.exception)[0], Toast.LENGTH_SHORT).show();
	    	finish();
	    }
	}

	private void go() {
		
		myProgressDialog = new ProgressDialog(Preferences.this);
    	myProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	myProgressDialog.setMax(5);
    	myProgressDialog.setTitle(getResources().getStringArray(R.array.waitingLoadUrlForCaligula)[0]);
		myProgressDialog.setMessage(Webteam.getPhraseDAmbiance());
    	myProgressDialog.setCancelable(true);
    	myProgressDialog.show();
		
    	new Thread() {
       		@Override public void run() {
       			L.v("Preferences", "run");
    			Caligula.connexion(httpClient, myProgressDialog);
       			L.v("Preferences", "connexion ok");
    			try {
    				fillUrlForStagiaire(Caligula.askForSomething(Caligula.askForOpenStagiaire, httpClient), 0);
	    			myProgressDialog.incrementProgressBy(1);
	    			fillUrlForProfesseur(Caligula.askForSomething(Caligula.askForOpenInstructeur, httpClient), 0);
	    			myProgressDialog.incrementProgressBy(1);
    			}
    			catch (ExceptionResponseHttpNull e) {
    				L.e("Preferences", e.toString());
    				return;
    			}
    			L.v("Preferences", "site all url caligula = " + urlCaligulaBdd.getAllUrlCaligula().size());
    			maLPCaligula.setEntries(listToCharSequence(urlCaligulaBdd.getAllUrlCaligula(), NAME));
    			maLPCaligula.setEntryValues(listToCharSequence(urlCaligulaBdd.getAllUrlCaligula(), ID));
    			
    			if (urlCaligulaBdd.getUrlCaligula(Integer.parseInt(maLPCaligula.getValue())) == null) {
    				maLPCaligula.setDefaultValue(urlCaligulaBdd.getAllUrlCaligula().get(0).getId());
        			uiThreadCallback.post(new Runnable() {public void run() {maLPCaligula.setSummary(urlCaligulaBdd.getAllUrlCaligula().get(0).getName());}});
    			}
    			else {
    				maLPCaligula.setDefaultValue(urlCaligulaBdd.getUrlCaligula(Integer.parseInt(maLPCaligula.getValue())).getId());
        			uiThreadCallback.post(new Runnable() {public void run() {maLPCaligula.setSummary(urlCaligulaBdd.getAllUrlCaligula().get(Integer.parseInt(maLPCaligula.getValue())).getName());}});
    			}
    			
	       	    if (myProgressDialog != null) {
	       	    	myProgressDialog.dismiss();
	       	    }
       	    }
       	}.start();
	}
	
	private boolean fillUrlForStagiaire(HttpResponse maResponse, int numLineAlreadyRead) throws ExceptionResponseHttpNull {
		if (maResponse == null)		
			throw new ExceptionResponseHttpNull("Preferences : fillUrlForStagiaire");
		UrlCaligula urlCaligula =  new UrlCaligula();
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(maResponse.getEntity().getContent(), "iso-8859-1"), 8);
			
			String ligne;
			int numLine = 0;
			boolean lock = false;
			while((ligne = bufferedReader.readLine()) != null) {
//				L.v("Caligula.stagiaire", ligne);
				if (ligne.indexOf("'trainee'") != -1) {
//					L.v("Caligula.stagiaire.check", "LOCK");
					lock = true;
				}
				if (lock) {
					if (numLine >= numLineAlreadyRead) {
						numLineAlreadyRead++;
//						L.v("Caligula.stagiaire.check", "NUM : " + numLineAlreadyRead + " : " + ligne);
						if (ligne.indexOf("treebranch") != -1) {
//							L.v("Caligula.stagiaire.check", "APPEL : " + numLineAlreadyRead);
							fillUrlForStagiaire(Caligula.askForSomething("http://caligula.ensea.fr/ade/standard/gui/tree.jsp?branchId=" + Integer.parseInt(ligne.substring(ligne.indexOf("checkBranch") + 12, ligne.indexOf(", '"))) + "&expand=false&forceLoad=false&reload=false&scroll=0", httpClient), numLineAlreadyRead);
							return true;
//							if (add)
//								break;
						}
						if (ligne.indexOf("treeitem") != -1) {
							//mapUrlStagiaire.put("http://caligula.ensea.fr/ade/standard/gui/tree.jsp?selectId=" +  Integer.parseInt(ligne.substring(ligne.indexOf("check") + 6, ligne.indexOf(", '"))) + "&reset=true&forceLoad=false&scroll=0", ligne.substring(ligne.indexOf("');") + 5, ligne.indexOf("</a>")));
							
							urlCaligula.setName(ligne.substring(ligne.indexOf("');") + 5, ligne.indexOf("</a>")));
							urlCaligula.setUrl("http://caligula.ensea.fr/ade/standard/gui/tree.jsp?selectId=" +  Integer.parseInt(ligne.substring(ligne.indexOf("check") + 6, ligne.indexOf(", '"))) + "&reset=true&forceLoad=false&scroll=0");
							urlCaligula.setWhat(UrlCaligula.ELEVE);
							
							urlCaligulaBdd.insertUrlCaligula(urlCaligula);
							
//							L.v("Caligula.stagiaire.check", "ADD : http://caligula.ensea.fr/ade/standard/gui/tree.jsp?selectId=" +  Integer.parseInt(ligne.substring(ligne.indexOf("check") + 6, ligne.indexOf(", '"))) + "&reset=true&forceLoad=false&scroll=0");
							//add = true;
						}
						if (ligne.indexOf("'instructor'") != -1) {
							return true;
						}
					}
					numLine++;
				}
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean fillUrlForProfesseur(HttpResponse maResponse, int numLineAlreadyRead) throws ExceptionResponseHttpNull {
		if (maResponse == null)		
			throw new ExceptionResponseHttpNull("Preferences : fillUrlForStagiaire");
		UrlCaligula urlCaligula =  new UrlCaligula();
		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader(new InputStreamReader(maResponse.getEntity().getContent(), "iso-8859-1"), 8);
			
			String ligne;
			int numLine = 0;
			boolean lock = false;
			while((ligne = bufferedReader.readLine()) != null) {
				if (ligne.indexOf("'instructor'") != -1) {
					L.v("Caligula.stagiaire.check", "LOCK");
					lock = true;
				}
				if (lock) {
					if (numLine >= numLineAlreadyRead) {
						numLineAlreadyRead++;
						L.v("Caligula.stagiaire.check", "NUM : " + numLineAlreadyRead + " : " + ligne);
						if (ligne.indexOf("treebranch") != -1) {
							L.v("Caligula.stagiaire.check", "APPEL : " + numLineAlreadyRead);
							fillUrlForProfesseur(Caligula.askForSomething("http://caligula.ensea.fr/ade/standard/gui/tree.jsp?branchId=" + Integer.parseInt(ligne.substring(ligne.indexOf("checkBranch") + 12, ligne.indexOf(", '"))) + "&expand=false&forceLoad=false&reload=false&scroll=0", httpClient), numLineAlreadyRead);
							return true;
						}
						if (ligne.indexOf("treeitem") != -1) {
							//mapUrlProfesseur.put("http://caligula.ensea.fr/ade/standard/gui/tree.jsp?selectId=" +  Integer.parseInt(ligne.substring(ligne.indexOf("check") + 6, ligne.indexOf(", '"))) + "&reset=true&forceLoad=false&scroll=0", ligne.substring(ligne.indexOf("');") + 5, ligne.indexOf("</a>")));
							L.v("Caligula.stagiaire.check", "ADD : http://caligula.ensea.fr/ade/standard/gui/tree.jsp?selectId=" +  Integer.parseInt(ligne.substring(ligne.indexOf("check") + 6, ligne.indexOf(", '"))) + "&reset=true&forceLoad=false&scroll=0");
							
							urlCaligula.setName(ligne.substring(ligne.indexOf("');") + 5, ligne.indexOf("</a>")));
							urlCaligula.setUrl("http://caligula.ensea.fr/ade/standard/gui/tree.jsp?selectId=" +  Integer.parseInt(ligne.substring(ligne.indexOf("check") + 6, ligne.indexOf(", '"))) + "&reset=true&forceLoad=false&scroll=0");
							urlCaligula.setWhat(UrlCaligula.PROFESSEUR);
							
							urlCaligulaBdd.insertUrlCaligula(urlCaligula);
							
							//add = true;
						}
						if (ligne.indexOf("'room'") != -1) {
							return true;
						}
					}
					numLine++;
				}
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

    //Touche "menu"
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
  	MenuInflater inflater = getMenuInflater();
  	inflater.inflate(R.menu.preferences_, menu);
  	return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		//Intent monIntent = null;
		switch (item.getItemId()) {
		case R.id.menu_preferences_refresh:
			if (CallService.isConnected(this)) {
				urlCaligulaBdd.clear();
				getUrlForCaligula();
			}
			else {
				Toast.makeText(this, getResources().getStringArray(R.array.exception)[0], Toast.LENGTH_SHORT).show();
			}
			break;
		}
	return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onPreferenceClick(Preference preference) {
		//Utile pour la date du lundi de la semaine num�ro 1:
		showDialog(0); //On peut mettre un id si l'on souhaite avoir plusieurs Dialog... Il on n'en a pas besoin alors on met 0.
		return false;
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		return new DatePickerDialog(this,
        mDateSetListener,
        mYear, mMonth + 1, mDay);
	}
	
}

