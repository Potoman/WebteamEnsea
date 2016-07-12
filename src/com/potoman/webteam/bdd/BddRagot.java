package com.potoman.webteam.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BddRagot extends SQLiteOpenHelper {
	public static final String TABLE_RAGOT = "table_ragot";
	public static final String COL_ID = "id";
	public static final int NUM_COL_ID = 0;
	public static final String COL_RAGOT = "ragot";
	public static final int NUM_COL_RAGOT = 1;
	public static final String COL_ID_POSTEUR = "idPosteur";
	public static final int NUM_COL_ID_POSTEUR = 2;
	public static final String COL_PSEUDO_POSTEUR = "pseudo";
	public static final int NUM_COL_PSEUDO_POSTEUR = 3;
	public static final String COL_DATE = "date";
	public static final int NUM_COL_DATE = 4;
	public static final String COL_HISTORY = "history";
	public static final int NUM_COL_HISTORY = 5;
	public static final String COL_FAVORIS = "favoris";
	public static final int NUM_COL_FAVORIS = 6;
	public static final String COL_SEPARATEUR = "separateur";
	public static final int NUM_COL_SEPARATEUR = 7;
	
	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_RAGOT + " ("
	+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
	+ COL_RAGOT + " TEXT NOT NULL, "
	+ COL_ID_POSTEUR + " INTEGER NOT NULL, "
	+ COL_PSEUDO_POSTEUR + " TEXT NOT NULL, "
	+ COL_DATE + " BIGINT NOT NULL, "
	+ COL_HISTORY + " INTEGER NOT NULL, "
	+ COL_FAVORIS + " INTEGER NOT NULL, "
	+ COL_SEPARATEUR + " INTEGER NOT NULL);";
 
	public BddRagot(Context context, String name, CursorFactory factory, int version) {
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
		db.execSQL("DROP TABLE " + TABLE_RAGOT + ";");
		onCreate(db);
	}
}
