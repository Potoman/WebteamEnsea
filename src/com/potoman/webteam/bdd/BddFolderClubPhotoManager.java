package com.potoman.webteam.bdd;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.potoman.webteam.bdd.data.FolderClubPhoto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BddFolderClubPhotoManager {

	private static final int VERSION_BDD = 5;
	private static final String NOM_BDD = "folder_club_photo.db";
 
	private SQLiteDatabase bdd;
 
	private BddFolderClubPhoto maBddFolderClubPhoto;
	
	public BddFolderClubPhotoManager(Context context){
		//On créer la BDD et sa table
		maBddFolderClubPhoto = new BddFolderClubPhoto(context, NOM_BDD, null, VERSION_BDD);
	}

	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBddFolderClubPhoto.getWritableDatabase();
	}
 
	public void close(){
		//on ferme l'accès à la BDD
		bdd.close();
	}
 
	public SQLiteDatabase getBDD(){
		return bdd;
	}
 
	/*
	 * Le path doit commencer par un "/Albums".
	 */
	public void insertFolder(Collection<FolderClubPhoto> colFolder){
		for (FolderClubPhoto folder : colFolder) {
			ContentValues values = new ContentValues();
			values.put(BddFolderClubPhoto.COL_CATEGORY, folder.getCategory());
			Integer catCont = folder.getCategoryContainer();
			values.put(BddFolderClubPhoto.COL_CATEGORY_CONTAINER, catCont == null ? -1 : catCont);
			values.put(BddFolderClubPhoto.COL_TITLE, folder.getTitle());
			values.put(BddFolderClubPhoto.COL_COUNT_PHOTO_IN_FOLDER, folder.getCountPhotoInFolder());
			values.put(BddFolderClubPhoto.COL_COUNT_SUB_FOLDER, folder.getCountSubFolder());
			values.put(BddFolderClubPhoto.COL_COUNT_PHOTO_IN_SUB_FOLDER, folder.getCountPhotoInSubFolder());
			values.put(BddFolderClubPhoto.COL_DEPTH, folder.getDepth());
			bdd.insert(BddFolderClubPhoto.TABLE_FOLDER_CLUB_PHOTO, null, values);
		}
	}
	
	public Map<Integer, FolderClubPhoto> getAllFolder() {
		Cursor c = bdd.query(BddFolderClubPhoto.TABLE_FOLDER_CLUB_PHOTO, new String[] {
				BddFolderClubPhoto.COL_CATEGORY, 
				BddFolderClubPhoto.COL_CATEGORY_CONTAINER,
				BddFolderClubPhoto.COL_TITLE, 
				BddFolderClubPhoto.COL_COUNT_PHOTO_IN_FOLDER, 
				BddFolderClubPhoto.COL_COUNT_SUB_FOLDER, 
				BddFolderClubPhoto.COL_COUNT_PHOTO_IN_SUB_FOLDER, 
				BddFolderClubPhoto.COL_DEPTH}, null, null, null, null, null);
		return cursorToMapFolderClubPhoto(c);
	}
	
	public FolderClubPhoto getFolder(int category) {
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(BddFolderClubPhoto.TABLE_FOLDER_CLUB_PHOTO, new String[] {
				BddFolderClubPhoto.COL_CATEGORY, 
				BddFolderClubPhoto.COL_CATEGORY_CONTAINER, 
				BddFolderClubPhoto.COL_TITLE, 
				BddFolderClubPhoto.COL_COUNT_PHOTO_IN_FOLDER, 
				BddFolderClubPhoto.COL_COUNT_SUB_FOLDER, 
				BddFolderClubPhoto.COL_COUNT_PHOTO_IN_SUB_FOLDER, 
				BddFolderClubPhoto.COL_DEPTH}, BddFolderClubPhoto.COL_CATEGORY + " = " + category, null, null, null, null);
		return cursorFolderClubPhoto(c);
	}

	/*
	 * Retourne le contenu d'un dossier :
	 */
	public Map<Integer, FolderClubPhoto> getChildFolder(Integer category) {
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(BddFolderClubPhoto.TABLE_FOLDER_CLUB_PHOTO, new String[] {
				BddFolderClubPhoto.COL_CATEGORY, 
				BddFolderClubPhoto.COL_CATEGORY_CONTAINER,
				BddFolderClubPhoto.COL_TITLE, 
				BddFolderClubPhoto.COL_COUNT_PHOTO_IN_FOLDER, 
				BddFolderClubPhoto.COL_COUNT_SUB_FOLDER, 
				BddFolderClubPhoto.COL_COUNT_PHOTO_IN_SUB_FOLDER, 
				BddFolderClubPhoto.COL_DEPTH}, BddFolderClubPhoto.COL_CATEGORY_CONTAINER + " = " + category, null, null, null, null);
		return cursorToMapFolderClubPhoto(c);
	}
	
	private FolderClubPhoto cursorFolderClubPhoto(Cursor c) {
		if (c.getCount() == 0) {
			return null;
		}

		c.moveToFirst();
		FolderClubPhoto folder = new FolderClubPhoto(c.getInt(BddFolderClubPhoto.NUM_COL_CATEGORY),
				c.getInt(BddFolderClubPhoto.NUM_COL_CATEGORY_CONTAINER),
				c.getString(BddFolderClubPhoto.NUM_COL_TITLE),
				c.getInt(BddFolderClubPhoto.NUM_COL_COUNT_PHOTO_IN_FOLDER),
				c.getInt(BddFolderClubPhoto.NUM_COL_COUNT_SUB_FOLDER),
				c.getInt(BddFolderClubPhoto.NUM_COL_COUNT_PHOTO_IN_SUB_FOLDER),
				c.getInt(BddFolderClubPhoto.NUM_COL_DEPTH));
		c.close();
 
		return folder;
	}
	
	private Map<Integer, FolderClubPhoto> cursorToMapFolderClubPhoto(Cursor c){
		if (c.getCount() == 0) {
			return new HashMap<Integer, FolderClubPhoto>();
		}
 
		Map<Integer, FolderClubPhoto> mapFolderClubPhoto = new HashMap<Integer, FolderClubPhoto>();
		FolderClubPhoto folder = null;
		c.moveToFirst();
		do {
			int catCont = c.getInt(BddFolderClubPhoto.NUM_COL_CATEGORY_CONTAINER);
			folder = new FolderClubPhoto(c.getInt(BddFolderClubPhoto.NUM_COL_CATEGORY),
					catCont == -1 ? null : catCont,
					c.getString(BddFolderClubPhoto.NUM_COL_TITLE),
					c.getInt(BddFolderClubPhoto.NUM_COL_COUNT_PHOTO_IN_FOLDER),
					c.getInt(BddFolderClubPhoto.NUM_COL_COUNT_SUB_FOLDER),
					c.getInt(BddFolderClubPhoto.NUM_COL_COUNT_PHOTO_IN_SUB_FOLDER),
					c.getInt(BddFolderClubPhoto.NUM_COL_DEPTH));
			mapFolderClubPhoto.put(folder.getCategory(), folder);
		}
		while (c.moveToNext());
		c.close();
 
		return mapFolderClubPhoto;
	}

	public void clear() {
		bdd.delete(BddFolderClubPhoto.TABLE_FOLDER_CLUB_PHOTO, null, null);
	}
}
