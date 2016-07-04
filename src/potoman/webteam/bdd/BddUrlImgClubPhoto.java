package potoman.webteam.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class BddUrlImgClubPhoto extends SQLiteOpenHelper {
	public static final String TABLE_URL_IMG_CLUB_PHOTO = "table_folder_club_photo";
	public static final String COL_ID = "category";
	public static final int NUM_COL_ID = 0;
	public static final String COL_ID_FOLDER = "category_container";
	public static final int NUM_COL_ID_FOLDER = 1;
	public static final String COL_URL = "title";
	public static final int NUM_COL_URL = 2;
	
	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_URL_IMG_CLUB_PHOTO + " ("
	+ COL_ID + " INTEGER PRIMARY KEY, "
	+ COL_ID_FOLDER + " INTEGER NOT NULL, "
	+ COL_URL + " STRING NOT NULL);";
 
	public BddUrlImgClubPhoto(Context context, String name, CursorFactory factory, int version) {
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
		db.execSQL("DROP TABLE " + TABLE_URL_IMG_CLUB_PHOTO + ";");
		onCreate(db);
	}
}
