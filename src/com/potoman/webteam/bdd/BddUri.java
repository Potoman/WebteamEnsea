package com.potoman.webteam.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BddUri extends SQLiteOpenHelper {
	public static final String TABLE_URI = "table_uri";
	public static final String COL_ID = "id";
	public static final int NUM_COL_ID = 0;
	public static final String COL_URI = "uri";
	public static final int NUM_COL_URI = 1;
	/*public static final String COL_ID_NOTE = "note";
	public static final int NUM_COL_ID_NOTE = 2;
	public static final String COL_PSEUDO_POSTEUR = "idPosteur";
	public static final int NUM_COL_PSEUDO_POSTEUR = 3;*/
	
	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_URI + " ("
	+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
	+ COL_URI + " TEXT NOT NULL);";
 
	public BddUri(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
 
	@Override
	public void onCreate(SQLiteDatabase db) {
		//on créé la table à partir de la requête écrite dans la variable CREATE_BDD
		db.execSQL(CREATE_BDD);
	}
 
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//On peut fait ce qu'on veut ici moi j'ai décidé de supprimer la table et de la recréer
		//comme ça lorsque je change la version les id repartent de 0
		db.execSQL("DROP TABLE " + TABLE_URI + ";");
		onCreate(db);
	}
}

