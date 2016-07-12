package com.potoman.webteam.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class BddSquirrel extends SQLiteOpenHelper {

	public static final String TABLE_SQUIRREL = "table_squirrel";
	public static final String COL_ID = "id";
	public static final int NUM_COL_ID = 0;
	public static final String COL_EMAIL_FROM = "id_from";
	public static final int NUM_COL_EMAIL_FROM = 1;
	public static final String COL_EMAIL_TO = "id_to";
	public static final int NUM_COL_EMAIL_TO = 2;
	public static final String COL_TITRE = "titre";
	public static final int NUM_COL_TITRE = 3;
	public static final String COL_DATA = "data";
	public static final int NUM_COL_DATA = 4;
	public static final String COL_TIME = "time";
	public static final int NUM_COL_TIME = 5;
	public static final String COL_REPONSE = "reponse";
	public static final int NUM_COL_REPONSE = 6;
	public static final String COL_CONTENU_LOAD = "contenu_load";
	public static final int NUM_COL_CONTENU_LOAD = 7;
	public static final String COL_ETAT_BOITE = "etat_boite";
	public static final int NUM_COL_ETAT_BOITE = 8;
	public static final String COL_NOM_BOITE = "nom_boite";
	public static final int NUM_COL_NOM_BOITE = 9;
	public static final String COL_PROPRIO = "proprio";
	public static final int NUM_COL_PROPRIO = 10;
	
	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_SQUIRREL + " ("
	+ COL_ID + " INTEGER NOT NULL, "
	+ COL_EMAIL_FROM + " TEXT NOT NULL, "
	+ COL_EMAIL_TO + " TEXT NOT NULL, "
	+ COL_TITRE + " TEXT NOT NULL, "
	+ COL_DATA + " TEXT NOT NULL, "
	+ COL_TIME + " TEXT NOT NULL, "
	+ COL_REPONSE + " INTEGER NOT NULL, "
	+ COL_CONTENU_LOAD + " INTEGER NOT NULL, "
	+ COL_ETAT_BOITE + " INTEGER NOT NULL, "
	+ COL_NOM_BOITE + " TEXT NOT NULL, "
	+ COL_PROPRIO + " INTEGER NOT NULL);";

	public BddSquirrel(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
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
		db.execSQL("DROP TABLE " + TABLE_SQUIRREL + ";");
		onCreate(db);
	}
}
