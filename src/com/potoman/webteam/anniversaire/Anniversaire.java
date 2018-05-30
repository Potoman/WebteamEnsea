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
	JSONObject reponseJSON = null;
	ListView myList;
	TextView tvDateCurrently;
	LinearLayout llAnniversaire;
	
	TrombiResultAdapter adapter = null;			//J'utilise la m�me liste que le trombi.
	TmpAnniversaire mData = null;
	ProgressDialog myProgressDialog;
	final Handler uiThreadCallback = new Handler();
	static List<ContactWebteam> maListeDeProfil = new ArrayList<ContactWebteam>();
	private int mLag = 0;
	
	//For changing date :
	private int mYear;
    private int mMonth;
    private int mDay;

    static final int DATE_DIALOG_ID = 0;
    
   public  static final int[] lagMonth = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    
    private float downXValue; //Used for grabbing
	
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year, 
                                      int monthOfYear, int dayOfMonth) {
                	
                	final Calendar c = Calendar.getInstance();
                    //int tempYear = c.get(Calendar.YEAR);
                    int tempMonth = c.get(Calendar.MONTH);
                    int tempDay = c.get(Calendar.DAY_OF_MONTH);
                	
                	mLag = 0;
                	if (monthOfYear < tempMonth) {
                		for (int i = monthOfYear; i < tempMonth; i++) {
                			mLag -= lagMonth[i];
                		}
                	}
                	else {
                		for (int i = tempMonth; i < monthOfYear; i++) {
                			mLag += lagMonth[i];
                		}
                	}
                	
                	L.v("Anniversaire", "mLag (after month) : " + mLag);
                	mLag -= tempDay;
                	mLag += dayOfMonth;
                	L.v("Anniversaire", "mLag : " + mLag);
                    mYear = year;
                    mMonth = monthOfYear + 1;
                    mDay = dayOfMonth;
                    
                    mData = null;
                    getAnniversaire();
                }
            };
            
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DATE_DIALOG_ID:
            return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear, mMonth - 1, mDay);
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
		
		myList = (ListView)findViewById(R.id.lvAnniversaire);
	    myList.setAdapter(adapter);
	    //myList.setOnTouchListener(this);
	    myList.setOnItemClickListener(this);
		adapter.notifyDataSetChanged();
	    
	    L.v("Anniversaire", "onCreate");
        mData = getLastNonConfigurationInstance();
        if (mData == null) {
        	final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH) + 1;
            mDay = c.get(Calendar.DAY_OF_MONTH);
            //tvDateCurrently.setText(dateCurrently);
        }
		getAnniversaire();
	}

	private TmpAnniversaire getLastNonConfigurationInstance() {
    	return (TmpAnniversaire) super.getLastNonConfigurationInstance();
	}
	
    @Override
    public Object onRetainNonConfigurationInstance() {
        TempData temp = new TempData();
        temp.tempReponseJSON = reponseJSON;
        temp.lag = mLag;
        temp.year = mYear;
        temp.month = mMonth;
        temp.day = mDay;
        return temp;
    }
	
	public void getAnniversaire() {
		L.v("Anniversaire", "getAnniversaire");
		List<String> nameData = new ArrayList<String>();
		nameData.add("demain");
		List<String> valeurData = new ArrayList<String>();
		valeurData.add("" + mLag);
		if (mData == null)
			threadJSON(0, UrlService.createUrlAnniversaire(
					PreferenceManager.getDefaultSharedPreferences(this).getString(Webteam.PSEUDO, ""),
					PreferenceManager.getDefaultSharedPreferences(this).getString(Webteam.PASSWORD, "")
					)
					, getResources().getStringArray(R.array.waitingAnniversaire)[0], Webteam.getPhraseDAmbiance(), nameData, valeurData);
		else {
			reponseJSON = mData.tempReponseJSON;
			mLag = mData.lag;
			mYear = mData.year;
			mMonth = mData.month;
			mDay = mData.day;
			resultatAnniversaire();
		}
    }

    private class TmpAnniversaire {
		public int lag = 0;
		public int year = 0;
		public int month = 0;
		public int day = 0;
		public JSONObject tempReponseJSON = null;
	}
	
	public void threadJSON(final int redirection, final String url, String titleProgressDialog, String messageProgressDialog, final List<String> nameData, final List<String> valeurData) {//JSONObject en retour normalement.
		L.v("Anniversaire", "threadJSON");
		myProgressDialog = ProgressDialog.show(Anniversaire.this, titleProgressDialog, messageProgressDialog, true, true, new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
			}
		});
       	new Thread() {
       		@Override
       		public void run() {
       			try {
					reponseJSON = CallService.getJsonPost(Anniversaire.this, url, nameData, valeurData);
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
	
	void resultatAnniversaire() {
		L.v("Anniversaire", "resultatAnniversaire");
		if (reponseJSON == null) {
			Toast.makeText(this, "Vous n'êtes pas connecté à internet... :S", Toast.LENGTH_LONG).show();
			finish();
		}
		else {
			int erreur = 0;
			try {
				erreur = (Integer) reponseJSON.getInt("erreur");
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
		L.v("Anniversaire", "remplirAnniversaire");
		maListeDeProfil.clear();
		try {
			int index = 0;
			JSONArray arrayListeDesProfils = reponseJSON.getJSONArray(CONTENU);
				while (!arrayListeDesProfils.isNull(index)) {
					JSONObject jObj = arrayListeDesProfils.getJSONObject(index);
					maListeDeProfil.add(new ContactWebteam(jObj));
					index++;
				}
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		adapter.synchNewProfil();
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
		//Intent monIntent = null;
		switch (item.getItemId()) {
		case R.id.itemToutOuvrir:
			while (true) {
				int i;
				for (i = 0; i < adapter.lstInfoProfil.size(); i++) {
					if (i == adapter.lstInfoProfil.size() - 1) {
						//On est au bout...
						if (adapter.lstInfoProfil.get(i).typeInfo == TrombiResultAdapter.ITEM_NOM_PRENOM) {
							adapter.switchOpenClose(i);
							i = -1;
							break;
						}
						else
							break;
					}
					if (adapter.lstInfoProfil.get(i).typeInfo == TrombiResultAdapter.ITEM_NOM_PRENOM && adapter.lstInfoProfil.get(i + 1).typeInfo == TrombiResultAdapter.ITEM_NOM_PRENOM) {
						adapter.switchOpenClose(i);
						break;
					}
				}
				if (i == -1)
					break;
			}
			break;
		case R.id.itemToutFermer:
			while (true) {
				int i;
				for (i = 0; i < adapter.lstInfoProfil.size(); i++) {
					if (adapter.lstInfoProfil.get(i).typeInfo != TrombiResultAdapter.ITEM_NOM_PRENOM) {
						adapter.switchOpenClose(i);
						break;
					}
					if (i == adapter.lstInfoProfil.size() - 1 && adapter.lstInfoProfil.get(i).typeInfo == TrombiResultAdapter.ITEM_NOM_PRENOM) {
					i = -1;
					break;
					}
				}
				if (i == -1)
					break;
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
		switch (arg1.getAction())
        {		
            case MotionEvent.ACTION_DOWN:
            {
                // store the X value when the user's finger was pressed down
                downXValue = arg1.getX();
                break;
            }

            case MotionEvent.ACTION_UP:
            {
                // Get the X value when the user released his/her finger
                float currentX = arg1.getX();   

                // going backwards: pushing stuff to the right
                if (downXValue + Math.ceil(getWindow().getWindowManager().getDefaultDisplay().getWidth()/3) < currentX)
                {
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
                    mData = null;
                    getAnniversaire();
                }

                // going forwards: pushing stuff to the left
                if (downXValue - Math.ceil(getWindow().getWindowManager().getDefaultDisplay().getWidth()/3) > currentX)
                {
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

                    mData = null;
                    getAnniversaire();		
                }
                break;
            }
        }
        // if you return false, these actions will not be recorded
        return true;
	}
}
