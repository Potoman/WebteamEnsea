package potoman.webteam.bdd.contact;

import java.util.ArrayList;
import java.util.List;

import org.example.webteam.eleve.ContactWebteam;
import org.example.webteam.loggin.Root;

import potoman.tools.L;
import potoman.webteam.constant.Webteam;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

public class BddContactManager {
	 
	private static final int VERSION_BDD = 5;	//Version 1 sans le favoris.
	private static final String NOM_BDD = "profil.db";
 
	private SQLiteDatabase bdd;
 
	private BddContact maBddProfil;
 
	private Context context = null;
	
	public BddContactManager(Context context){
		this.context = context;
		//On créer la BDD et sa table
		maBddProfil = new BddContact(context, NOM_BDD, null, VERSION_BDD);
	}
 
	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBddProfil.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public long insertProfil(ContactWebteam profil){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(BddContact.COL_PSEUDO, profil.getPseudo());
		values.put(BddContact.COL_ID, profil.getId());
		values.put(BddContact.COL_PRENOM, profil.getPrenom());
		values.put(BddContact.COL_NOM, profil.getNom());
		values.put(BddContact.COL_ADRESSE, profil.getAdresse());
		values.put(BddContact.COL_ADRESSE_PARENT, profil.getAdresseParent());
		values.put(BddContact.COL_URL_IMAGE, profil.getUrlImage());
		values.put(BddContact.COL_SITE_INTERNET, profil.getSiteInternet());
		values.put(BddContact.COL_TELEPHONE, profil.getTelephone());
		values.put(BddContact.COL_TELEPHONE_FIXE, profil.getTelephoneFixe());
		values.put(BddContact.COL_TELEPHONE_PARENT, profil.getTelephoneParent());
		values.put(BddContact.COL_SIGNATURE, profil.getSignature());
		values.put(BddContact.COL_EMAIL, profil.getEmail());
		values.put(BddContact.COL_CLASSE, profil.getClasse());
		values.put(BddContact.COL_DATE_DE_NAISSANCE, profil.getDateDeNaissance());
		values.put(BddContact.COL_FAVORIS, profil.getFavorisInt());
		values.put(BddContact.COL_PSEUDO_SQUIRREL, profil.getPseudoSquirrel());
		values.put(BddContact.COL_PASSWORD_SQUIRREL, profil.getPasswordSquirrel());
		values.put(BddContact.COL_PSEUDO_CLUB_PHOTO, profil.getPseudoSquirrel());
		values.put(BddContact.COL_PASSWORD_CLUB_PHOTO, profil.getPasswordSquirrel());
		//on insère l'objet dans la BDD via le ContentValues
		long id = bdd.insert(BddContact.TABLE_PROFIL, null, values);
		return id;
	}
 
	public int deleteProfil(int id) {
		return bdd.delete(BddContact.TABLE_PROFIL, BddContact.COL_ID + " = " +id, null);
	}
	
	public int updateProfil(ContactWebteam profil){
		ContentValues values = new ContentValues();
		values.put(BddContact.COL_PSEUDO, profil.getPseudo());
		values.put(BddContact.COL_ID, profil.getId());
		values.put(BddContact.COL_PRENOM, profil.getPrenom());
		values.put(BddContact.COL_NOM, profil.getNom());
		values.put(BddContact.COL_ADRESSE, profil.getAdresse());
		values.put(BddContact.COL_ADRESSE_PARENT, profil.getAdresseParent());
		values.put(BddContact.COL_URL_IMAGE, profil.getUrlImage());
		values.put(BddContact.COL_SITE_INTERNET, profil.getSiteInternet());
		values.put(BddContact.COL_TELEPHONE, profil.getTelephone());
		values.put(BddContact.COL_TELEPHONE_FIXE, profil.getTelephoneFixe());
		values.put(BddContact.COL_TELEPHONE_PARENT, profil.getTelephoneParent());
		values.put(BddContact.COL_SIGNATURE, profil.getSignature());
		values.put(BddContact.COL_EMAIL, profil.getEmail());
		values.put(BddContact.COL_CLASSE, profil.getClasse());
		values.put(BddContact.COL_DATE_DE_NAISSANCE, profil.getDateDeNaissance());
		values.put(BddContact.COL_FAVORIS, profil.getFavorisInt());
		values.put(BddContact.COL_PSEUDO_SQUIRREL, profil.getPseudoSquirrel());
		values.put(BddContact.COL_PASSWORD_SQUIRREL, profil.getPasswordSquirrel());
		values.put(BddContact.COL_PSEUDO_CLUB_PHOTO, profil.getPseudoClubPhoto());
		values.put(BddContact.COL_PASSWORD_CLUB_PHOTO, profil.getPasswordClubPhoto());
		return bdd.update(BddContact.TABLE_PROFIL, values, BddContact.COL_ID + " = " + profil.getId(), null);
	}

