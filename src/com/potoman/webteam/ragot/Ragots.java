package com.potoman.webteam.ragot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.example.webteam.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.potoman.tools.CallService;
import com.potoman.tools.IObserver;
import com.potoman.tools.L;
import com.potoman.tools.UrlService;
import com.potoman.webteam.TempData;
import com.potoman.webteam.bdd.BddRagotManager;
import com.potoman.webteam.bdd.BddUriManager;
import com.potoman.webteam.constant.Webteam;
import com.potoman.webteam.eleve.FicheEleve;
import com.potoman.webteam.exception.ExceptionService;
import com.potoman.webteam.task.ATGetRagot;
import com.potoman.webteam.task.IWorkFinishOfAsyncTask;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class Ragots extends RagotManager implements OnFocusChangeListener, OnClickListener, IObserver {
	JSONObject reponseJSON = null;
	EditText ragotAPoster;
	Button envoieRagot;
	ProgressDialog myProgressDialog;
	final Handler uiThreadCallback = new Handler();
	
	Object data = null;
	boolean alreadyDisplayWarningInPost = false;
	
	ATGetRagot atGetRagot = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //requestWindowFeature(Window.FEATURE_PROGRESS);
	    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	    setContentView(R.layout.ragots);
	    
	    this.setTitle("Webteam > Ragots");
	    
	    L.v("Ragots", "onCreate");
	    
		registerForContextMenu((ListView)findViewById(R.id.lvRagots));
		ragotAPoster = (EditText)findViewById(R.id.etRagot);
		ragotAPoster.setOnFocusChangeListener(this);
		envoieRagot = (Button)findViewById(R.id.bRagot);
		envoieRagot.setOnClickListener(this);
		
		cible = Webteam.maListeDeRagot;
		ragotBdd = new BddRagotManager(this);
	    ragotBdd.setCible(cible);
		ragotBdd.open();

		uriBdd = new BddUriManager(this);
		uriBdd.open();
		
		if (cible == null) {
			cible = new ArrayList<Ragot>();
		}	

		
		data = getLastNonConfigurationInstance();
		if (data != null) {
			ragotAPoster.setText(((TempData) data).ajoutRagot);
			alreadyDisplayWarningInPost = ((TempData) data).alreadyDisplayWarningInPost;
		}
		getRagots(false);
		if (cible.size() != 0) {
			afficherRagots();
		}
		else {
			remplirListRagot();
		}
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	public boolean isPortrait() {
		if (getWindow().getWindowManager().getDefaultDisplay().getHeight() > getWindow().getWindowManager().getDefaultDisplay().getWidth())
			return true;
		else
			return false;
	}
	
	public void afficherRagots() {
		//L.v("Ragots", "Top");
		myList = (ListView)findViewById(R.id.lvRagots);
	    adapter = new RagotAdapter(getApplicationContext(), cible, this, isPortrait());//, maCheckBox);
	    myList.setAdapter(adapter);
	    myList.setOnItemClickListener(this);
	}
	
	public void remplirBddRagots(int redirection) {
		//L.v("Ragots", "remplirBddRagots");
		JSONArray contenuJSON = null;
		for (int i = 0; i < cible.size(); i++) {
			cible.get(i).setNews("");
		}
		try {

			contenuJSON = reponseJSON.getJSONArray("contenu");

			int valueOfLastHistory = 0;
			Ragot dernierRagot = null;
			dernierRagot = ragotBdd.getLastRagot();

			boolean flagSynchro;
			if (dernierRagot != null) {
				valueOfLastHistory = dernierRagot.getHistory() + 1;
				//L.v("Ragots", "remplir ragot 1, valueOfLastHistory : " + valueOfLastHistory);
				flagSynchro = false;
			}
			else {
				//L.v("Ragots", "Premi�re ouverture...");
				flagSynchro = true;
			}
			
			//Parcourir le JSON dans l'autre sens...
			int index = 0;
			int indexWrong = -1;
			while (!contenuJSON.isNull(index)) {
				if (contenuJSON.getJSONObject(index).getInt("id_posteur") == 0)
				{
					if (!alreadyDisplayWarningInPost) {
						Toast.makeText(this, "Impossible d'ajouter un ragot pour le moment. Veuillez patientez... Owned \\o/", Toast.LENGTH_LONG).show();
						alreadyDisplayWarningInPost = true;
						//L.v("Ragots", "TAOST ! AlreadyDisplayWarningInPost = true");
					}
					indexWrong = index;
				}
				index++;
			}
				
			int countInsert = 0;
			Ragot monRagot = null;
			for (int i = index - 1; i >= 0; i--) {
				if (i != indexWrong) {
					//L.v("Ragots", "remplir ragot 3, indexDec : " + i);
					if (flagSynchro) {
						monRagot = new Ragot(contenuJSON.getJSONObject(i).getString("ragot"),
								contenuJSON.getJSONObject(i).getInt("id_posteur"),
								contenuJSON.getJSONObject(i).getString("pseudo"),
								contenuJSON.getJSONObject(i).getLong("time"),
								valueOfLastHistory,
								0, 0,
								contenuJSON.getJSONObject(i).getString("new"));
						ragotBdd.insertRagot(monRagot);
						cible.add(0, monRagot);
						countInsert++;
					}
					if (dernierRagot != null) {
						if (contenuJSON.getJSONObject(i).getInt("id_posteur") == dernierRagot.getIdPseudo() &&
							contenuJSON.getJSONObject(i).getLong("time") == dernierRagot.getDate()) {
							//L.v("Ragots", "SYNCHRONISATION !!!");
							flagSynchro = true;
						}
					}
				}
			}
			
			//On n'as pas réussis à se synchroniser, il faut donc ajouter tout le flux JSON en rajoutant un ragot sp�cial s�parateur dans la BDD :
			if (!flagSynchro) {
				monRagot = new Ragot();
				ragotBdd.insertRagot(monRagot);
				cible.add(0, monRagot);
				for (int i = index - 1; i >= 0; i--) {
					if (i != indexWrong) {
						monRagot = new Ragot(contenuJSON.getJSONObject(i).getString("ragot"),
								contenuJSON.getJSONObject(i).getInt("id_posteur"),
								contenuJSON.getJSONObject(i).getString("pseudo"),
								contenuJSON.getJSONObject(i).getLong("time"),
								valueOfLastHistory,
								0, 0,
								contenuJSON.getJSONObject(i).getString("new"));
						ragotBdd.insertRagot(monRagot);
						cible.add(0, monRagot);
						countInsert++;
					}
				}
			}
			
			if (redirection == 0)
				if (countInsert == 0)
					Toast.makeText(this, "Pas de nouveau ragot... :'(", Toast.LENGTH_LONG).show();
				else
					if (countInsert == 1)
						Toast.makeText(this, "" + countInsert + " nouveau ragot ! :)", Toast.LENGTH_LONG).show();
					else
						Toast.makeText(this, "" + countInsert + " nouveaux ragots !!! =)", Toast.LENGTH_LONG).show();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		actualiseJustDate();
		if (cible.size() != 0)
			afficherRagots();
	}
	
	
	public void getRagots(boolean force) {
		if (data == null || force) {
			if (ragotBdd.getLastRagot() != null) {
				if (cible.size() == 0)
				remplirListRagot();
			}
			
			if (ragotBdd.getLastRagot() == null) {
				myProgressDialog = ProgressDialog.show(Ragots.this, getResources().getStringArray(R.array.waitingLoadRagotIfBddEmpty)[0], Webteam.getPhraseDAmbiance(), true, true, new OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						finish();
					}
				});
			}
			else {
				setProgressBarIndeterminateVisibility(true);
			}
			
			atGetRagot = new ATGetRagot(this);
			atGetRagot.execute(getApplicationContext());
			//threadJSON(0, UrlService.createUrlRagot(), , , null, null);
		}
		else {
			setProgressBarIndeterminateVisibility(false);
			reponseJSON = ((TempData)data).tempReponseJSON;
		}
    }
	
	public void threadJSON(final int redirection, final String url, String titleProgressDialog, String messageProgressDialog, final List<String> nameData, final List<String> valeurData) {//JSONObject en retour normalement.
		if (ragotBdd.getLastRagot() == null) {
			myProgressDialog = ProgressDialog.show(Ragots.this, titleProgressDialog, messageProgressDialog, true, true, new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			});
		}
		else {
			setProgressBarIndeterminateVisibility(true);
		}
		
		
		
		
       	new Thread() {
       		@Override public void run() {
   				if (nameData == null) {
					try {
						reponseJSON = CallService.getJsonGeth(Ragots.this, url);
					} catch (final ExceptionService e) {
						reponseJSON = null;
						uiThreadCallback.post(new Runnable() {public void run() {Toast.makeText(Ragots.this, e.toString(), Toast.LENGTH_SHORT).show();}});
						e.printStackTrace();
					}
   				}
				else {
   					try {
						reponseJSON = CallService.getJsonPost(Ragots.this, url, nameData, valeurData);
					} catch (final ExceptionService e) {
						reponseJSON = null;
						uiThreadCallback.post(new Runnable() {public void run() {Toast.makeText(Ragots.this, e.toString(), Toast.LENGTH_SHORT).show();}});
						e.printStackTrace();
					}
				}
   				//L.v("Ragots", "After getJsonGeth");
				uiThreadCallback.post(new Runnable() {public void run() {resultatRagot(redirection);}});
	       	    if (myProgressDialog != null) {
	       	    	myProgressDialog.dismiss();
	       	    }
       	    }
       	}.start();
	}
	
	void resultatRagot(int redirection) {
	    setProgressBarIndeterminateVisibility(false);
	    if (reponseJSON != null) {
			int erreur = 0;
			try {
				erreur = (Integer) reponseJSON.getInt("erreur");
			} catch (JSONException e) {
				e.printStackTrace();
			}
	    	switch (redirection) {
	    	case 0:
	    		if (erreur == 1){
		    	remplirBddRagots(redirection);
	    		}
	    		else {
	    		Toast.makeText(this, "Impossible de charger les ragots.", Toast.LENGTH_SHORT).show();
	    		finish();
	    		}
			break;
	    	case 1:
	    		if (erreur == 1){
	        	remplirBddRagots(redirection);
	    		}
	    		else {
	    		Toast.makeText(this, "Impossible d'ajouter un ragot.", Toast.LENGTH_SHORT).show();
	    		}
	    	ragotAPoster.setText("IPQ...");
	   		break;
	    	}
	    }
	}
	
    @Override
    public Object onRetainNonConfigurationInstance() {
        TempData temp = new TempData();
        temp.ajoutRagot = ragotAPoster.getText().toString();
        temp.tempReponseJSON = reponseJSON;
        temp.alreadyDisplayWarningInPost = alreadyDisplayWarningInPost;
        return temp;
    }

	public void onNothingSelected(AdapterView<?> arg0) {
	}

	public void onClick(View v) {
		if (v == envoieRagot) {
			alreadyDisplayWarningInPost = false;
			List<String> nameData = new ArrayList<String>();
			nameData.add("action");
			nameData.add("ragot");
			List<String> valeurData = new ArrayList<String>();
			valeurData.add("poster");
			valeurData.add(ragotAPoster.getText().toString());
			threadJSON(1, UrlService.createUrlRagot(
					PreferenceManager.getDefaultSharedPreferences(this).getString(Webteam.PSEUDO, ""), 
					PreferenceManager.getDefaultSharedPreferences(this).getString(Webteam.PASSWORD, "")), 
					getResources().getStringArray(R.array.waitingAddRagot)[0], Webteam.getPhraseDAmbiance(), nameData, valeurData);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.ragots_, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemActualiser:
			getRagots(true);
		return true;
		}
	return super.onMenuItemSelected(featureId, item);
	}

	public void onFocusChange(View v, boolean hasFocus) {
		if (hasFocus) {
			((EditText) v).setText("");
			if (data != null) {
				((EditText) v).setText(((TempData)data).ajoutRagot);
			}
		}
		else {
			((EditText) v).setText("IPQ...");
		}
	}
	
	@Override
    public boolean onContextItemSelected(MenuItem item)
    {
		switch (item.getGroupId()) {
		case 0:
			if (CallService.isConnected(this)) {
				Intent monIntent = new Intent(this,FicheEleve.class);
				Bundle variableDePassage = new Bundle();
				variableDePassage.putInt("idProfil", item.getItemId());
			    monIntent.putExtras(variableDePassage);
				startActivity(monIntent);
			}
			else {
				Toast.makeText(this, getResources().getStringArray(R.array.exception)[ExceptionService.ID_ERROR_CONNECTED], Toast.LENGTH_SHORT).show();
			}
			break;
			//****************************************CENSURE********************************************
		case 1:
			ragotBdd.switchFavoris(item.getItemId());
			adapter.notifyDataSetChanged();
			break;
		case 2:
			if (item.getTitle().toString().startsWith("https://webteam.ensea.fr/profil.php?id=")) {
				showProfil(Integer.parseInt(item.getTitle().toString().substring(39)));
			}
			else {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getTitle().toString()));
				lienCurrentlySeen = item.getTitle().toString();
				startActivity(intent);
			}
			break;
		case 3:
			//Annuler
			break;
		}
	return true;
    }
	
	private void showProfil(int id) {
		if (CallService.isConnected(this)) {
			Intent monIntent = new Intent(this,FicheEleve.class);
			Bundle variableDePassage = new Bundle();
			variableDePassage.putInt("idProfil", id);
		    monIntent.putExtras(variableDePassage);
			startActivity(monIntent);
		}
		else {
			Toast.makeText(this, getResources().getStringArray(R.array.exception)[ExceptionService.ID_ERROR_CONNECTED], Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (atGetRagot != null) {
			atGetRagot.cancel(true);
		}
	}
	
	@Override
	public void update(Object observable, Object data) {
		if (myProgressDialog != null) {
   	    	myProgressDialog.dismiss();
		}
		else {
		    setProgressBarIndeterminateVisibility(false);
		}
		
		try {
			if (atGetRagot.get() == null) {
				Toast.makeText(this, "Impossible de charger les ragots.", Toast.LENGTH_SHORT);
				return;
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}
		
		
		if (observable == atGetRagot) {
			int countInsert = 0;
			int valueOfLastHistory = 0;
			Ragot dernierRagot = ragotBdd.getLastRagot();

			try {
				if (dernierRagot == null) {
					// On positione le flag de synchro à true comme si nous étions déjà synchronisé.
					for (int index = atGetRagot.get().size() - 1; index >= 0; index--) {
						ragotBdd.insertRagot(atGetRagot.get().get(index));
						countInsert++;
						Webteam.maListeDeRagot.add(0, atGetRagot.get().get(index));
					}
				}
				else {
					boolean flagSynchro = false;
					valueOfLastHistory = dernierRagot.getHistory() + 1;
					for (int index = atGetRagot.get().size() - 1; index >= 0; index--) {
						
						if (atGetRagot.get().get(atGetRagot.get().size() - 1).getDate() > dernierRagot.getDate()) {
							/* 
							 * Si la date est sup�rieur au dernier ragot, c'est que nous ne pouvons pas
							 * synchroniser nos ragots. Nous ajoutons donc tout dans la BDD.
							 */
							
							if (!flagSynchro) {
								Ragot ragotEmpty = new Ragot();
								ragotBdd.insertRagot(ragotEmpty);
								Webteam.maListeDeRagot.add(0, ragotEmpty);
								flagSynchro = true;
							}
							
							atGetRagot.get().get(index).setHistory(valueOfLastHistory);
							ragotBdd.insertRagot(atGetRagot.get().get(index));
							countInsert++;
							Webteam.maListeDeRagot.add(0, atGetRagot.get().get(index));
							
						}
						else {
							if (atGetRagot.get().get(index).getIdPseudo() == dernierRagot.getIdPseudo()
									&& atGetRagot.get().get(index).getDate() == dernierRagot.getDate()) {
								// On viens de synchroniser.
								L.i("", "synchro : " + atGetRagot.get().get(index).getRagot());
								flagSynchro = true;
								continue;
							}
							if (flagSynchro) {
								L.i("", "add in BDD");
								atGetRagot.get().get(index).setHistory(valueOfLastHistory);
								ragotBdd.insertRagot(atGetRagot.get().get(index));
								countInsert++;
								Webteam.maListeDeRagot.add(0, atGetRagot.get().get(index));
							}
						}
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			if (countInsert == 0) {
				Toast.makeText(this, "Pas de nouveau ragot... :'(", Toast.LENGTH_LONG).show();
			}
			else {
				if (countInsert == 1) {
					Toast.makeText(this, "" + countInsert + " nouveau ragot ! :)", Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(this, "" + countInsert + " nouveaux ragots !!! =)", Toast.LENGTH_LONG).show();
				}
				actualiseJustDate();
				if (Webteam.maListeDeRagot.size() != 0) {
					afficherRagots();
				}
			}
		}
	}
}



