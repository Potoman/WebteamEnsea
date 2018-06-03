package com.potoman.webteam.anniversaire;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.potoman.tools.CallService;
import com.potoman.tools.L;
import com.potoman.tools.UrlService;
import com.potoman.webteam.R;
import com.potoman.webteam.TempData;
import com.potoman.webteam.constant.Webteam;
import com.potoman.webteam.eleve.ContactWebteam;
import com.potoman.webteam.eleve.FicheEleve;
import com.potoman.webteam.exception.ExceptionService;
import com.potoman.webteam.trombinoscope.TrombiResultAdapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Anniversaire extends Activity implements OnItemClickListener, OnTouchListener {

	private ListView myList;
	private TextView tvDateCurrently;
	private LinearLayout llAnniversaire;
	private ProgressDialog myProgressDialog;

	private final Handler uiThreadCallback = new Handler();

	private TrombiResultAdapter adapter = null;
	private final TmpAnniversaire mToday = new TmpAnniversaire();

	private final List<ContactWebteam> maListeDeProfil = new ArrayList<ContactWebteam>();
	
	//For changing date :
	private Calendar mCalendar;

    static final int DATE_DIALOG_ID = 0;

	private class TmpAnniversaire {
		public Calendar mCalendar = Calendar.getInstance();

		public void set(TmpAnniversaire ta) {
			mCalendar = ta.mCalendar;
		}
	}

	public static final int[] lagMonth = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    
    private float downXValue; //Used for grabbing
	
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                    
					mCalendar.set(year, monthOfYear, dayOfMonth);

                    getAnniversaire();
                }
            };
            
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this,
                        mDateSetListener,
                        mCalendar.get(Calendar.YEAR),
						mCalendar.get(Calendar.MONTH),
						mCalendar.get(Calendar.DAY_OF_MONTH));
        }
        return null;
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.anniversaire);
	    this.setTitle("Webteam > Anniversaire");
		registerForContextMenu((ListView)findViewById(R.id.lvAnniversaire));
		
		tvDateCurrently = (TextView)findViewById(R.id.tvDateCurrently);
		llAnniversaire = (LinearLayout)findViewById(R.id.llAnniversaire);
		llAnniversaire.setOnTouchListener(this);
		
	    adapter = new TrombiResultAdapter(getApplicationContext(), maListeDeProfil);
		
		myList = (ListView) findViewById(R.id.lvAnniversaire);
	    myList.setAdapter(adapter);
	    //myList.setOnTouchListener(this);
	    myList.setOnItemClickListener(this);
		adapter.notifyDataSetChanged();
	    
	    L.v("Anniversaire", "onCreate");

	    getBackInfo();

		getAnniversaire();
	}

	private void getBackInfo() {
		TmpAnniversaire tn = getLastNonConfigurationInstance();
		mToday.set(tn);
	}

	private TmpAnniversaire getLastNonConfigurationInstance() {
    	return (TmpAnniversaire) super.getLastNonConfigurationInstance();
	}
	
    @Override
    public Object onRetainNonConfigurationInstance() {
        return mToday;
    }
	
	public void getAnniversaire() {
		L.v("Anniversaire", "getAnniversaire");
		List<String> nameData = new ArrayList<String>();
		nameData.add("demain");
		List<String> valeurData = new ArrayList<String>();
		valeurData.add("" + computeLag());
		if (mToday == null)
			threadJSON(UrlService.createUrlAnniversaire(PreferenceManager.getDefaultSharedPreferences(this).getString(Webteam.PSEUDO, ""),
							PreferenceManager.getDefaultSharedPreferences(this).getString(Webteam.PASSWORD, "")),
					getResources().getStringArray(R.array.waitingAnniversaire)[0],
					Webteam.getPhraseDAmbiance(),
					nameData,
					valeurData);
		else {
			mCalendar = mToday.mCalendar;
			resultatAnniversaire();
		}
    }

    private int computeLag() {
    	
//		final Calendar c = Calendar.getInstance();
//		//int tempYear = c.get(Calendar.YEAR);
//		int tempMonth = c.get(Calendar.MONTH);
//		int tempDay = c.get(Calendar.DAY_OF_MONTH);

//		mLag = 0;
//		if (monthOfYear < tempMonth) {
//			for (int i = monthOfYear; i < tempMonth; i++) {
//				mLag -= lagMonth[i];
//			}
//		}
//		else {
//			for (int i = tempMonth; i < monthOfYear; i++) {
//				mLag += lagMonth[i];
//			}
//		}

//		L.v("Anniversaire", "mLag (after month) : " + mLag);
//		mLag -= tempDay;
//		mLag += dayOfMonth;
//		L.v("Anniversaire", "mLag : " + mLag);
		return 0;
	}

	private void threadJSON(final String url, String titleProgressDialog, String messageProgressDialog, final List<String> nameData, final List<String> valeurData) {//JSONObject en retour normalement.
		L.v("Anniversaire", "threadJSON");
		myProgressDialog = ProgressDialog.show(Anniversaire.this, titleProgressDialog, messageProgressDialog, true, true, new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
			}
		});
       	new Thread() {
       		@Override
       		public void run() {
       			try {
					JSONObject responseJSON = CallService.getJsonPost(Anniversaire.this, url, nameData, valeurData);
					maListeDeProfil.clear();
					maListeDeProfil.addAll(parse(responseJSON));
				} catch (final ExceptionService e) {
					reponseJSON = null;
					uiThreadCallback.post(new Runnable() {public void run() {Toast.makeText(Anniversaire.this, e.toString(), Toast.LENGTH_SHORT).show();}});
					e.printStackTrace();
				}
       			L.v("Anniversaire", "callService");
				uiThreadCallback.post(new Runnable() {public void run() {resultatAnniversaire();}});
	       	    myProgressDialog.dismiss();
       	    }
       	}.start();
	}

	private static final String CONTENU = "contenu";
	private static final String ERREUR = "erreur";

	private static List<ContactWebteam> parse(JSONObject response) throws JSONException {
		erreur = (Integer) reponseJSON.getInt("erreur");
		if (erreur == 0) {
			// No anniversaire
			return new ArrayList<>();
		}
		else {
			return ContactWebteam.parse(json.getJSONArray(CONTENU));
		}
	}

	void resultatAnniversaire() {
		L.v("Anniversaire", "resultatAnniversaire");
		if (reponseJSON == null) {
			Toast.makeText(this, "Vous n'êtes pas connecté à internet... :S", Toast.LENGTH_LONG).show();
			finish();
		}
		else {
			try {
				maListeDeProfil.clear();
				maListeDeProfil.addAll(parse(reponseJSON));
	    		afficherAnniversaire();
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    	if (erreur == 1){
		    	remplirAnniversaire();
	    		afficherAnniversaire();
	    	}
	    	else {
	    		Toast.makeText(this, "Il n'y a pas d'anniversaire ce jour là !", Toast.LENGTH_SHORT).show();
	    		//finish();
	    		maListeDeProfil.clear();
	    		adapter.synchNewProfil();
	    	}
	    }
	}

	public void remplirAnniversaire() {
		try {
			L.v("Anniversaire", "remplirAnniversaire");
			maListeDeProfil.clear();
			maListeDeProfil.addAll(ContactWebteam.parse(reponseJSON));
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			adapter.synchNewProfil();
		}
	}

	public void afficherAnniversaire() {
		tvDateCurrently.setText("Anniversaire pour le jour du : " + mDay + "/" + mMonth + "/" + mYear);
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	    adapter.switchOpenClose(position);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	  super.onCreateContextMenu(menu, v, menuInfo);
	  MenuInflater inflater = getMenuInflater();
	  inflater.inflate(R.menu.longclicktrombinoscope_, menu);
	  menu.add(0, adapter.lstProfil.get(adapter.lstInfoProfil.get((int)((AdapterContextMenuInfo)menuInfo).id).indexListProfil).getId(), 0, "Voir la fiche du profil");
	}

	@Override
    public boolean onContextItemSelected(MenuItem item)
    {
		if (item.getTitle().toString().equals("Annuler")) {
			//On fait rien.
		}
		else {
			Intent monIntent = new Intent(this,FicheEleve.class);
			Bundle variableDePassage = new Bundle();
			variableDePassage.putInt("idProfil", item.getItemId());
		    monIntent.putExtras(variableDePassage); 
			startActivity(monIntent);
		}
	return true;
    }
	
	//Touche "menu"
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.anniversaire_, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		List<TrombiResultAdapter.ItemInfo> tmpList = adapter.lstInfoProfil;
		switch (item.getItemId()) {
		case R.id.itemToutOuvrir:
			while (true) {
				int adapterSize = tmpList.size();
				for (int index = 0; index < adapterSize; index++) {
					TrombiResultAdapter.ItemInfo ii = tmpList.get(index);
					if (index == adapterSize - 1) {
						if (ii.typeInfo == TrombiResultAdapter.ITEM_NOM_PRENOM) {
							adapter.switchOpenClose(index);
							return super.onMenuItemSelected(featureId, item);
						}
						else {
							break;
						}
					}
					if (ii.typeInfo == TrombiResultAdapter.ITEM_NOM_PRENOM && tmpList.get(index + 1).typeInfo == TrombiResultAdapter.ITEM_NOM_PRENOM) {
						adapter.switchOpenClose(index);
						break;
					}
				}
			}
			break;
		case R.id.itemToutFermer:
			while (true) {
				int adapterSize = tmpList.size();
				for (int index = 0; index < adapterSize; index++) {
					TrombiResultAdapter.ItemInfo ii = tmpList.get(index);
					if (ii.typeInfo != TrombiResultAdapter.ITEM_NOM_PRENOM) {
						adapter.switchOpenClose(index);
						break;
					}
					if (index == adapterSize - 1 && ii.typeInfo == TrombiResultAdapter.ITEM_NOM_PRENOM) {
						return super.onMenuItemSelected(featureId, item);
					}
				}
			}
			break;
		case R.id.itemChangerLaDate:
			showDialog(DATE_DIALOG_ID);
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		L.v("Anniversaire", "onTouch");
		switch (arg1.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// store the X value when the user's finger was pressed down
			downXValue = arg1.getX();
			break;
		case MotionEvent.ACTION_UP:
			// Get the X value when the user released his/her finger
			float currentX = arg1.getX();

			// going backwards: pushing stuff to the right
			if (downXValue + Math.ceil(getWindow().getWindowManager().getDefaultDisplay().getWidth()/3) < currentX) {
				//Previous
				mLag--;
				mDay--;
				if (mDay == 0) {
					if (mMonth == 1) {
						mMonth = 12;
						mDay = 31;
					}
					else {
						mDay = lagMonth[mMonth - 2];
						mMonth--;
					}
					if (mDay == 29) {
						mYear = 2000;
					}
				}
					L.v("Anniversaire", "Previous : day " + mDay + ", month " + mMonth + ", mYear" + mYear);
				mToday = null;
				getAnniversaire();
			}

			// going forwards: pushing stuff to the left
			if (downXValue - Math.ceil(getWindow().getWindowManager().getDefaultDisplay().getWidth()/3) > currentX) {
				//Next
				mLag++;
				mDay++;
				if (mDay > lagMonth[mMonth - 1]) {
					mDay = 1;
					mMonth++;
					if (mMonth == 13)
						mMonth--;
				}
				if (mDay == 29 && mMonth == 2) {
					mYear = 2000;
				}

				mToday = null;
				getAnniversaire();
			}
			break;
		}
        // if you return false, these actions will not be recorded
        return true;
	}
}
