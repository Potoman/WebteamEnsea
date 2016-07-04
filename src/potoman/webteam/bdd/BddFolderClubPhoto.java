package potoman.webteam.bdd;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class BddFolderClubPhoto extends SQLiteOpenHelper {
	public static final String TABLE_FOLDER_CLUB_PHOTO = "table_folder_club_photo";
	public static final String COL_CATEGORY = "category";
	public static final int NUM_COL_CATEGORY = 0;
	public static final String COL_CATEGORY_CONTAINER = "category_container";
	public static final int NUM_COL_CATEGORY_CONTAINER = 1;
	public static final String COL_TITLE = "title";
	public static final int NUM_COL_TITLE = 2;
	public static final String COL_COUNT_PHOTO_IN_FOLDER = "count_photo_in_folder";
	public static final int NUM_COL_COUNT_PHOTO_IN_FOLDER = 3;
	public static final String COL_COUNT_SUB_FOLDER = "count_sub_folder";
	public static final int NUM_COL_COUNT_SUB_FOLDER = 4;
	public static final String COL_COUNT_PHOTO_IN_SUB_FOLDER = "count_photo_in_sub_foler";
	public static final int NUM_COL_COUNT_PHOTO_IN_SUB_FOLDER = 5;
	public static final String COL_DEPTH = "depth";
	public static final int NUM_COL_DEPTH = 6;
	
	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_FOLDER_CLUB_PHOTO + " ("
	+ COL_CATEGORY + " INTEGER PRIMARY KEY, "
	+ COL_CATEGORY_CONTAINER + " INTEGER, "
	+ COL_TITLE + " STRING NOT NULL, "
	+ COL_COUNT_PHOTO_IN_FOLDER + " INTEGER NOT NULL, "
	+ COL_COUNT_SUB_FOLDER + " INTEGER NOT NULL, "
	+ COL_COUNT_PHOTO_IN_SUB_FOLDER + " INTEGER NOT NULL, "
	+ COL_DEPTH + " INTEGER NOT NULL);";
 
	public BddFolderClubPhoto(Context context, String name, CursorFactory factory, int version) {
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
		db.execSQL("DROP TABLE " + TABLE_FOLDER_CLUB_PHOTO + ";");
		onCreate(db);
	}
}
