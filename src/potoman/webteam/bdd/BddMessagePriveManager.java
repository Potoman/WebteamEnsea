package potoman.webteam.bdd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.example.webteam.boitemanager.boite.BoiteDeMessage;
import org.example.webteam.boitemanager.boite.messageprive.BoiteDeMessagePrive;
import org.example.webteam.boitemanager.message.Message;
import org.example.webteam.boitemanager.message.email.Email;
import org.example.webteam.boitemanager.message.messageprive.MessagePrive;
import org.example.webteam.loggin.Root;

import potoman.tools.L;
import potoman.webteam.constant.Webteam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

public class BddMessagePriveManager extends BddMessageManager {
	 
	private static final int VERSION_BDD = 12;
	private static final String NOM_BDD = "messagePrive.db";
 
	private SQLiteDatabase bdd;
 
	private BddMessagePrive maBddMessagePrive;
	
	private Context context = null;
	
	public BddMessagePriveManager(Context context){
		this.context = context;
		//On créer la BDD et sa table
		maBddMessagePrive = new BddMessagePrive(context, NOM_BDD, null, VERSION_BDD);
	}

	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBddMessagePrive.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public void insert(Message message){
		L.v("BddMessagePriveManager", "insert");
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(BddMessagePrive.COL_ID, message.getId());
		values.put(BddMessagePrive.COL_NOM_BOITE, message.getNomBoite());
		values.put(BddMessagePrive.COL_ID_FROM, ((MessagePrive)message).getIdFrom());
		values.put(BddMessagePrive.COL_PSEUDO_FROM, ((MessagePrive)message).getPseudoFrom());
		values.put(BddMessagePrive.COL_ID_TO, ((MessagePrive)message).getIdTo());
		values.put(BddMessagePrive.COL_PSEUDO_TO, ((MessagePrive)message).getPseudoTo());
		values.put(BddMessagePrive.COL_TITRE, message.getTitre());
		values.put(BddMessagePrive.COL_DATA, message.getData());
		values.put(BddMessagePrive.COL_TIME, message.getTime());
		values.put(BddMessagePrive.COL_FAVORIS, message.getFavoris());
		values.put(BddMessagePrive.COL_REPONSE, message.getReponse());
		values.put(BddMessagePrive.COL_CONTENU_LOAD, message.getContenuLoad());
		values.put(BddMessagePrive.COL_ETAT_BOITE, message.getEtatBoite());
		long id = bdd.insert(BddMessagePrive.TABLE_MESSAGE_PRIVE, null, values);
		L.v("BddMessagePriveManager", "insert : id : " + id);
	}
	
	@Override
	public int updateMessageContenuLoad(Message message) {
		//La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
		//il faut simple préciser quell_WEBe livre on doit mettre à jour grâce à l'ID
		ContentValues values = new ContentValues();
		values.put(BddMessagePrive.COL_CONTENU_LOAD, message.getContenuLoad());
		values.put(BddMessagePrive.COL_DATA, message.getData());
		return bdd.update(BddMessagePrive.TABLE_MESSAGE_PRIVE, values, BddMessagePrive.COL_ID + " = " + message.getId() + " AND " + BddMessagePrive.COL_NOM_BOITE + " LIKE \"" + message.getNomBoite() + "\"", null);
	}

	@Override
	public int updateMessageState(Message message) {
		Message messageInBdd = getMessage(message.getId(), message.getNomBoite());
		message.setContenuLoad(messageInBdd.getContenuLoad());
		ContentValues values = new ContentValues();
		values.put(BddMessagePrive.COL_ETAT_BOITE, message.getEtatBoite());
		values.put(BddMessagePrive.COL_CONTENU_LOAD, message.getContenuLoad());
		return bdd.update(BddMessagePrive.TABLE_MESSAGE_PRIVE, values, BddMessagePrive.COL_ID + " = " + message.getId() + " AND " + BddMessagePrive.COL_NOM_BOITE + " LIKE \"" + message.getNomBoite() + "\"", null);
	}
	
