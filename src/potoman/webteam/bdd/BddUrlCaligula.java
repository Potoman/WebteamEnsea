package potoman.webteam.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BddUrlCaligula extends SQLiteOpenHelper {
	public static final String TABLE_URL_CALIGULA = "table_url_caligula";
	public static final String COL_ID = "id";
	public static final int NUM_COL_ID = 0;
	public static final String COL_NAME = "name";
	public static final int NUM_COL_NAME = 1;
	public static final String COL_URL = "url";
	public static final int NUM_COL_URL = 2;
	public static final String COL_WHAT = "what";
	public static final int NUM_COL_WHAT = 3;
	
	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_URL_CALIGULA + " ("
	+ COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
	+ COL_NAME + " TEXT NOT NULL, "
	+ COL_URL + " TEXT NOT NULL, "
	+ COL_WHAT + " INTEGER NOT NULL);";
 
	public BddUrlCaligula(Context context, String name, CursorFactory factory, int version) {
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
		db.execSQL("DROP TABLE " + TABLE_URL_CALIGULA + ";");
		onCreate(db);
	}
}
