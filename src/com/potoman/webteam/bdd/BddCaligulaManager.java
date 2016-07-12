
package com.potoman.webteam.bdd;

import java.util.ArrayList;
import java.util.List;

import com.potoman.tools.L;
import com.potoman.webteam.caligula.Cours;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BddCaligulaManager {
	 
	private static final int VERSION_BDD = 2;
	private static final String NOM_BDD = "caligula.db";
 
	private SQLiteDatabase bdd;
 
	private BddCaligula maBddCaligula;
	
	public BddCaligulaManager(Context context){
		//On créer la BDD et sa table
		maBddCaligula = new BddCaligula(context, NOM_BDD, null, VERSION_BDD);
	}

	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBddCaligula.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public long insertCours(Cours cours){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(BddCaligula.COL_DATE, cours.getDate());
		values.put(BddCaligula.COL_HEURE, cours.getHeure());
		values.put(BddCaligula.COL_DUREE, cours.getDuree());
		values.put(BddCaligula.COL_ACTIVITE, cours.getActivite());
		values.put(BddCaligula.COL_STAGIAIRE, cours.getStagiaire());
		values.put(BddCaligula.COL_FORMATEUR, cours.getFormateur());
		values.put(BddCaligula.COL_SALLE, cours.getSalle());
		//on insère l'objet dans la BDD via  le ContentValues
		cours.setId((int)bdd.insert(BddCaligula.TABLE_CALIGULA, null, values));
		L.v("BddCaligulaManager", "insert : id : " + cours.getId());
		return cours.getId();
	}
 
	public Cours getLastCours(int classe){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(BddCaligula.TABLE_CALIGULA, new String[] {
				BddCaligula.COL_ID,
				BddCaligula.COL_DATE, 
				BddCaligula.COL_HEURE, 
				BddCaligula.COL_DUREE,
				BddCaligula.COL_ACTIVITE,
				BddCaligula.COL_STAGIAIRE,
				BddCaligula.COL_FORMATEUR,
				BddCaligula.COL_SALLE}, BddCaligula.COL_STAGIAIRE + " LIKE \"" + classe + "\"", null, null, null, BddRagot.COL_ID + " DESC", "1");
		return cursorToCours(c);
	}
	
	public List<Cours> getAllCours(){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(BddCaligula.TABLE_CALIGULA, new String[] {
				BddCaligula.COL_ID,
				BddCaligula.COL_DATE, 
				BddCaligula.COL_HEURE, 
				BddCaligula.COL_DUREE,
				BddCaligula.COL_ACTIVITE,
				BddCaligula.COL_STAGIAIRE,
				BddCaligula.COL_FORMATEUR,
				BddCaligula.COL_SALLE}, null, null, null, null, null);
		return cursorToListCours(c);
	}
	
	public List<Cours> getAllCours(int classe){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(BddCaligula.TABLE_CALIGULA, new String[] {
				BddCaligula.COL_ID,
				BddCaligula.COL_DATE, 
				BddCaligula.COL_HEURE, 
				BddCaligula.COL_DUREE,
				BddCaligula.COL_ACTIVITE,
				BddCaligula.COL_STAGIAIRE,
				BddCaligula.COL_FORMATEUR,
				BddCaligula.COL_SALLE}, BddCaligula.COL_STAGIAIRE + " LIKE \"" + classe + "\"", null, null, null, null);
		return cursorToListCours(c);
	}
	
	public List<Cours> getAllCours(String date){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(BddCaligula.TABLE_CALIGULA, new String[] {
				BddCaligula.COL_ID,
				BddCaligula.COL_DATE, 
				BddCaligula.COL_HEURE, 
				BddCaligula.COL_DUREE,
				BddCaligula.COL_ACTIVITE,
				BddCaligula.COL_STAGIAIRE,
				BddCaligula.COL_FORMATEUR,
				BddCaligula.COL_SALLE}, BddCaligula.COL_DATE + " LIKE \"" + date + "\"", null, null, null, null);
		return cursorToListCours(c);
	}
	
	public int deleteRagot(int id) {
		return bdd.delete(BddCaligula.TABLE_CALIGULA, BddCaligula.COL_ID + " = " + id, null);
	}
	
	public Cours getCours(int id){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(BddCaligula.TABLE_CALIGULA, new String[] { 
				BddCaligula.COL_ID,
				BddCaligula.COL_DATE, 
				BddCaligula.COL_HEURE, 
				BddCaligula.COL_DUREE,
				BddCaligula.COL_ACTIVITE,
				BddCaligula.COL_STAGIAIRE,
				BddCaligula.COL_FORMATEUR,
				BddCaligula.COL_SALLE}, BddCaligula.COL_ID + " LIKE \"" + id +"\" ", null, null, null, null);
		return cursorToCours(c);
	}
	
	//Cette méthode permet de convertir un cursor en un livre
	private Cours cursorToCours(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		//Sinon on se place sur le premier élément
		c.moveToFirst();
		//On créé un livre
		Cours cours = new Cours(c.getInt(BddCaligula.NUM_COL_ID),
				c.getString(BddCaligula.NUM_COL_DATE),
				c.getString(BddCaligula.NUM_COL_HEURE),
				c.getString(BddCaligula.NUM_COL_DUREE),
				c.getString(BddCaligula.NUM_COL_ACTIVITE),
				c.getInt(BddCaligula.NUM_COL_STAGIAIRE),
				c.getString(BddCaligula.NUM_COL_FORMATEUR),
				c.getString(BddCaligula.NUM_COL_SALLE));
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return cours;
	}
	
	public void clear() {
		bdd.delete(BddCaligula.TABLE_CALIGULA, null, null);
	}
	
	private List<Cours> cursorToListCours(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		List<Cours> maListeDeCours = new ArrayList<Cours>();
		//Sinon on se place sur le premier élément
		Cours cours = null;
		c.moveToFirst();
		do
		{
			//On créé un livre
			//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
			cours = new Cours(c.getInt(BddCaligula.NUM_COL_ID),
					c.getString(BddCaligula.NUM_COL_DATE),
					c.getString(BddCaligula.NUM_COL_HEURE),
					c.getString(BddCaligula.NUM_COL_DUREE),
					c.getString(BddCaligula.NUM_COL_ACTIVITE),
					c.getInt(BddCaligula.NUM_COL_STAGIAIRE),
					c.getString(BddCaligula.NUM_COL_FORMATEUR),
					c.getString(BddCaligula.NUM_COL_SALLE));
			maListeDeCours.add(cours);
		}
		while (c.moveToNext());
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return maListeDeCours;
	}
	
	
}

