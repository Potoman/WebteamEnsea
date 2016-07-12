
package com.potoman.webteam.bdd;

import java.util.ArrayList;
import java.util.List;

import com.potoman.tools.L;
import com.potoman.webteam.caligula.UrlCaligula;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BddUrlCaligulaManager {
	 
	private static final int VERSION_BDD = 3;
	private static final String NOM_BDD = "urlCaligula.db";
 
	private SQLiteDatabase bdd;
 
	private BddUrlCaligula maBddUrlCaligula;
	
	public BddUrlCaligulaManager(Context context){
		//On créer la BDD et sa table
		maBddUrlCaligula = new BddUrlCaligula(context, NOM_BDD, null, VERSION_BDD);
	}

	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBddUrlCaligula.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public void insertUrlCaligula(UrlCaligula urlCaligula){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(BddUrlCaligula.COL_NAME, urlCaligula.getName());
		values.put(BddUrlCaligula.COL_URL, urlCaligula.getUrl());
		values.put(BddUrlCaligula.COL_WHAT, urlCaligula.getWhat());
		//on insère l'objet dans la BDD via  le ContentValues
		//urlCaligula.setId((int)bdd.insert(BddCaligula.TABLE_CALIGULA, null, values));
		long id = bdd.insert(BddUrlCaligula.TABLE_URL_CALIGULA, null, values);
		L.v("BddUrlCaligulaManager", "insert : id : " + id + ", name : " + urlCaligula.getName() + ", url : " + urlCaligula.getUrl() + ", what : " + urlCaligula.getWhat());
		//return cours.getId();
	}
	
	public UrlCaligula getUrlCaligula(int id){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(BddUrlCaligula.TABLE_URL_CALIGULA, new String[] { 
				BddUrlCaligula.COL_ID,
				BddUrlCaligula.COL_NAME, 
				BddUrlCaligula.COL_URL,
				BddUrlCaligula.COL_WHAT}, BddUrlCaligula.COL_ID + " LIKE \"" + id +"\" ", null, null, null, null);
		return cursorToUrlCaligula(c);
	}
	
	//Cette méthode permet de convertir un cursor en un livre
	private UrlCaligula cursorToUrlCaligula(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		//Sinon on se place sur le premier élément
		c.moveToFirst();
		//On créé un livre
		UrlCaligula urlCaligula = new UrlCaligula(c.getInt(BddUrlCaligula.NUM_COL_ID),
				c.getString(BddUrlCaligula.NUM_COL_NAME),
				c.getString(BddUrlCaligula.NUM_COL_URL),
				c.getInt(BddUrlCaligula.NUM_COL_WHAT));
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return urlCaligula;
	}
	
	public List<UrlCaligula> getAllUrlCaligula(){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(BddUrlCaligula.TABLE_URL_CALIGULA, new String[] {
				BddUrlCaligula.COL_ID,
				BddUrlCaligula.COL_NAME, 
				BddUrlCaligula.COL_URL, 
				BddUrlCaligula.COL_WHAT}, null, null, null, null, null);
		return cursorToListUrlCaligula(c);
	}
	
	public void clear() {
		bdd.delete(BddUrlCaligula.TABLE_URL_CALIGULA, null, null);
	}
	
	private List<UrlCaligula> cursorToListUrlCaligula(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return new ArrayList<UrlCaligula>();
 
		List<UrlCaligula> maListeDeUrlCaligula = new ArrayList<UrlCaligula>();
		//Sinon on se place sur le premier élément
		UrlCaligula urlCaligula = null;
		c.moveToFirst();
		do
		{
			//On créé un livre
			//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
			urlCaligula = new UrlCaligula(c.getInt(BddUrlCaligula.NUM_COL_ID),
				c.getString(BddUrlCaligula.NUM_COL_NAME),
				c.getString(BddUrlCaligula.NUM_COL_URL),
				c.getInt(BddUrlCaligula.NUM_COL_WHAT));
			maListeDeUrlCaligula.add(urlCaligula);
		}
		while (c.moveToNext());
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return maListeDeUrlCaligula;
	}
}

