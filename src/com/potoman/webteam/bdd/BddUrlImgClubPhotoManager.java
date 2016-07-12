package com.potoman.webteam.bdd;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.potoman.webteam.bdd.data.FolderClubPhoto;
import com.potoman.webteam.bdd.data.UrlImgClubPhoto;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BddUrlImgClubPhotoManager {

	private static final int VERSION_BDD = 1;
	private static final String NOM_BDD = "url_img_club_photo.db";
 
	private SQLiteDatabase bdd;
 
	private BddUrlImgClubPhoto maBddUrlImgClubPhoto;
	
	public BddUrlImgClubPhotoManager(Context context){
		//On créer la BDD et sa table
		maBddUrlImgClubPhoto = new BddUrlImgClubPhoto(context, NOM_BDD, null, VERSION_BDD);
	}

	public void open(){
		//on ouvre la BDD en écriture
		bdd = maBddUrlImgClubPhoto.getWritableDatabase();
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
	public void insertUrlImg(List<UrlImgClubPhoto> listUrlImg){
		for (UrlImgClubPhoto urlImg : listUrlImg) {
			ContentValues values = new ContentValues();
			values.put(BddUrlImgClubPhoto.COL_ID, urlImg.getId());
			values.put(BddUrlImgClubPhoto.COL_ID_FOLDER, urlImg.getCategory());
			values.put(BddUrlImgClubPhoto.COL_URL, urlImg.getUrlImg());
			bdd.insert(BddUrlImgClubPhoto.TABLE_URL_IMG_CLUB_PHOTO, null, values);
		}
	}
	
	public List<UrlImgClubPhoto> getFolder(int category) {
		//Récupère dans un Cursor les valeur correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
		Cursor c = bdd.query(BddUrlImgClubPhoto.TABLE_URL_IMG_CLUB_PHOTO, new String[] {
				BddUrlImgClubPhoto.COL_ID, 
				BddUrlImgClubPhoto.COL_ID_FOLDER, 
				BddUrlImgClubPhoto.COL_URL}, BddUrlImgClubPhoto.COL_ID_FOLDER + " = " + category, null, null, null, null);
		return cursorToListUrlImgClubPhoto(c);
	}
	
	private List<UrlImgClubPhoto> cursorToListUrlImgClubPhoto(Cursor c){
		if (c.getCount() == 0) {
			return new ArrayList<UrlImgClubPhoto>();
		}
 
		List<UrlImgClubPhoto> listUrlImgClubPhoto = new ArrayList<UrlImgClubPhoto>();
		c.moveToFirst();
		do {
			UrlImgClubPhoto urlImg = new UrlImgClubPhoto(c.getInt(BddUrlImgClubPhoto.NUM_COL_ID),
					c.getInt(BddUrlImgClubPhoto.NUM_COL_ID_FOLDER),
					c.getString(BddUrlImgClubPhoto.NUM_COL_URL));
			listUrlImgClubPhoto.add(urlImg);
		}
		while (c.moveToNext());
		c.close();
 
		return listUrlImgClubPhoto;
	}

	public void clear() {
		bdd.delete(BddUrlImgClubPhoto.TABLE_URL_IMG_CLUB_PHOTO, null, null);
	}
}
