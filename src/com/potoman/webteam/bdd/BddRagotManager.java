package com.potoman.webteam.bdd;

import java.util.ArrayList;
import java.util.List;

import com.potoman.tools.L;
import com.potoman.webteam.loggin.Root;
import com.potoman.webteam.ragot.Ragot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BddRagotManager {
	 
	private static final int VERSION_BDD = 2;
	private static final String NOM_BDD = "ragot.db";
 
	private SQLiteDatabase bdd;
 
	private BddRagot maBddRagot;
 
	private List<Ragot> cible = null;
	
	public BddRagotManager(Context context){
		//On créer la BDD et sa table
		maBddRagot = new BddRagot(context, NOM_BDD, null, VERSION_BDD);
	}

	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBddRagot.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	public long insertRagot(Ragot ragot){
		//Création d'un ContentValues (fonctionne comme une HashMap)
		ContentValues values = new ContentValues();
		//on lui ajoute une valeur associé à une clé (qui est le nom de la colonne dans laquelle on veut mettre la valeur)
		values.put(BddRagot.COL_RAGOT, ragot.getRagot());
		values.put(BddRagot.COL_ID_POSTEUR, ragot.getIdPseudo());
		values.put(BddRagot.COL_PSEUDO_POSTEUR, ragot.getPseudo());
		values.put(BddRagot.COL_DATE, ragot.getDate());
		values.put(BddRagot.COL_HISTORY, ragot.getHistory());
		values.put(BddRagot.COL_FAVORIS, ragot.getFavoris());
		values.put(BddRagot.COL_SEPARATEUR, ragot.getSeparateur());
		//on insère l'objet dans la BDD via  le ContentValues
		ragot.setId((int)bdd.insert(BddRagot.TABLE_RAGOT, null, values));
		L.v("BddRagotManager", "insert : id : " + ragot.getId());
		return ragot.getId();
	}
 
	public int deleteRagot(int id) {
		return bdd.delete(BddRagot.TABLE_RAGOT, BddRagot.COL_ID + " = " + id, null);
	}
	
	public int updateRagot(int id, Ragot ragot){
		//La mise à jour d'un livre dans la BDD fonctionne plus ou moins comme une insertion
		//il faut simple préciser quell_WEBe livre on doit mettre à jour grâce à l'ID
		ContentValues values = new ContentValues();
		values.put(BddRagot.COL_ID, ragot.getId());
		values.put(BddRagot.COL_RAGOT, ragot.getRagot());
		values.put(BddRagot.COL_ID_POSTEUR, ragot.getIdPseudo());
		values.put(BddRagot.COL_PSEUDO_POSTEUR, ragot.getPseudo());
		values.put(BddRagot.COL_DATE, ragot.getDate());
		values.put(BddRagot.COL_HISTORY, ragot.getHistory());
		values.put(BddRagot.COL_FAVORIS, ragot.getFavoris());
		values.put(BddRagot.COL_SEPARATEUR, ragot.getSeparateur());
		return bdd.update(BddRagot.TABLE_RAGOT, values, BddRagot.COL_ID + " = " + id, null);
	}
 
	/*public int removeLivreWithID(int id){
		//Suppression d'un livre de la BDD grâce à l'ID
		return bdd.delete(BddProfil.TABLE_PROFIL, BddProfil.COL_ID + " = " + id, null);
	}*/
	
	public Ragot getRagot(int id){
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(BddRagot.TABLE_RAGOT, new String[] { 
				BddRagot.COL_ID,
				BddRagot.COL_RAGOT, 
				BddRagot.COL_ID_POSTEUR,
				BddRagot.COL_PSEUDO_POSTEUR,
				BddRagot.COL_DATE,
				BddRagot.COL_HISTORY,
				BddRagot.COL_FAVORIS,
				BddRagot.COL_SEPARATEUR}, BddRagot.COL_ID + " LIKE \"" + id +"\" ", null, null, null, null);
		return cursorToRagot(c);
	}
	
//	public List<Ragot> getAllRagot(){
//		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
//		Cursor c = bdd.query(BddRagot.TABLE_RAGOT, new String[] {
//				BddRagot.COL_ID,
//				BddRagot.COL_RAGOT, 
//				BddRagot.COL_ID_POSTEUR,
//				BddRagot.COL_PSEUDO_POSTEUR,
//				BddRagot.COL_DATE,
//				BddRagot.COL_HISTORY,
//				BddRagot.COL_FAVORIS}, " * ", null, null, null, null);
//		return cursorToListRagot(c);
//	}
	
	public Ragot synchroniseRagot(int idPseudo, int timestampRagot) {
		Cursor c = bdd.query(BddRagot.TABLE_RAGOT, new String[] { 
				BddRagot.COL_ID,
				BddRagot.COL_RAGOT, 
				BddRagot.COL_ID_POSTEUR,
				BddRagot.COL_PSEUDO_POSTEUR,
				BddRagot.COL_DATE,
				BddRagot.COL_HISTORY,
				BddRagot.COL_FAVORIS,
				BddRagot.COL_SEPARATEUR}, BddRagot.COL_ID_POSTEUR + " LIKE \"" + idPseudo +"\" AND " + BddRagot.COL_DATE + " LIKE \" " + timestampRagot + "\" ", null, null, null, null);
		return cursorToRagot(c);
	}
	
	public Ragot getLastRagot() {
		Cursor c = bdd.query(BddRagot.TABLE_RAGOT, new String[] { 
				BddRagot.COL_ID,
				BddRagot.COL_RAGOT, 
				BddRagot.COL_ID_POSTEUR,
				BddRagot.COL_PSEUDO_POSTEUR,
				BddRagot.COL_DATE,
				BddRagot.COL_HISTORY,
				BddRagot.COL_FAVORIS,
				BddRagot.COL_SEPARATEUR}, null, null, null, null, BddRagot.COL_ID + " DESC", "1");
		return cursorToRagot(c);
	}
	
	//Cette fonction permet d'incrémenter le chiffre de l'historique, chiffre utilisé lors de l'affichage pour afficher par groupe les ragots à l'aide d'une couleur de fond en dégradé :
//	public void updateHistory () {
//		List<Ragot> maListeDeRagot = new ArrayList<Ragot>();
//		maListeDeRagot = getAllRagot();
//		for (int i = 0; i < maListeDeRagot.size(); i++) {
//			maListeDeRagot.get(i).setHistory(maListeDeRagot.get(i).getHistory());
//			updateRagot(maListeDeRagot.get(i).getId(), maListeDeRagot.get(i));
//		}
//	}
	
	
	public List<Ragot> get50LastRagot() {
		Cursor c = bdd.query(BddRagot.TABLE_RAGOT, new String[] {
				BddRagot.COL_ID,
				BddRagot.COL_RAGOT, 
				BddRagot.COL_ID_POSTEUR,
				BddRagot.COL_PSEUDO_POSTEUR,
				BddRagot.COL_DATE,
				BddRagot.COL_HISTORY,
				BddRagot.COL_FAVORIS,
				BddRagot.COL_SEPARATEUR}, null, null, null, null, BddRagot.COL_ID + " DESC", "" + Root.NBR_RAGOT);//BddRagot.COL_ID + " DESC"
		return cursorToListRagot(c);
	}

	public List<Ragot> get50LastRagot(int skip) {
		Cursor c = bdd.query(BddRagot.TABLE_RAGOT, new String[] {
				BddRagot.COL_ID,
				BddRagot.COL_RAGOT, 
				BddRagot.COL_ID_POSTEUR,
				BddRagot.COL_PSEUDO_POSTEUR,
				BddRagot.COL_DATE,
				BddRagot.COL_HISTORY,
				BddRagot.COL_FAVORIS,
				BddRagot.COL_SEPARATEUR}, null, null, null, null, BddRagot.COL_ID + " DESC", "" + skip + "," + Root.NBR_RAGOT);//BddRagot.COL_ID + " DESC"
		return cursorToListRagot(c);
	}
	
	public List<Ragot> getOnlyFavoris() {
		Cursor c = bdd.query(BddRagot.TABLE_RAGOT, new String[] {
				BddRagot.COL_ID,
				BddRagot.COL_RAGOT, 
				BddRagot.COL_ID_POSTEUR,
				BddRagot.COL_PSEUDO_POSTEUR,
				BddRagot.COL_DATE,
				BddRagot.COL_HISTORY,
				BddRagot.COL_FAVORIS,
				BddRagot.COL_SEPARATEUR}, BddRagot.COL_FAVORIS + " LIKE \"1\"", null, null, null, BddRagot.COL_ID + " DESC");//BddRagot.COL_ID + " DESC"
		return cursorToListRagot(c);
	}	
	
	public void switchFavoris(int id) {
		for (int i = 0; i < cible.size(); i++)
			if (cible.get(i).getId() == id) {
				ContentValues values = new ContentValues();
				values.put(BddRagot.COL_ID, cible.get(i).getId());
				values.put(BddRagot.COL_RAGOT, cible.get(i).getRagot());
				values.put(BddRagot.COL_ID_POSTEUR, cible.get(i).getIdPseudo());
				values.put(BddRagot.COL_PSEUDO_POSTEUR, cible.get(i).getPseudo());
				values.put(BddRagot.COL_DATE, cible.get(i).getDate());
				values.put(BddRagot.COL_HISTORY, cible.get(i).getHistory());
				int newFavoris = cible.get(i).getFavoris();
				if (newFavoris == 0)
					newFavoris = 1;
				else
					newFavoris = 0;
				cible.get(i).setFavoris(newFavoris);
				values.put(BddRagot.COL_FAVORIS, newFavoris);
				values.put(BddRagot.COL_SEPARATEUR, cible.get(i).getSeparateur());
				bdd.update(BddRagot.TABLE_RAGOT, values, BddRagot.COL_ID + " = " + id, null);
				break;
			}
	}
	
	//Cette méthode permet de convertir un cursor en un livre
	private Ragot cursorToRagot(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		//Sinon on se place sur le premier élément
		c.moveToFirst();
		//On créé un livre
		Ragot ragot = new Ragot(c.getInt(BddRagot.NUM_COL_ID),
				c.getString(BddRagot.NUM_COL_RAGOT),
				c.getInt(BddRagot.NUM_COL_ID_POSTEUR),
				c.getString(BddRagot.NUM_COL_PSEUDO_POSTEUR),
				c.getLong(BddRagot.NUM_COL_DATE),
				c.getInt(BddRagot.NUM_COL_HISTORY),
				c.getInt(BddRagot.NUM_COL_FAVORIS),
				c.getInt(BddRagot.NUM_COL_SEPARATEUR));
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return ragot;
	}
	
	private List<Ragot> cursorToListRagot(Cursor c){
		//si aucun élément n'a été retourné dans la requête, on renvoie null
		if (c.getCount() == 0)
			return null;
 
		List<Ragot> maListeDeRagot = new ArrayList<Ragot>();
		//Sinon on se place sur le premier élément
		Ragot ragot = null;
		c.moveToFirst();
		do
		{
			//On créé un livre
			//on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
			ragot = new Ragot(c.getInt(BddRagot.NUM_COL_ID),
					c.getString(BddRagot.NUM_COL_RAGOT),
					c.getInt(BddRagot.NUM_COL_ID_POSTEUR),
					c.getString(BddRagot.NUM_COL_PSEUDO_POSTEUR),
					c.getLong(BddRagot.NUM_COL_DATE),
					c.getInt(BddRagot.NUM_COL_HISTORY),
					c.getInt(BddRagot.NUM_COL_FAVORIS),
					c.getInt(BddRagot.NUM_COL_SEPARATEUR));
			maListeDeRagot.add(ragot);
		}
		while (c.moveToNext());
		//On ferme le cursor
		c.close();
 
		//On retourne le livre
		return maListeDeRagot;
	}

	public List<Ragot> getCible() {
		return cible;
	}

	public void setCible(List<Ragot> cible) {
		this.cible = cible;
	}
	
//	public void censure() {
//		Cursor c = bdd.delete(BddRagot.TABLE_RAGOT, new String[] {
//				BddRagot.COL_ID,
//				BddRagot.COL_RAGOT, 
//				BddRagot.COL_ID_POSTEUR,
//				BddRagot.COL_PSEUDO_POSTEUR,
//				BddRagot.COL_DATE,
//				BddRagot.COL_HISTORY,
//				BddRagot.COL_FAVORIS}, null, null, null, null, BddRagot.COL_ID + " DESC", "" + Root.NBR_RAGOT);//BddRagot.COL_ID + " DESC"
//		return cursorToListRagot(c);
//		db.delete(TABLE_SIMPLETABLE_CLIENT1,KEY_EMPLOYEE_NAME + "='"+ContactName+"'",null);
//	}
}

