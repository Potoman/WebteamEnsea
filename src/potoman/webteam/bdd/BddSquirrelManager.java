package potoman.webteam.bdd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.example.webteam.boitemanager.Squirrel;
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

public class BddSquirrelManager extends BddMessageManager {
	
	private static final int VERSION_BDD = 4;
	private static final String NOM_BDD = "squirrel.db";
 
	private SQLiteDatabase bdd;
 
	private BddSquirrel maBddSquirrel;
	
	private Context context = null;
	
	public BddSquirrelManager(Context context) {
		this.context = context;
		//On créer la BDD et sa table
		maBddSquirrel = new BddSquirrel(context, NOM_BDD, null, VERSION_BDD);
	}

	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBddSquirrel.getWritableDatabase();
	}
 
	public void close() {
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public void insert(Message message){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		L.v("BddSquirrelManager", "id : " + message.getId());
		values.put(BddSquirrel.COL_ID, message.getId());
		values.put(BddSquirrel.COL_EMAIL_FROM, ((Email)message).getEmailFrom());
		values.put(BddSquirrel.COL_EMAIL_TO, ((Email)message).getEmailToString());
		values.put(BddSquirrel.COL_TITRE, message.getTitre());
		values.put(BddSquirrel.COL_DATA, message.getData());
		values.put(BddSquirrel.COL_TIME, message.getTime());
		values.put(BddSquirrel.COL_REPONSE, message.getReponse());
		values.put(BddSquirrel.COL_CONTENU_LOAD, message.getContenuLoad());
		values.put(BddSquirrel.COL_ETAT_BOITE, message.getEtatBoite());
		values.put(BddSquirrel.COL_NOM_BOITE, message.getNomBoite());
		values.put(BddSquirrel.COL_PROPRIO, ((Email)message).getProprio());
		//on insère l'objet dans la BDD via  le ContentValues
		long id = bdd.insert(BddSquirrel.TABLE_SQUIRREL, null, values);
		L.v("BddSquirrelManager", "insert : id : " + id);
	}
 
	public Message getMessage(int id, String nomBoite) {
		Cursor c = bdd.query(BddSquirrel.TABLE_SQUIRREL, new String[] { 
				BddSquirrel.COL_ID,
				BddSquirrel.COL_EMAIL_FROM, 
				BddSquirrel.COL_EMAIL_TO,
				BddSquirrel.COL_TITRE,
				BddSquirrel.COL_DATA,
				BddSquirrel.COL_TIME,
				BddSquirrel.COL_REPONSE,
				BddSquirrel.COL_CONTENU_LOAD,
				BddSquirrel.COL_ETAT_BOITE,
				BddSquirrel.COL_NOM_BOITE,
				BddSquirrel.COL_PROPRIO}, BddSquirrel.COL_ID + " LIKE \"" + id + "\" AND " + BddSquirrel.COL_NOM_BOITE + " LIKE \"" + nomBoite + "\" AND " + BddSquirrel.COL_PROPRIO + " LIKE \"" + PreferenceManager.getDefaultSharedPreferences(context).getString(Webteam.PSEUDO_WEBMAIL, "") + "\"", null, null, null, null);
		return cursorToMessage(c);
	}

	
	@Override
	public void deleteBoite(String nomBoite) {
//		List<Message> tempList = new ArrayList<Message>();
//		getAllMessageInBoite(nomBoite, tempList);
//		if (tempList != null && tempList.size() > 0) {
			bdd.delete(BddSquirrel.TABLE_SQUIRREL, BddSquirrel.COL_NOM_BOITE + " LIKE \"" + nomBoite + "\"", null);
		//}
	}
	
	public void deleteMessage(int id, String nomBoite) {
		bdd.delete(BddSquirrel.TABLE_SQUIRREL, BddSquirrel.COL_ID + " = " + id + " AND " + BddSquirrel.COL_NOM_BOITE + " LIKE \"" + nomBoite + "\"", null);
	}
	
	public int updateMessageContenuLoad(Message message) {
		ContentValues values = new ContentValues();
		values.put(BddSquirrel.COL_CONTENU_LOAD, message.getContenuLoad());
		values.put(BddSquirrel.COL_DATA, message.getData());
		values.put(BddSquirrel.COL_TIME, message.getTime());
		values.put(BddSquirrel.COL_ETAT_BOITE, message.getEtatBoite());
		values.put(BddSquirrel.COL_EMAIL_TO, ((Email) message).getEmailToString());
		return bdd.update(BddSquirrel.TABLE_SQUIRREL, values, BddSquirrel.COL_ID + " = " + message.getId() + " AND " + BddSquirrel.COL_NOM_BOITE + " LIKE \"" + message.getNomBoite() + "\"", null);
	}

	public int updateMessageState(Message message) {
		Message messageInBdd = getMessage(message.getId(), message.getNomBoite());
		message.setContenuLoad(messageInBdd.getContenuLoad());
		ContentValues values = new ContentValues();
		values.put(BddSquirrel.COL_ETAT_BOITE, message.getEtatBoite());
		values.put(BddSquirrel.COL_EMAIL_TO, ((Email) message).getEmailToString());
		values.put(BddSquirrel.COL_CONTENU_LOAD, message.getContenuLoad());
		return bdd.update(BddSquirrel.TABLE_SQUIRREL, values, BddSquirrel.COL_ID + " = " + message.getId() + " AND " + BddSquirrel.COL_NOM_BOITE + " LIKE \"" + message.getNomBoite() + "\"", null);
	}
	
	//Cette méthode permet de convertir un cursor en un livre
	private Message cursorToMessage(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0) {
			L.e("BddSquirrelManager", "Aucun message... COUNT = 0 !");
			return null;
		}
		else {
			L.e("BddSquirrelManager", "Message trouvéééééééééééé ! COUNT = " + c.getCount());
		}
 
		//Sinon on se place sur le premier élément
		c.moveToFirst();
		L.v("BddSquirrelManager", "nom boite = " + c.getString(BddSquirrel.NUM_COL_NOM_BOITE));
		L.v("BddSquirrelManager", "proprio = " + c.getString(BddSquirrel.NUM_COL_PROPRIO));
		Message message = new Email(c.getInt(BddSquirrel.NUM_COL_ID),
					c.getString(BddSquirrel.NUM_COL_EMAIL_FROM),
					c.getString(BddSquirrel.NUM_COL_EMAIL_TO),
					c.getString(BddSquirrel.NUM_COL_TITRE),
					c.getString(BddSquirrel.NUM_COL_DATA),
					c.getString(BddSquirrel.NUM_COL_TIME),
					c.getInt(BddSquirrel.NUM_COL_REPONSE),
					c.getInt(BddSquirrel.NUM_COL_CONTENU_LOAD),
					c.getInt(BddSquirrel.NUM_COL_ETAT_BOITE),
					c.getString(BddSquirrel.NUM_COL_NOM_BOITE),
					c.getString(BddSquirrel.NUM_COL_PROPRIO));
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return message;
	}
	
	public void clear() {
		bdd.delete(BddSquirrel.TABLE_SQUIRREL, null, null);
	}
	
	private void cursorToListMessage(Cursor c, List<Message> maListeDeMessage){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		maListeDeMessage.clear();
		
		if (c.getCount() != 0) {
			
			//Sinon on se place sur le premier élément
			Message message = null;
			c.moveToFirst();
			do
			{
				//On créé un livre
				//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
				message = new Email(c.getInt(BddSquirrel.NUM_COL_ID),
							c.getString(BddSquirrel.NUM_COL_EMAIL_FROM),
							c.getString(BddSquirrel.NUM_COL_EMAIL_TO),
							c.getString(BddSquirrel.NUM_COL_TITRE),
							c.getString(BddSquirrel.NUM_COL_DATA),
							c.getString(BddSquirrel.NUM_COL_TIME),
							c.getInt(BddSquirrel.NUM_COL_REPONSE),
							c.getInt(BddSquirrel.NUM_COL_CONTENU_LOAD),
							c.getInt(BddSquirrel.NUM_COL_ETAT_BOITE),
							c.getString(BddSquirrel.NUM_COL_NOM_BOITE),
							c.getString(BddSquirrel.NUM_COL_PROPRIO));
				maListeDeMessage.add(message);
			}
			while (c.moveToNext());
			//On ferme le cursor
			c.close();
		}
	}

	public void getAllMessageInBoite(String nomBoite, List<Message> maListeDeMessageBoite) {
		Cursor c = bdd.query(BddSquirrel.TABLE_SQUIRREL, new String[] { 
				BddSquirrel.COL_ID,
				BddSquirrel.COL_EMAIL_FROM, 
				BddSquirrel.COL_EMAIL_TO,
				BddSquirrel.COL_TITRE,
				BddSquirrel.COL_DATA,
				BddSquirrel.COL_TIME,
				BddSquirrel.COL_REPONSE,
				BddSquirrel.COL_CONTENU_LOAD,
				BddSquirrel.COL_ETAT_BOITE,
				BddSquirrel.COL_NOM_BOITE,
				BddSquirrel.COL_PROPRIO}, "(" + BddSquirrel.COL_ETAT_BOITE + " LIKE \"" + Message.ETAT_LU + "\" OR " + BddSquirrel.COL_ETAT_BOITE + " LIKE \"" + Message.ETAT_NON_LU + "\") AND " + BddSquirrel.COL_PROPRIO + " LIKE \"" + PreferenceManager.getDefaultSharedPreferences(context).getString(Webteam.PSEUDO_WEBMAIL, "") + "\" AND " + BddSquirrel.COL_NOM_BOITE + " LIKE \"" + nomBoite + "\"", null, null, null, BddSquirrel.COL_ID + " DESC");
		cursorToListMessage(c, maListeDeMessageBoite);
	}
	
	@Override
	public void getAllMessage(Map<String, List<Message>> mapMessageByBoite) {

		List<Message> tempListMessage = new ArrayList<Message>();
		Cursor c = bdd.query(BddSquirrel.TABLE_SQUIRREL, new String[] { 
				BddSquirrel.COL_ID,
				BddSquirrel.COL_EMAIL_FROM, 
				BddSquirrel.COL_EMAIL_TO,
				BddSquirrel.COL_TITRE,
				BddSquirrel.COL_DATA,
				BddSquirrel.COL_TIME,
				BddSquirrel.COL_REPONSE,
				BddSquirrel.COL_CONTENU_LOAD,
				BddSquirrel.COL_ETAT_BOITE,
				BddSquirrel.COL_NOM_BOITE,
				BddSquirrel.COL_PROPRIO}, BddSquirrel.COL_PROPRIO + " LIKE \"" + PreferenceManager.getDefaultSharedPreferences(context).getString(Webteam.PSEUDO_WEBMAIL, "") + "\"", null, null, null, BddSquirrel.COL_ID + " DESC");
		cursorToListMessage(c, tempListMessage);
		
		for (Message message : tempListMessage) {
			L.e("BddSquirrelManager", "getAllMessage : nomboite = " + message.getNomBoite() + ", id = " + message.getId() + ", proprio = " + ((Email) message).getProprio());
			if (!mapMessageByBoite.containsKey(message.getNomBoite())) {
				mapMessageByBoite.put(message.getNomBoite(), new ArrayList<Message>());
			}
			mapMessageByBoite.get(message.getNomBoite()).add(message);
		}
	}
}