	public void setPriority(ContactWebteam profil) {
		ContentValues values = new ContentValues();
		
		values.put(BddContact.COL_PSEUDO, profil.getPseudo());
		values.put(BddContact.COL_ID, profil.getId());
		values.put(BddContact.COL_PRENOM, profil.getPrenom());
		values.put(BddContact.COL_NOM, profil.getNom());
		values.put(BddContact.COL_ADRESSE, profil.getAdresse());
		values.put(BddContact.COL_ADRESSE_PARENT, profil.getAdresseParent());
		values.put(BddContact.COL_URL_IMAGE, profil.getUrlImage());
		values.put(BddContact.COL_SITE_INTERNET, profil.getSiteInternet());
		values.put(BddContact.COL_TELEPHONE, profil.getTelephone());
		values.put(BddContact.COL_TELEPHONE_FIXE, profil.getTelephoneFixe());
		values.put(BddContact.COL_TELEPHONE_PARENT, profil.getTelephoneParent());
		values.put(BddContact.COL_SIGNATURE, profil.getSignature());
		values.put(BddContact.COL_EMAIL, profil.getEmail());
		values.put(BddContact.COL_CLASSE, profil.getClasse());
		values.put(BddContact.COL_DATE_DE_NAISSANCE, profil.getDateDeNaissance());
		values.put(BddContact.COL_FAVORIS, profil.getFavorisInt());
		values.put(BddContact.COL_PSEUDO_SQUIRREL, profil.getPseudoSquirrel());
		values.put(BddContact.COL_PASSWORD_SQUIRREL, profil.getPasswordSquirrel());
		values.put(BddContact.COL_PSEUDO_CLUB_PHOTO, profil.getPseudoSquirrel());
		values.put(BddContact.COL_PASSWORD_CLUB_PHOTO, profil.getPasswordSquirrel());
		
		bdd.update(BddContact.TABLE_PROFIL, values, BddContact.COL_ID + " = " + profil.getId(), null);
	}
	
	/*public int removeLivreWithID(int id){
		//Suppression d'un livre de la BDD grâce à l'ID
		return bdd.delete(BddProfil.TABLE_PROFIL, BddProfil.COL_ID + " = " + id, null);
	}*/
	
	public ContactWebteam getProfil(String pseudo){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(BddContact.TABLE_PROFIL, new String[] { 
				BddContact.COL_PSEUDO, 
				BddContact.COL_ID, 
				BddContact.COL_PRENOM, 
				BddContact.COL_NOM, 
				BddContact.COL_ADRESSE, 
				BddContact.COL_ADRESSE_PARENT, 
				BddContact.COL_URL_IMAGE, 
				BddContact.COL_SITE_INTERNET, 
				BddContact.COL_TELEPHONE, 
				BddContact.COL_TELEPHONE_FIXE, 
				BddContact.COL_TELEPHONE_PARENT, 
				BddContact.COL_SIGNATURE, 
				BddContact.COL_EMAIL, 
				BddContact.COL_CLASSE, 
				BddContact.COL_DATE_DE_NAISSANCE, 
				BddContact.COL_FAVORIS, 
				BddContact.COL_PSEUDO_SQUIRREL, 
				BddContact.COL_PASSWORD_SQUIRREL,
				BddContact.COL_PSEUDO_CLUB_PHOTO, 
				BddContact.COL_PASSWORD_CLUB_PHOTO}, BddContact.COL_PSEUDO + " LIKE \"" + pseudo +"\" ", null, null, null, null);
		return cursorToProfil(c);
	}
	
	public ContactWebteam getContactWebteam(int idWebteam){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(BddContact.TABLE_PROFIL, new String[] { 
				BddContact.COL_PSEUDO, 
				BddContact.COL_ID, 
				BddContact.COL_PRENOM, 
				BddContact.COL_NOM, 
				BddContact.COL_ADRESSE, 
				BddContact.COL_ADRESSE_PARENT, 
				BddContact.COL_URL_IMAGE, 
				BddContact.COL_SITE_INTERNET, 
				BddContact.COL_TELEPHONE, 
				BddContact.COL_TELEPHONE_FIXE, 
				BddContact.COL_TELEPHONE_PARENT, 
				BddContact.COL_SIGNATURE, 
				BddContact.COL_EMAIL, 
				BddContact.COL_CLASSE, 
				BddContact.COL_DATE_DE_NAISSANCE, 
				BddContact.COL_FAVORIS, 
				BddContact.COL_PSEUDO_SQUIRREL, 
				BddContact.COL_PASSWORD_SQUIRREL,
				BddContact.COL_PSEUDO_CLUB_PHOTO, 
				BddContact.COL_PASSWORD_CLUB_PHOTO}, BddContact.COL_ID + " LIKE \"" + idWebteam +"\" ", null, null, null, null);
		return cursorToProfil(c);
	}