	private Message cursorToMessage(Cursor c){
		if (c.getCount() == 0) {
			L.e("BddMessagePriveManager", "COUNT = 0");
			return null;
		}
		
		c.moveToFirst();
		
		Message message = null;
//		switch (whichBoite) {
//		case BoiteDeMessage.BOITE_DE_RECEPTION:
		message = new MessagePrive(c.getInt(BddMessagePrive.NUM_COL_ID),
				c.getString(BddMessagePrive.NUM_COL_NOM_BOITE),
				c.getInt(BddMessagePrive.NUM_COL_ID_FROM),
				c.getString(BddMessagePrive.NUM_COL_PSEUDO_FROM),
				c.getInt(BddMessagePrive.NUM_COL_ID_TO),
				c.getString(BddMessagePrive.NUM_COL_PSEUDO_TO),
				c.getString(BddMessagePrive.NUM_COL_TITRE),
				c.getString(BddMessagePrive.NUM_COL_DATA),
				c.getString(BddMessagePrive.NUM_COL_TIME),
				c.getInt(BddMessagePrive.NUM_COL_CONTENU_LOAD),
				c.getInt(BddMessagePrive.NUM_COL_ETAT_BOITE));
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return message;
	}
	
	public void clear() {
		bdd.delete(BddMessagePrive.TABLE_MESSAGE_PRIVE, null, null);
	}
	
	private void cursorToListMessage(Cursor c, List<Message> maListeDeMessage) {
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		maListeDeMessage.clear();
		if (c.getCount() != 0) {
			//Sinon on se place sur le premier élément
			MessagePrive messagePrive = null;
			c.moveToFirst();
			do {
				messagePrive = new MessagePrive(c.getInt(BddMessagePrive.NUM_COL_ID),
						c.getString(BddMessagePrive.NUM_COL_NOM_BOITE),
						c.getInt(BddMessagePrive.NUM_COL_ID_FROM),
						c.getString(BddMessagePrive.NUM_COL_PSEUDO_FROM),
						c.getInt(BddMessagePrive.NUM_COL_ID_TO),
						c.getString(BddMessagePrive.NUM_COL_PSEUDO_TO),
						c.getString(BddMessagePrive.NUM_COL_TITRE),
						c.getString(BddMessagePrive.NUM_COL_DATA),
						c.getString(BddMessagePrive.NUM_COL_TIME),
						c.getInt(BddMessagePrive.NUM_COL_CONTENU_LOAD),
						c.getInt(BddMessagePrive.NUM_COL_ETAT_BOITE));
				maListeDeMessage.add(messagePrive);
			}
			while (c.moveToNext());
			//On ferme le cursor
			c.close();
		}
	}


	@Override
	public Message getMessage(int id, String nomBoite) {
		Cursor c = bdd.query(BddMessagePrive.TABLE_MESSAGE_PRIVE, new String[] { 
				BddMessagePrive.COL_ID,
				BddMessagePrive.COL_NOM_BOITE,
				BddMessagePrive.COL_ID_FROM, 
				BddMessagePrive.COL_PSEUDO_FROM,
				BddMessagePrive.COL_ID_TO, 
				BddMessagePrive.COL_PSEUDO_TO, 
				BddMessagePrive.COL_TITRE,
				BddMessagePrive.COL_DATA,
				BddMessagePrive.COL_TIME,
				BddMessagePrive.COL_FAVORIS,
				BddMessagePrive.COL_REPONSE,
				BddMessagePrive.COL_CONTENU_LOAD,
				BddMessagePrive.COL_ETAT_BOITE}, BddMessagePrive.COL_ID + " = " + id + " AND " + BddMessagePrive.COL_NOM_BOITE + " LIKE \"" + nomBoite + "\"", null, null, null, null);
		return cursorToMessage(c);
	}
	
	@Override
	public void deleteBoite(String nomBoite) {
		L.i("BddMessagePriveManager", "deleteBoite - nomboite = " + nomBoite);
		
		bdd.delete(BddMessagePrive.TABLE_MESSAGE_PRIVE, BddMessagePrive.COL_NOM_BOITE + " LIKE \"" + nomBoite + "\"", null);
	}
	
	@Override
	public void deleteMessage(int id, String nomBoite) {
		List<Message> tempListMessage = new ArrayList<Message>();
		getAllMessageInBoite(nomBoite, tempListMessage);
		if (tempListMessage != null && tempListMessage.size() > 0) {
			bdd.delete(BddMessagePrive.TABLE_MESSAGE_PRIVE, BddMessagePrive.COL_ID + " LIKE \"" + id + "\" AND " + BddMessagePrive.COL_NOM_BOITE + " LIKE \"" + nomBoite + "\"", null);
		}
	}

