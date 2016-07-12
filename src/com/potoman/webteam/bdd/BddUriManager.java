package com.potoman.webteam.bdd;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.potoman.tools.L;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class BddUriManager {
	 
	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "url.db";
 
	private SQLiteDatabase bdd;
 
	private BddUri maBddUri;
 
	public BddUriManager(Context context){
		//On créer la BDD et sa table
		maBddUri = new BddUri(context, NOM_BDD, null, VERSION_BDD);
	}
 
	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBddUri.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public long insertUri(Uri lien){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(BddUri.COL_URI, lien.toString());
		//on insère l'objet dans la BDD via le ContentValues
		long id = bdd.insert(BddUri.TABLE_URI, null, values);
		L.v("BddUriManager", "insert : id : " + id);
		return id;
	}
 
	public int deleteProfil(Uri uri) {
		return bdd.delete(BddUri.TABLE_URI, BddUri.COL_URI + " = \"" + uri.toString() + "\"", null);
	}
	
	public int updateProfil(int id, URI lien){
		//La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
		//il faut simple préciser quell_WEBe livre on doit mettre à jour grâce à l'ID
		ContentValues values = new ContentValues();
		values.put(BddUri.COL_URI, lien.toString());
		//return bdd.update(BddProfil.TABLE_PROFIL, values, BddProfil.COL_ID + " = " + id, null);
		return 0;
	}
	
	/*public int removeLivreWithID(int id){
		//Suppression d'un livre de la BDD grâce à l'ID
		return bdd.delete(BddProfil.TABLE_PROFIL, BddProfil.COL_ID + " = " + id, null);
	}*/

	public Uri getUri(Uri lien){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(BddUri.TABLE_URI, new String[] { 
				BddUri.COL_ID, 
				BddUri.COL_URI}, BddUri.COL_URI + " LIKE \"" + lien.toString() + "\" ", null, null, null, null);
		return cursorToUri(c);
	}
	
	public Uri getUri(String lien){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(BddUri.TABLE_URI, new String[] { 
				BddUri.COL_ID, 
				BddUri.COL_URI}, BddUri.COL_URI + " LIKE \"" + lien + "\" ", null, null, null, null);
		return cursorToUri(c);
	}
	
	public List<Uri> getAllUri(){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(BddUri.TABLE_URI, new String[] {
				BddUri.COL_ID, 
				BddUri.COL_URI}, null, null, null, null, null);
		return cursorToListUri(c);
	}
 
	//Cette méthode permet de convertir un cursor en un livre
	private Uri cursorToUri(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		//Sinon on se place sur le premier élément
		c.moveToFirst();
		//On créé un livre
		String tempUri = c.getString(BddUri.NUM_COL_URI);
		Uri lien = Uri.parse(tempUri);
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return lien;
	}
	
	private List<Uri> cursorToListUri(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		List<Uri> maListeDUri = new ArrayList<Uri>();
		c.moveToFirst();
		do
		{
			maListeDUri.add(Uri.parse(c.getString(BddUri.NUM_COL_URI)));
		}
		while (c.moveToNext());
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return maListeDUri;
	}
}