	public SQLiteDatabase getBdd() {
		return bdd;
	}
	
	public SQLiteOpenHelper getOpenHelper() {
		return maBddProfil;
	}
	
	public List<ContactWebteam> getProfilFavoris(){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		L.v("BddProfilManager", "plip");
		Cursor c = bdd.query(BddContact.TABLE_PROFIL, new String[] {
				//BddProfil.COL_ID, 
				BddContact.COL_PSEUDO, 
				BddContact.COL_ID, 
				BddContact.COL_PRENOM, 
				BddContact.COL_NOM, 
				BddContact.COL_ADRESSE, 
				BddContact.COL_ADRESSE_PARENT, 
				BddContact.COL_URL_IMAGE, 
				BddContact.COL_SITE_INTERNET, 
				BddContact.COL_TELEPHONE, 
				BddContact.COL_TELEPHONE_FIXE, 
				BddContact.COL_TELEPHONE_PARENT, 
				BddContact.COL_SIGNATURE, 
				BddContact.COL_EMAIL, 
				BddContact.COL_CLASSE, 
				BddContact.COL_DATE_DE_NAISSANCE, 
				BddContact.COL_FAVORIS, 
				BddContact.COL_PSEUDO_SQUIRREL, 
				BddContact.COL_PASSWORD_SQUIRREL, 
				BddContact.COL_PSEUDO_CLUB_PHOTO, 
				BddContact.COL_PASSWORD_CLUB_PHOTO}, BddContact.COL_FAVORIS + " LIKE 1", null, null, null, null);
		return cursorToListProfil(c);
	}
	
	public List<ContactWebteam> getAllProfil(){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		L.v("BddProfilManager", "plip");
		Cursor c = bdd.query(BddContact.TABLE_PROFIL, new String[] {
				//BddProfil.COL_ID, 
				BddContact.COL_PSEUDO, 
				BddContact.COL_ID, 
				BddContact.COL_PRENOM, 
				BddContact.COL_NOM, 
				BddContact.COL_ADRESSE, 
				BddContact.COL_ADRESSE_PARENT, 
				BddContact.COL_URL_IMAGE, 
				BddContact.COL_SITE_INTERNET, 
				BddContact.COL_TELEPHONE, 
				BddContact.COL_TELEPHONE_FIXE, 
				BddContact.COL_TELEPHONE_PARENT, 
				BddContact.COL_SIGNATURE, 
				BddContact.COL_EMAIL, 
				BddContact.COL_CLASSE, 
				BddContact.COL_DATE_DE_NAISSANCE, 
				BddContact.COL_FAVORIS, 
				BddContact.COL_PSEUDO_SQUIRREL, 
				BddContact.COL_PASSWORD_SQUIRREL, 
				BddContact.COL_PSEUDO_CLUB_PHOTO, 
				BddContact.COL_PASSWORD_CLUB_PHOTO}, null, null, null, null, null);
		return cursorToListProfil(c);
	}
	
	public void clear() {
		bdd.delete(BddContact.TABLE_PROFIL, null, null);
	}
	
	//Cette méthode permet de convertir un cursor en un livre
	private ContactWebteam cursorToProfil(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		//Sinon on se place sur le premier élément
		c.moveToFirst();
		//On créé un livre
		ContactWebteam profil = new ContactWebteam();
		//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
		profil.setPseudo(c.getString(BddContact.NUM_COL_PSEUDO)); 
		profil.setId(c.getInt(BddContact.NUM_COL_ID)); 
		profil.setPrenom(c.getString(BddContact.NUM_COL_PRENOM)); 
		profil.setNom(c.getString(BddContact.NUM_COL_NOM)); 
		profil.setAdresse(c.getString(BddContact.NUM_COL_ADRESSE)); 
		profil.setAdresseParent(c.getString(BddContact.NUM_COL_ADRESSE_PARENT)); 
		profil.setUrlImage(c.getString(BddContact.NUM_COL_URL_IMAGE));
		profil.setSiteInternet(c.getString(BddContact.NUM_COL_SITE_INTERNET));
		profil.setTelephone(c.getString(BddContact.NUM_COL_TELEPHONE));
		profil.setTelephoneFixe(c.getString(BddContact.NUM_COL_TELEPHONE_FIXE));
		profil.setTelephoneParent(c.getString(BddContact.NUM_COL_TELEPHONE_PARENT));
		profil.setSignature(c.getString(BddContact.NUM_COL_SIGNATURE)); 
		profil.setEmail(c.getString(BddContact.NUM_COL_EMAIL));
		profil.setClasse(c.getString(BddContact.NUM_COL_CLASSE));
		profil.setDateDeNaissance(c.getString(BddContact.NUM_COL_DATE_DE_NAISSANCE));
		profil.setFavorisInt(c.getInt(BddContact.NUM_COL_FAVORIS));
		profil.setPseudoSquirrel(c.getString(BddContact.NUM_COL_PSEUDO_SQUIRREL));
		profil.setPasswordSquirrel(c.getString(BddContact.NUM_COL_PASSWORD_SQUIRREL));
		profil.setPseudoClubPhoto(c.getString(BddContact.NUM_COL_PSEUDO_CLUB_PHOTO));
		profil.setPasswordClubPhoto(c.getString(BddContact.NUM_COL_PASSWORD_CLUB_PHOTO));
		
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return profil;
	}
	
