package potoman.webteam.bdd.contact;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BddContact extends SQLiteOpenHelper {
	public static final String TABLE_PROFIL = "table_profil";
	public static final String COL_PSEUDO = "pseudo";
	public static final int NUM_COL_PSEUDO = 0;
	public static final String COL_ID = "id";
	public static final int NUM_COL_ID = 1;
	public static final String COL_PRENOM = "prenom";
	public static final int NUM_COL_PRENOM = 2;
	public static final String COL_NOM = "nom";
	public static final int NUM_COL_NOM = 3;
	public static final String COL_ADRESSE = "adresse";
	public static final int NUM_COL_ADRESSE = 4;
	public static final String COL_ADRESSE_PARENT = "adresseParent";
	public static final int NUM_COL_ADRESSE_PARENT = 5;
	public static final String COL_URL_IMAGE = "urlImage";
	public static final int NUM_COL_URL_IMAGE = 6;
	public static final String COL_SITE_INTERNET = "siteInternet";
	public static final int NUM_COL_SITE_INTERNET = 7;
	public static final String COL_TELEPHONE = "telephone";
	public static final int NUM_COL_TELEPHONE = 8;
	public static final String COL_TELEPHONE_FIXE = "telephoneFixe";
	public static final int NUM_COL_TELEPHONE_FIXE = 9;
	public static final String COL_TELEPHONE_PARENT = "telephoneParent";
	public static final int NUM_COL_TELEPHONE_PARENT = 10;
	public static final String COL_SIGNATURE = "signature";
	public static final int NUM_COL_SIGNATURE = 11;
	public static final String COL_EMAIL = "email";
	public static final int NUM_COL_EMAIL = 12;
	public static final String COL_CLASSE = "classe";
	public static final int NUM_COL_CLASSE = 13;
	public static final String COL_DATE_DE_NAISSANCE = "dateDeNaissance";
	public static final int NUM_COL_DATE_DE_NAISSANCE = 14;
	public static final String COL_FAVORIS = "favoris"; // 0 : pas loggé; 1 : loggé
	public static final int NUM_COL_FAVORIS = 15;
	public static final String COL_PSEUDO_SQUIRREL = "pseudoSquirrel"; // 0 : pas loggé; 1 : loggé
	public static final int NUM_COL_PSEUDO_SQUIRREL = 16;
	public static final String COL_PASSWORD_SQUIRREL = "passwordSquirrel"; // 0 : pas loggé; 1 : loggé
	public static final int NUM_COL_PASSWORD_SQUIRREL = 17;
	public static final String COL_PSEUDO_CLUB_PHOTO = "pseudoClubPhoto"; // 0 : pas loggé; 1 : loggé
	public static final int NUM_COL_PSEUDO_CLUB_PHOTO = 18;
	public static final String COL_PASSWORD_CLUB_PHOTO = "passwordClubPhoto"; // 0 : pas loggé; 1 : loggé
	public static final int NUM_COL_PASSWORD_CLUB_PHOTO = 19;
	
	private static final String CREATE_BDD = "CREATE TABLE " + TABLE_PROFIL + " ("
	+ COL_PSEUDO + " TEXT NOT NULL, "
	+ COL_ID + " INTEGER NOT NULL, "
	+ COL_PRENOM + " TEXT NOT NULL, "
	+ COL_NOM + " TEXT NOT NULL, "
	+ COL_ADRESSE + " TEXT NOT NULL, "
	+ COL_ADRESSE_PARENT + " TEXT NOT NULL, "
	+ COL_URL_IMAGE + " TEXT NOT NULL, "
	+ COL_SITE_INTERNET + " TEXT NOT NULL, "
	+ COL_TELEPHONE + " TEXT NOT NULL, "
	+ COL_TELEPHONE_FIXE + " TEXT NOT NULL, "
	+ COL_TELEPHONE_PARENT + " TEXT NOT NULL, "
	+ COL_SIGNATURE + " TEXT NOT NULL, "
	+ COL_EMAIL + " TEXT NOT NULL, "
	+ COL_CLASSE + " TEXT NOT NULL, "
	+ COL_DATE_DE_NAISSANCE + " TEXT NOT NULL, "
	+ COL_FAVORIS + " INTEGER NOT NULL, "
	+ COL_PSEUDO_SQUIRREL + " TEXT NOT NULL, "
	+ COL_PASSWORD_SQUIRREL + " TEXT NOT NULL, "
	+ COL_PSEUDO_CLUB_PHOTO + " TEXT NOT NULL, "
	+ COL_PASSWORD_CLUB_PHOTO + " TEXT NOT NULL);";
 
	public BddContact(Context context, String name, CursorFactory factory, int version) {
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
		db.execSQL("DROP TABLE " + TABLE_PROFIL + ";");
		onCreate(db);
	}
}
