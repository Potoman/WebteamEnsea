package org.example.webteam.boitemanager.boite.messageprive;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.example.task.ATGetBoiteDeMessagePrive;
import org.example.task.ATGetBoiteSquirrel;
import org.example.webteam.R;
import org.example.webteam.R.anim;
import org.example.webteam.R.array;
import org.example.webteam.R.id;
import org.example.webteam.R.menu;
import org.example.webteam.boitemanager.boite.BoiteDeMessage;
import org.example.webteam.boitemanager.message.Message;

import potoman.tools.L;
import potoman.webteam.bdd.BddMessagePriveManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Toast;

public class BoiteDeMessagePrive extends BoiteDeMessage {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setTitle("Webteam > Mes boîtes");
	    
	    messageBdd = new BddMessagePriveManager(this);
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
		if (((String) view.getTag()).equals("whichMessage")) {
			Intent monIntent = new Intent(this, BoiteDeMessagePriveLecture.class);
			Bundle variableDePassage = new Bundle();
			variableDePassage.putInt("idMessage", adapterMessage.getListMessage().get(position).getId());
			variableDePassage.putString("nomBoite", adapterMessage.getListMessage().get(position).getNomBoite());
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
			setTitle("Boite > " + view.getTag());
			adapterMessage.setListOfMessage(mapListMessageByBoite.get(view.getTag()));
			vfBoiteDeMessage.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
			vfBoiteDeMessage.showNext();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.boite_de_message_, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_synchroniser:
			launchSynchro();
			break;
		case R.id.item_ecrire_message_personnel:
			Intent monIntent = new Intent(this, BoiteDeMessagePriveEcriture.class);
			startActivityForResult(monIntent, REQUEST_CODE_ECRITURE_MESSAGE);
			break;
		}
	return super.onMenuItemSelected(featureId, item);
	}
	
	public void launchSynchro() {
		super.launchSynchro();
		getListeBoite();
	}
	
	@Override
	public void synchro(List<Message> maListeDeMessage, List<Message> maListeDeMessageOTA, String key) {
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
	    				messageBdd.deleteMessage(maListeDeMessage.get(i).getId(), key);	//Synchronisation des MPs par rapport é ce qui se trouve sur la webteam.
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
	public AsyncTask<Context, Void, Map<String, String>> getAsyncTaskListeBoite() {
		myGetListeBoite = null;
		super.update(null, null);
		return null;
	}

	@Override
	public AsyncTask<Context, Void, List<Message>> getAsyncTaskBoite(String nomBoite, String key) {
		L.v("BoiteDeMessagePrive", "mapLastMessage.get(" + nomBoite + ") = " + mapLastMessage.get(nomBoite));
		return new ATGetBoiteDeMessagePrive(this, nomBoite);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	if (vfBoiteDeMessage.getCurrentView() == lvMessageInBoiteDeMessage) {
	    		setTitle("Webteam > Mes boîtes");
	    	}
	    }
	    return super.onKeyDown(keyCode, event);
	}

}