	@Override
	public void getAllMessageInBoite(String nomBoite, List<Message> maListeDeMessage) {
		Cursor c = bdd.query(BddMessagePrive.TABLE_MESSAGE_PRIVE, new String[] { 
				BddMessagePrive.COL_ID,
				BddMessagePrive.COL_NOM_BOITE,
				BddMessagePrive.COL_ID_FROM, 
				BddMessagePrive.COL_PSEUDO_FROM,
				BddMessagePrive.COL_ID_TO, 
				BddMessagePrive.COL_PSEUDO_TO, 
				BddMessagePrive.COL_TITRE,
				BddMessagePrive.COL_DATA,
				BddMessagePrive.COL_TIME,
				BddMessagePrive.COL_FAVORIS,
				BddMessagePrive.COL_REPONSE,
				BddMessagePrive.COL_CONTENU_LOAD,
				BddMessagePrive.COL_ETAT_BOITE}, 
				"(" + BddMessagePrive.COL_ETAT_BOITE + " LIKE \"" + Message.ETAT_LU + "\" OR " + BddMessagePrive.COL_ETAT_BOITE + " LIKE \"" + Message.ETAT_NON_LU + "\") AND (" + BddMessagePrive.COL_ID_FROM + " LIKE \"" + PreferenceManager.getDefaultSharedPreferences(context).getInt(Webteam.ID, 0) + "\" OR " + BddMessagePrive.COL_ID_TO + " LIKE \"" + PreferenceManager.getDefaultSharedPreferences(context).getInt(Webteam.ID, 0) + "\") AND " + BddMessagePrive.COL_NOM_BOITE + " LIKE \"" + nomBoite + "\"", null, null, null, BddMessagePrive.COL_ID + " DESC");
		cursorToListMessage(c, maListeDeMessage);
	}

	@Override
	public void getAllMessage(Map<String, List<Message>> mapListMessageByBoite) {
		
		List<Message> tempListMessage = new ArrayList<Message>();
		Cursor c = bdd.query(BddMessagePrive.TABLE_MESSAGE_PRIVE, new String[] { 
				BddMessagePrive.COL_ID,
				BddMessagePrive.COL_NOM_BOITE,
				BddMessagePrive.COL_ID_FROM, 
				BddMessagePrive.COL_PSEUDO_FROM,
				BddMessagePrive.COL_ID_TO, 
				BddMessagePrive.COL_PSEUDO_TO, 
				BddMessagePrive.COL_TITRE,
				BddMessagePrive.COL_DATA,
				BddMessagePrive.COL_TIME,
				BddMessagePrive.COL_FAVORIS,
				BddMessagePrive.COL_REPONSE,
				BddMessagePrive.COL_CONTENU_LOAD,
				BddMessagePrive.COL_ETAT_BOITE}, 
				"(" + BddMessagePrive.COL_ETAT_BOITE + " LIKE \"" + Message.ETAT_LU + "\" OR " + BddMessagePrive.COL_ETAT_BOITE + " LIKE \"" + Message.ETAT_NON_LU + "\") AND (" + BddMessagePrive.COL_ID_FROM + " LIKE \"" + PreferenceManager.getDefaultSharedPreferences(context).getInt(Webteam.ID, 0) + "\" OR " + BddMessagePrive.COL_ID_TO + " LIKE \"" + PreferenceManager.getDefaultSharedPreferences(context).getInt(Webteam.ID, 0) + "\")", null, null, null, BddMessagePrive.COL_ID + " DESC");
		cursorToListMessage(c, tempListMessage);
		
		for (Message message : tempListMessage) {
			L.e("BddMessagePriveManager", "getAllMessage : nomboite = " + message.getNomBoite() + ", id = " + message.getId());
			if (!mapListMessageByBoite.containsKey(message.getNomBoite())) {
				mapListMessageByBoite.put(message.getNomBoite(), new ArrayList<Message>());
			}
			mapListMessageByBoite.get(message.getNomBoite()).add(message);
		}
	}
}

