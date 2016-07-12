package com.potoman.webteam.boitemanager.boite.email;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import org.apache.http.impl.client.DefaultHttpClient;
import org.example.webteam.R;

import com.potoman.tools.CallService;
import com.potoman.tools.L;
import com.potoman.webteam.bdd.BddSquirrelManager;
import com.potoman.webteam.boitemanager.Squirrel;
import com.potoman.webteam.boitemanager.boite.BoiteDeMessage;
import com.potoman.webteam.boitemanager.message.Message;
import com.potoman.webteam.constant.Webteam;
import com.potoman.webteam.task.ATConnexionSquirrel;
import com.potoman.webteam.task.ATGetBoiteSquirrel;
import com.potoman.webteam.task.ATGetListeBoiteSquirrel;
import com.potoman.webteam.task.IWorkFinishOfAsyncTask;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class BoiteDeMessageEmail extends BoiteDeMessage implements OnItemClickListener {
	
	public DefaultHttpClient httpClient = null;

	private String pseudoSquirrel = "";
	private String passwordSquirrel = "";
	
	private AsyncTask<Context, Void, Integer> myConnexionSquirrel = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//System.setProperty("http.keepAlive", "false");
		
		setTitle("Email > Mes boîtes");

	    messageBdd = new BddSquirrelManager(this);
	    
		pseudoSquirrel = PreferenceManager.getDefaultSharedPreferences(this).getString(Webteam.PSEUDO_WEBMAIL, "");
		passwordSquirrel = PreferenceManager.getDefaultSharedPreferences(this).getString(Webteam.PASSWORD_WEBMAIL, "");
    		
		httpClient = new DefaultHttpClient();
		CallService.setParam(httpClient, false);

	    messageBdd.open();
	    messageBdd.getAllMessage(mapListMessageByBoite);
	    messageBdd.close();
	    
	    buildTypeBoite();
	    
	    for (Entry<String, List<Message>> entry : mapListMessageByBoite.entrySet()) {
	    	if (entry.getValue().size() > 0) {
	    		mapLastMessage.put(entry.getKey(), entry.getValue().get(0).getId());
	    	}
	    }
	    
	    launchSynchro();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		if (((String)view.getTag()).equals("whichMessage")) {
			Intent monIntent = new Intent(this, BoiteDeMessageEmailLecture.class);
			Bundle variableDePassage = new Bundle();
			variableDePassage.putInt("idMessage", adapterMessage.getListMessage().get(position).getId());
			variableDePassage.putString("nomBoite", adapterMessage.getListMessage().get(position).getNomBoite());
			variableDePassage.putString("urlBoite", mapKey.get(adapterMessage.getListMessage().get(position).getNomBoite()));
		    monIntent.putExtras(variableDePassage);
		    numeroItemDuMessageQueLOnLit = position;
			startActivityForResult(monIntent, REQUEST_CODE_LECTURE_MESSAGE);
		}
		else {
			if (mapListMessageByBoite.get((String) view.getTag()).size() == 0) {
				Toast.makeText(this, "Il n'y a aucun message dans cette boite !", Toast.LENGTH_SHORT).show();
				return;
			}
			buildBoite((String) view.getTag());
			setTitle("Email > " + view.getTag());
			adapterMessage.setListOfMessage(mapListMessageByBoite.get(view.getTag()));
			vfBoiteDeMessage.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
			vfBoiteDeMessage.showNext();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.boite_de_message_email_, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_synchroniser:
			for (String key : mapLastMessage.keySet()) {
				mapLastMessage.put(key, -1);
			}
			launchSynchro();
		return true;
		case R.id.item_get_last_email:
			launchSynchro();
		return true;
		case R.id.item_ecrire_message_email:
			Intent monIntent = new Intent(this, BoiteDeMessageEmailEcriture.class);
			startActivityForResult(monIntent, REQUEST_CODE_ECRITURE_MESSAGE);
			break;
		}
	return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	public AsyncTask<Context, Void, Map<String, String>> getAsyncTaskListeBoite() {
		return new ATGetListeBoiteSquirrel(this, httpClient);
	}
	
	public void launchSynchro() {
		super.launchSynchro();
		myConnexionSquirrel = new ATConnexionSquirrel(this, pseudoSquirrel, passwordSquirrel, httpClient, 0);
		myConnexionSquirrel.execute(getApplicationContext());
	}
	
	@Override
	public void synchro(final List<Message> maListeDeMessage, final List<Message> maListeDeMessageOTA, String key) {
		L.e("BoiteDeMessage", "synchronisation : " + key + " list = " + maListeDeMessage + " listOTA = " + maListeDeMessageOTA);
		if (maListeDeMessage != null && maListeDeMessageOTA != null) {
			L.e("BoiteDeMessage", "synchronisation : " + key);
			boolean delete = false;
			
			if (mapLastMessage.get(key) == -1) {
				delete = true;
			}
		    messageBdd.open();
			if (delete) {
	    		L.v("BoiteDeMessageEmail", "synchro : Message existant dans la BDD.");
		    	boolean exist = false;
	    		for (int i = 0; i < maListeDeMessage.size(); i++) {
	    			exist = false;
	    			for (int j = 0; j < maListeDeMessageOTA.size(); j++) {
	    				if (maListeDeMessageOTA.get(j).getId() == maListeDeMessage.get(i).getId()) {
	    					exist = true;
	    					break;
	    				}
	    			}
	    			if (!exist) {
	    				messageBdd.deleteMessage(maListeDeMessage.get(i).getId(), key);	//Synchronisation des emails par rapport à ce qui se trouve sur Squirrel.
	    			}
	    		}
			}
	    	for (int i = 0; i < maListeDeMessageOTA.size(); i++) {
	    		L.e("BoiteDeMessageEmail", "insertIfNotInBddElseUpdate");
	    		maListeDeMessageOTA.get(i).insertIfNotInBddElseUpdate(messageBdd);
	    	}
		    messageBdd.close();
	    	
	    	if (!delete) {
				int sizeOTA = maListeDeMessageOTA.size();
				for (int i = 0; i < maListeDeMessage.size(); i++) {
					maListeDeMessageOTA.add(i + sizeOTA, maListeDeMessage.get(i));
				}
				mapLastMessage.put(key, maListeDeMessageOTA.get(0).getId());
			}
	    	maListeDeMessage.clear();
	    	for (int i = 0; i < maListeDeMessageOTA.size(); i++) {
	    		maListeDeMessage.add(maListeDeMessageOTA.get(i));
	    	}
	    	//adapterMessage.setListOfMessage(maListeDeMessage);
			adapterMessage.notifyDataSetChanged();
			adapterBoiteDeMessage.notifyDataSetChanged();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		myConnexionSquirrel.cancel(true);
		if (myGetListeBoite != null) {
			myGetListeBoite.cancel(true);
		}
	}

	@Override
	public AsyncTask<Context, Void, List<Message>> getAsyncTaskBoite(String nomBoite, String key) {
		L.v("BoiteDeMessageEmail", "mapLastMessage.get(" + nomBoite + ") = " + mapLastMessage.get(nomBoite));
		return new ATGetBoiteSquirrel(this, httpClient, mapLastMessage.get(nomBoite), key, nomBoite);
	}

	
	@Override
	public void update(Object observable, Object data) {
		super.update(observable, data);
		if (observable == myConnexionSquirrel) {
			setProgress(2000); // On met la barre de chargement à 20%.
			try {
				Integer answer = myConnexionSquirrel.get();
				if (answer == Squirrel.RESULT_CONNEXION_OK) {
					getListeBoite();
				}
				else  {
					Toast.makeText(this, "Synchronisation impossible.", Toast.LENGTH_SHORT).show();
					setProgress(10000);
					progressBar = 0;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	if (vfBoiteDeMessage.getCurrentView() == lvMessageInBoiteDeMessage) {
	    		setTitle("Email > Mes boîtes");
	    	}
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
}
