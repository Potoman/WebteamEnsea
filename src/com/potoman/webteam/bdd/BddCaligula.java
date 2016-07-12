package com.potoman.webteam.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BddCaligula extends SQLiteOpenHelper {
	public static final String TABLE_CALIGULA = "table_caligula";
	public static final String COL_ID = "id";
	public static final int NUM_COL_ID = 0;
	public static final String COL_DATE = "date";
	public static final int NUM_COL_DATE = 1;
	public static final String COL_HEURE = "heure";
	public static final int NUM_COL_HEURE = 2;
	public static final String COL_DUREE = "duree";
	public static final int NUM_COL_DUREE = 3;
	public static final String COL_ACTIVITE = "activite";
	public static final int NUM_COL_ACTIVITE = 4;
	public static final String COL_STAGIAIRE = "stagiaire";
	public static final int NUM_COL_STAGIAIRE = 5;
	public static final String COL_FORMATEUR = "formateur";
	public static final int NUM_COL_FORMATEUR = 6;
	public static final String COL_SALLE = "salle";
	public static final int NUM_COL_SALLE = 7;
	
	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_CALIGULA + " ("
	+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
	+ COL_DATE + " TEXT NOT NULL, "
	+ COL_HEURE + " TEXT NOT NULL, "
	+ COL_DUREE + " TEXT NOT NULL, "
	+ COL_ACTIVITE + " TEXT NOT NULL, "
	+ COL_STAGIAIRE + " INTEGER NOT NULL, "
	+ COL_FORMATEUR + " TEXT NOT NULL, "
	+ COL_SALLE + " TEXT NOT NULL);";
 
	public BddCaligula(Context context, String name, CursorFactory factory, int version) {
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
		db.execSQL("DROP TABLE " + TABLE_CALIGULA + ";");
		onCreate(db);
	}
}
