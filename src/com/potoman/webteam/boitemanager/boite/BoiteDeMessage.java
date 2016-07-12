package com.potoman.webteam.boitemanager.boite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.http.protocol.RequestContent;
import org.example.webteam.R;

import com.potoman.tools.IObserver;
import com.potoman.tools.L;
import com.potoman.webteam.TempData;
import com.potoman.webteam.bdd.BddMessageManager;
import com.potoman.webteam.boitemanager.item.boite.BoiteDeMessageAdapter;
import com.potoman.webteam.boitemanager.item.boite.ItemBoiteDeMessage;
import com.potoman.webteam.boitemanager.item.message.MessageAdapter;
import com.potoman.webteam.boitemanager.message.Message;
import com.potoman.webteam.constant.Webteam;
import com.potoman.webteam.task.ATGetBoiteSquirrel;
import com.potoman.webteam.task.IWorkFinishOfAsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.AsyncTask.Status;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public abstract class BoiteDeMessage extends Activity implements OnCancelListener, OnItemClickListener, IObserver {

	protected int progressBar = 0;
	
	public final static int BOITE_DE_RECEPTION = 0;
	public final static int BOITE_D_ENVOIS = 1;
	
	public final static int REQUEST_CODE_LECTURE_MESSAGE = 0;
		public final static int RESULT_CODE_LECTURE_MESSAGE_CONTENU_NOT_LOAD = Message.CONTENU_NOT_LOAD;
		public final static int RESULT_CODE_LECTURE_MESSAGE_CONTENU_LOAD = Message.CONTENU_LOAD;
		public final static int RESULT_CODE_LECTURE_MESSAGE_DELETED = 2;
		public final static int RESULT_CODE_LECTURE_MESSAGE_CONTENU_NOT_LOAD_AND_ANSWER = 3;
		public final static int RESULT_CODE_LECTURE_MESSAGE_CONTENU_LOAD_AND_ANSWER = 4;
		public final static int RESULT_CODE_LECTURE_MESSAGE_DELETED_AND_ANSWER = 5;
	public final static int REQUEST_CODE_ECRITURE_MESSAGE = 1;
		public final static int RESULT_CODE_ECRITURE_OK = 0;
		public final static int RESULT_CODE_ECRITURE_KO = 1;
	
    protected ViewFlipper vfBoiteDeMessage = null;
	protected ListView lvTypeBoiteDeMessage = null;
	protected ListView lvMessageInBoiteDeMessage = null;
	
	protected BoiteDeMessageAdapter adapterBoiteDeMessage = null;
	protected MessageAdapter adapterMessage = null;
	
	protected List<ItemBoiteDeMessage> listBoiteDeMessage = null;
	
	protected Object data = null;
	protected ProgressDialog myProgressDialog;
	
	protected AsyncTask<Context, Void, Map<String, String>> myGetListeBoite = null;

	protected Map<String, Integer> mapLastMessage = new HashMap<String, Integer>();	// Key = nom de la boite.
	protected Map<String, String> mapKey = new HashMap<String, String>(); // Key = nom de la boite; Value = key for URL
	protected Map<String, AsyncTask<Context, Void, List<Message>>> mapATGetBoite = new HashMap<String, AsyncTask<Context, Void, List<Message>>>();	// Key = nom de la boite.
	protected Map<String, List<Message>> mapListMessageByBoite = new HashMap<String, List<Message>>();	// Key = nom de la boite.
	
	protected int numeroItemDuMessageQueLOnLit = 0;
	
	public BddMessageManager messageBdd;

    protected int countOfIncrement = 0;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	    requestWindowFeature(Window.FEATURE_PROGRESS);
		setProgressBarVisibility(true);
		setProgress(0);
	    
		setContentView(R.layout.boite_de_message);
		vfBoiteDeMessage = (ViewFlipper)findViewById(R.id.vf_boite_de_message);
		lvMessageInBoiteDeMessage = (ListView)findViewById(R.id.lv_message_in_boite);
		lvTypeBoiteDeMessage = (ListView)findViewById(R.id.lv_type_boite_de_message);

		listBoiteDeMessage = new ArrayList<ItemBoiteDeMessage>();
		
		adapterBoiteDeMessage = new BoiteDeMessageAdapter(getApplicationContext(), listBoiteDeMessage);
		adapterMessage = new MessageAdapter(getApplicationContext());
	}
	
	@Override
	public Object onRetainNonConfigurationInstance() {
		TempData temp = new TempData();
		temp.maListeDeBoiteDeMessage = listBoiteDeMessage;
		return temp;
	}
	
	// Entré : mapListMessageByBoite
	protected void buildTypeBoite() {
		Set<ItemBoiteDeMessage> setItemToDelete = new HashSet<ItemBoiteDeMessage>();
		for (ItemBoiteDeMessage item : listBoiteDeMessage) {
			if (!mapListMessageByBoite.containsKey(item.getNomBoite())) {
				setItemToDelete.add(item);
			}
		}
		for (ItemBoiteDeMessage item : setItemToDelete) {
			listBoiteDeMessage.remove(item);
			messageBdd.deleteBoite(item.getNomBoite());
		}
		for (Entry<String, List<Message>> entry : mapListMessageByBoite.entrySet()) {
			boolean found = false;
			for (ItemBoiteDeMessage item : listBoiteDeMessage) {
				if (item.getNomBoite().equals(entry.getKey())) {
					found = true;
				}
			}
			if (!found) {
				listBoiteDeMessage.add(new ItemBoiteDeMessage(entry.getKey(), entry.getValue()));
			}
		}
	    lvTypeBoiteDeMessage.setAdapter(adapterBoiteDeMessage);
	    lvTypeBoiteDeMessage.setOnItemClickListener(this);
	    adapterBoiteDeMessage.notifyDataSetChanged();
	}
	
	protected void buildBoite(String key) {
		adapterMessage.setListOfMessage(mapListMessageByBoite.get(key));
		
		lvMessageInBoiteDeMessage.setAdapter(adapterMessage);
		lvMessageInBoiteDeMessage.setOnItemClickListener(this);
	}
	
	/*
	 * A appeler pour chaque boite de message.
	 */
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		L.v("BoiteDeMessage", "onActivityResult requestCode = " + requestCode + ", resultCode = " + resultCode);
		//super.onActivityResult(requestCode, resultCode, data);
		L.v("", "");
		switch (requestCode) {
		case REQUEST_CODE_LECTURE_MESSAGE:
			L.v("BoiteDeMessage", "first switch : REQUEST_CODE_LECTURE_MESSAGE");
			switch (resultCode) {
			case RESULT_CODE_LECTURE_MESSAGE_CONTENU_LOAD:
				L.v("BoiteDeMessage", "second switch : RESULT_CODE_LECTURE_MESSAGE_CONTENU_LOAD");
				adapterMessage.getListMessage().get(numeroItemDuMessageQueLOnLit).setEtatBoite(Message.ETAT_LU);
				adapterMessage.getListMessage().get(numeroItemDuMessageQueLOnLit).setContenuLoad(Message.CONTENU_LOAD);
				adapterMessage.notifyDataSetChanged();
				adapterBoiteDeMessage.notifyDataSetChanged();
				break;
			case RESULT_CODE_LECTURE_MESSAGE_CONTENU_NOT_LOAD:
				L.v("BoiteDeMessage", "second switch : RESULT_CODE_LECTURE_MESSAGE_CONTENU_NOT_LOAD");
				//On ne fait rien !
				break;
			case RESULT_CODE_LECTURE_MESSAGE_DELETED:
				L.v("BoiteDeMessage", "second switch : RESULT_CODE_LECTURE_MESSAGE_DELETED");
				adapterMessage.getListMessage().remove(numeroItemDuMessageQueLOnLit);
				adapterMessage.notifyDataSetChanged();
				adapterBoiteDeMessage.notifyDataSetChanged();
				break;
			case RESULT_CODE_LECTURE_MESSAGE_CONTENU_LOAD_AND_ANSWER:
				launchSynchro();
				break;
			case RESULT_CODE_LECTURE_MESSAGE_CONTENU_NOT_LOAD_AND_ANSWER:
				launchSynchro();
				break;
			default:
				L.v("BoiteDeMessage", "second switch : DEFAULT");
				break;
			}
			break;
		case REQUEST_CODE_ECRITURE_MESSAGE:
			switch (resultCode) {
			case RESULT_CODE_ECRITURE_OK:
				launchSynchro();
				break;
			case RESULT_CODE_ECRITURE_KO:
				//On ne fait rien.
				break;
			}
			break;
		}
	}
	
	public void workFinish(int cible) {
		L.w("BoiteDeMessage", "WORK FINISH !!!");
		//this.incrementMyProgressDialog();
		switch (cible) {
		case IWorkFinishOfAsyncTask.AT_LISTE_BOITE:
			L.w("BoiteDeMessage", "WORK FINISH of List Boite");
			
			
			break;
		}
	}
	
	protected void getListeBoite() {
		if (myGetListeBoite != null) {
			if (myGetListeBoite.getStatus() == AsyncTask.Status.RUNNING) {
				Toast.makeText(this, "Une synchronisation est en cours...", Toast.LENGTH_SHORT).show();
			}
			else {
				myGetListeBoite = getAsyncTaskListeBoite();
				myGetListeBoite.execute(getApplicationContext());
			}
		}
		else {
			myGetListeBoite = getAsyncTaskListeBoite();
			if (myGetListeBoite != null) {
				myGetListeBoite.execute(getApplicationContext());
			}
		}
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		finish();
	}
	
	public abstract AsyncTask<Context, Void, Map<String, String>> getAsyncTaskListeBoite();
	public abstract AsyncTask<Context, Void, List<Message>> getAsyncTaskBoite(String key, String name);
	
	public abstract void synchro(List<Message> maListeDeMessage, List<Message> maListeDeMessageOTA, String key);
	
	public void launchSynchro() {
		setProgressBarIndeterminateVisibility(true);
	}
	
	@Override
	protected void onPause() {
		super.onPause();

		for (AsyncTask value : mapATGetBoite.values()) {
			value.cancel(true);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	if (vfBoiteDeMessage.getCurrentView() == lvMessageInBoiteDeMessage) {
	    		vfBoiteDeMessage.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
	    		vfBoiteDeMessage.showPrevious();
			    return false;
	    	}
	    	if (vfBoiteDeMessage.getCurrentView() == lvTypeBoiteDeMessage) {
			    return super.onKeyDown(keyCode, event);
	    	}
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public void update(Object observable, Object data) {
		if (mapATGetBoite.values().contains(observable)) {
			progressBar++;
			
			if (listBoiteDeMessage.size() == progressBar) {
				setProgress(10000);
				progressBar = 0;
			}
			else {
				setProgress(2000 + (8000 / listBoiteDeMessage.size()) * progressBar);
			}
			
			String key = (String) data;
			int countfinish = 0;
			for (AsyncTask<Context, Void, List<Message>> at : mapATGetBoite.values()) {
				if (at.getStatus() == Status.FINISHED) {
					countfinish++;
				}
			}
			setProgressBarIndeterminateVisibility(!(countfinish == mapATGetBoite.size() - 1));
			
			for (ItemBoiteDeMessage item : listBoiteDeMessage) {
				if (item.getNomBoite().equals(key)) {
					item.setItalic(false);
				}
			}
			adapterBoiteDeMessage.notifyDataSetChanged();
			
			L.e("BoiteDeMessage", "key = " + key);
			try {
				synchro(mapListMessageByBoite.get(key), mapATGetBoite.get(key).get(), key);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			
			getBoite(key);
		}
		else if (observable == myGetListeBoite || observable == null) {
			Map<String, String> result = null;
			
			if (myGetListeBoite == null) {
				result = new HashMap<String, String>();
				result.put(Webteam.BOITE_DE_RECEPTION, Webteam.BOITE_DE_RECEPTION);
				result.put(Webteam.BOITE_D_ENVOIS, Webteam.BOITE_D_ENVOIS);
			}
			else {
				try {
					result = myGetListeBoite.get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}
			if (result != null) {
				for (Entry<String, String> entry : result.entrySet()) {
					L.i("BoiteDeMessage", "Boite à garder : " + entry.getKey());
					mapKey.put(entry.getKey(), entry.getValue());
					mapLastMessage.put(entry.getKey(), -1);
					if (!mapListMessageByBoite.containsKey(entry.getKey())) {
						mapListMessageByBoite.put(entry.getKey(), new ArrayList<Message>());
					}
				}
				Set<String> setListBoiteToDelete = new HashSet<String>();
				for (String nomBoite : mapListMessageByBoite.keySet()) {
					if (!result.containsKey(nomBoite)) {
						L.i("BoiteDeMessage", "Boite à supprimer : " + nomBoite);
						setListBoiteToDelete.add(nomBoite);
					}
				}
				messageBdd.open();
				for (String nomBoite : setListBoiteToDelete) {
					mapKey.remove(nomBoite);
					mapLastMessage.remove(nomBoite);
					mapListMessageByBoite.remove(nomBoite);
					messageBdd.deleteBoite(nomBoite);
				}
				messageBdd.close();
			}
			buildTypeBoite();
			for (ItemBoiteDeMessage item : listBoiteDeMessage) {
				item.setItalic(true);
			}
			getBoite("");
		}
	}
	
	/**
	 * Cette fonction permet de gérer le téléchargement des boites.
	 * Si elle est appelé par une AsyncTask de téléchargement de boite, son paramètre n'est pas une String "", mais
	 * le status de l'AsyncTask restera quand même en Running. Donc il faut faire attention à ce point.
	 * 
	 * Première passe : on remplis la Map qui contient les getter de boite.
	 * Deuxième passe : on regarde si, en ayant le paramètre String vide, ce qui prouverai un appel de la méthode
	 * depuis la fin de l'exécution de récupération de la liste des boites, ou bien de l'appuis sur un bouton de 
	 * demande de synchro,
	 */
	protected void getBoite(String keyBoiteFinish) {
		L.v("BoiteDeMessage", "On va récupérer le contenu des boites : '" + keyBoiteFinish + "'");
		
		for (Entry<String, String> entry : mapKey.entrySet()) {
			if (!mapATGetBoite.containsKey(entry.getKey())) {
				mapATGetBoite.put(entry.getKey(), getAsyncTaskBoite(entry.getKey(), entry.getValue()));
			}
		}
		
		if (keyBoiteFinish.equals("")) {
			for (AsyncTask<Context, Void, List<Message>> ast : mapATGetBoite.values()) {
				if (ast.getStatus() == AsyncTask.Status.RUNNING) {
					Toast.makeText(this, "Une synchronisation est en cours...", Toast.LENGTH_SHORT).show();
					return;
				}
			}
			boolean allPending = true;
			for (AsyncTask<Context, Void, List<Message>> ast : mapATGetBoite.values()) {
				if (ast.getStatus() != AsyncTask.Status.PENDING) {
					allPending = false;
					break;
				}
			}
			if (allPending) {
				for (AsyncTask<Context, Void, List<Message>> ast : mapATGetBoite.values()) {
					if (ast.getStatus() == AsyncTask.Status.PENDING) {
						ast.execute(this);
						return;
					}
				}
			}
			mapATGetBoite.clear();
			getBoite("");
		}
		else {
			for (AsyncTask<Context, Void, List<Message>> ast : mapATGetBoite.values()) {
				if (ast.getStatus() == AsyncTask.Status.PENDING) {
					ast.execute(this);
					return;
				}
			}
		}
	}
}


