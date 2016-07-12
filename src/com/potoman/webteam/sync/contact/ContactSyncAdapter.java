package com.potoman.webteam.sync.contact;

import java.util.ArrayList;
import java.util.List;

import com.potoman.tools.L;
import com.potoman.webteam.bdd.contact.BddContact;
import com.potoman.webteam.bdd.contact.BddContactManager;
import com.potoman.webteam.eleve.ContactWebteam;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts.Data;
import android.provider.ContactsContract.RawContacts;

public class ContactSyncAdapter extends AbstractThreadedSyncAdapter {

	private final Context mContext;
	
	public ContactSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		mContext = context;
	}

	@Override
	public void onPerformSync(Account account, 
			Bundle extras, 
			String authority,
			ContentProviderClient provider, 
			SyncResult syncResult) {
		L.v("ContactSyncAdapter", "onPerformSync");
		L.v("ContactSyncAdapter", "account type = " + account.type);
		L.v("ContactSyncAdapter", "account name = " + account.name);
		L.v("ContactSyncAdapter", "provider = " + provider);
		Cursor c = getContext().getContentResolver().query(
				ContactProvider.CONTENT_URI, new String[] {
						//BddProfil.COL_ID, 
						BddContact.COL_PSEUDO, 
						BddContact.COL_ID, 		
						BddContact.COL_PRENOM, 
						BddContact.COL_NOM, 
						BddContact.COL_ADRESSE, 
						BddContact.COL_ADRESSE_PARENT, 
						BddContact.COL_URL_IMAGE, 
						BddContact.COL_SITE_INTERNET, 
						BddContact.COL_TELEPHONE, 
						BddContact.COL_TELEPHONE_FIXE, 
						BddContact.COL_TELEPHONE_PARENT, 
						BddContact.COL_SIGNATURE, 
						BddContact.COL_EMAIL, 
						BddContact.COL_CLASSE, 
						BddContact.COL_DATE_DE_NAISSANCE, 
						BddContact.COL_FAVORIS, 
						BddContact.COL_PSEUDO_SQUIRREL, 
						BddContact.COL_PASSWORD_SQUIRREL, 
						BddContact.COL_PSEUDO_CLUB_PHOTO, 
						BddContact.COL_PASSWORD_CLUB_PHOTO}, BddContact.COL_FAVORIS + " LIKE 1", null, null);
		
		
		List<ContactWebteam> listContactFavoris = BddContactManager.cursorToListProfil(c);
		


//		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		for (ContactWebteam contactFavoris : listContactFavoris) {
//			ArrayList<ContentProviderOperation> tmp = null;
//			tmp = ;
//			ops.addAll(tmp);
			try {
				mContext.getContentResolver().applyBatch(ContactsContract.AUTHORITY, add(account, contactFavoris));
			} catch (RemoteException e) {
				e.printStackTrace();
			} catch (OperationApplicationException e) {
				e.printStackTrace();
			}
//			ops.clear();
		}
		
		
	}
	

	// adding a contact.  note we are storing the id referenced in the response 
    // from the server in the SYNC1 field - this way we can find it with this
    // server based id    
    private ArrayList<ContentProviderOperation> add(Account account, ContactWebteam contact) {
        int rawid = generateId(account);
        L.v("", "id generated = " + rawid);
//        if (rawid != 0) {
//        	return null;
//        }
        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
//                .withValue(RawContacts.SOURCE_ID, 0)
//                .withValue(RawContacts.SYNC1, rawid)
                .withValue(RawContacts.ACCOUNT_TYPE, account.type)
                .withValue(RawContacts.ACCOUNT_NAME, account.name)
                .withValue(RawContacts.SOURCE_ID, "" + contact.getId())
                .build());
        
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
            .withValue(ContactsContract.Data.MIMETYPE, 
            		ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
            .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, contact.getPrenom())
            .build());
        
        ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, 
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.getTelephone())
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, 
                        ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                .build());
        
		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
				.withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
				.withValue(
						ContactsContract.Data.MIMETYPE,
						ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
				.withValue(ContactsContract.CommonDataKinds.Email.DATA, contact.getPrenom() + "." + contact.getNom() + "@ensea.fr")
				.withValue(ContactsContract.CommonDataKinds.Email.TYPE,
						ContactsContract.CommonDataKinds.Email.TYPE_WORK)
				.build());
		
		ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE, 
                		"vnd.android.cursor.item/vnd.com.oreilly.demo.pa.ch17.sync.profile")
//                  .withValue(ContactsContract.Data.DATA2, "Ch15 Profile")
				.withValue(ContactsContract.Data.DATA2, "Webteam Profile")
				.withValue(ContactsContract.Data.DATA3, "Voir le profil")
//                .withValue(ContactsContract.Data.DATA4, "FDS profile")
                .build()
                );
		
        return ops;
    }

 // look up the actual raw id via the id we have stored in the SYNC1 field
    private long lookupRawContact(Account account, long id) {
        long rawid = 0;
        Cursor c = mContext.getContentResolver().query(
                       RawContacts.CONTENT_URI, new String[] {RawContacts._ID},
                       RawContacts.ACCOUNT_TYPE + "='" + account.type + "' AND "+ 
                       RawContacts.SYNC1 + "=?", 
                       new String[] {String.valueOf(id)},
                       null);
        try {
            if(c.moveToFirst()) {
                rawid = c.getLong(0);
            }
        } finally {
            if (c != null) {
                c.close();
                c = null;
            }
        }
        return rawid;
    }
    
    private int generateId(Account account) {
        int rawid = 0;
        Cursor c = mContext.getContentResolver().query(
                       RawContacts.CONTENT_URI, new String[] {RawContacts._ID},
                       null, 
                       null,
                       null);
        try {
            if(c.moveToLast()) {
                rawid = c.getInt(0) + 1;
            }
        } finally {
            if (c != null) {
                c.close();
                c = null;
            }
        }
        return rawid;
    }
}