	public static List<ContactWebteam> cursorToListProfil(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		List<ContactWebteam> maListeDeProfil = new ArrayList<ContactWebteam>();
		//Sinon on se place sur le premier élément
		ContactWebteam profil = null;
		c.moveToFirst();
		do
		{
			//On créé un livre
			//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
			profil = new ContactWebteam();
			profil.setPseudo(c.getString(BddContact.NUM_COL_PSEUDO)); 
			profil.setId(c.getInt(BddContact.NUM_COL_ID)); 
			profil.setPrenom(c.getString(BddContact.NUM_COL_PRENOM)); 
			profil.setNom(c.getString(BddContact.NUM_COL_NOM)); 
			profil.setAdresse(c.getString(BddContact.NUM_COL_ADRESSE)); 
			profil.setAdresseParent(c.getString(BddContact.NUM_COL_ADRESSE_PARENT)); 
			profil.setUrlImage(c.getString(BddContact.NUM_COL_URL_IMAGE));
			profil.setSiteInternet(c.getString(BddContact.NUM_COL_SITE_INTERNET));
			profil.setTelephone(c.getString(BddContact.NUM_COL_TELEPHONE));
			profil.setTelephoneFixe(c.getString(BddContact.NUM_COL_TELEPHONE_FIXE));
			profil.setTelephoneParent(c.getString(BddContact.NUM_COL_TELEPHONE_PARENT));
			profil.setSignature(c.getString(BddContact.NUM_COL_SIGNATURE)); 
			profil.setEmail(c.getString(BddContact.NUM_COL_EMAIL));
			profil.setClasse(c.getString(BddContact.NUM_COL_CLASSE));
			profil.setDateDeNaissance(c.getString(BddContact.NUM_COL_DATE_DE_NAISSANCE));
			profil.setFavorisInt(c.getInt(BddContact.NUM_COL_FAVORIS));
			profil.setPseudoSquirrel(c.getString(BddContact.NUM_COL_PSEUDO_SQUIRREL));
			profil.setPasswordSquirrel(c.getString(BddContact.NUM_COL_PASSWORD_SQUIRREL));
			profil.setPseudoClubPhoto(c.getString(BddContact.NUM_COL_PSEUDO_CLUB_PHOTO));
			profil.setPasswordClubPhoto(c.getString(BddContact.NUM_COL_PASSWORD_CLUB_PHOTO));
			maListeDeProfil.add(profil);
			L.v("BddProfilManager", "plop");
		}
		while (c.moveToNext());
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return maListeDeProfil;
	}
	
	public void updateParamLogSquirrel(String pseudoSquirrel, String passwordSquirrel) {
		ContentValues values = new ContentValues();
		values.put(BddContact.COL_PSEUDO_SQUIRREL, pseudoSquirrel);
		values.put(BddContact.COL_PASSWORD_SQUIRREL, passwordSquirrel);
		bdd.update(BddContact.TABLE_PROFIL, values, BddContact.COL_ID + " = \"" + PreferenceManager.getDefaultSharedPreferences(context).getInt(Webteam.ID, 0) + "\"", null);
	}
	
	public void updateParamLogClubPhoto(String pseudoClubPhoto, String passwordClubPhoto) {
		ContentValues values = new ContentValues();
		values.put(BddContact.COL_PSEUDO_CLUB_PHOTO, pseudoClubPhoto);
		values.put(BddContact.COL_PASSWORD_CLUB_PHOTO, passwordClubPhoto);
		bdd.update(BddContact.TABLE_PROFIL, values, BddContact.COL_ID + " = \"" + PreferenceManager.getDefaultSharedPreferences(context).getInt(Webteam.ID, 0) + "\"", null);
	}
}

