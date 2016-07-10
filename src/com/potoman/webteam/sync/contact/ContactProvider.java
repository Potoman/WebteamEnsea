package org.example.webteam.sync.contact;

import java.util.List;

import org.example.webteam.eleve.ContactWebteam;

import potoman.tools.L;
import potoman.webteam.bdd.contact.BddContact;
import potoman.webteam.bdd.contact.BddContactManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class ContactProvider extends ContentProvider {
	
	private BddContactManager bdd = null;
	
	public static final Uri CONTENT_URI = Uri.parse("content://org.example.webteam.sync.contact");
	// Le Mime de notre content provider, la premiére partie est toujours identique 
	public static final String CONTENT_PROVIDER_MIME = "vnd.android.cursor.item/vnd.webteam.content.provider.contact";

	
	
	@Override
	public int delete(Uri arg0, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return CONTENT_PROVIDER_MIME;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		return null;
	}

	private static ContactProvider instance = null;
	
	@Override
	public boolean onCreate() {
		L.v("ContactProvider", "onCreate, this = " + this);
		bdd = new BddContactManager(getContext());
		instance = this;
		return false;
	}
	
	public static ContactWebteam selectContactByIdWebteam(int idWebteam) {
		instance.bdd.open();
		ContactWebteam tmp = instance.bdd.getContactWebteam(idWebteam);
		instance.bdd.close();
		return tmp;
	}

	public static ContactWebteam selectContactByDataUri(Uri data) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		L.v("ContactProvider", "query, this = " + this);
		L.v("ContactProvider", "uri = " + uri);
		L.v("ContactProvider", "selection = " + selection);
		SQLiteDatabase sql = bdd.getOpenHelper().getReadableDatabase();
		return sql.query(BddContact.TABLE_PROFIL, projection, selection, selectionArgs, null, null, null);
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		L.v("ContactProvider", "update");
		L.v("ContactProvider", "uri = " + uri);
		L.v("ContactProvider", "selection = " + selection);
		return 0;
	}

	public static void updateContactWebteam(ContactWebteam mContactWebteam) {
		instance.bdd.open();
		instance.bdd.updateProfil(mContactWebteam);
		instance.bdd.close();
	}

	public static void insertContactWebteam(ContactWebteam mContactWebteam) {
		instance.bdd.open();
		instance.bdd.insertProfil(mContactWebteam);
		instance.bdd.close();
	}

	public static List<ContactWebteam> selectAllFavorisContact() {
		instance
		.bdd
		.open();
		List<ContactWebteam> tmp = instance.bdd.getProfilFavoris();
		instance.bdd.close();
		return tmp;
	}
	
}
