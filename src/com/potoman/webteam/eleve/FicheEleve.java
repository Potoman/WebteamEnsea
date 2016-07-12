

package com.potoman.webteam.eleve;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.example.webteam.R;
import org.json.JSONObject;

import com.potoman.tools.CallService;
import com.potoman.tools.DownloadImage;
import com.potoman.tools.L;
import com.potoman.webteam.bdd.contact.BddContactManager;
import com.potoman.webteam.constant.Webteam;
import com.potoman.webteam.exception.ExceptionService;
import com.potoman.webteam.sync.contact.ContactProvider;
import com.potoman.webteam.task.ATGetProfil;
import com.potoman.webteam.task.IWorkFinishOfAsyncTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class FicheEleve extends Activity implements OnItemClickListener, Observer, IWorkFinishOfAsyncTask { //, OnLongClickListener {

	private static final String[] projectionData = new String[] {ContactsContract.Data.RAW_CONTACT_ID};
	private static final String[] projectionRawContact = new String[] {ContactsContract.RawContacts.SOURCE_ID};
	
	private int idProfil = -1;
	private ContactWebteam mContactWebteam = null;
	
	private TextView prenomNom;

	private ImageView ivAvatarProfil;
	private ListView lvDefProfil;
	private ImageView ivFavoris;
	
	public List<String> ficheDetail;
	
	private Bitmap image;
	
//	private BddContactManager profilBdd;
	
	private ATGetProfil atGetProfil = null;
	
	private ProgressDialog myProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //setContentView(R.layout.ficheeleve);
	    this.setTitle("Webteam > Fiche élève");
        L.v("","Webteam > Fiche élève");
        L.v("","savedInstanceState > " + savedInstanceState);
	    Intent intent = getIntent();
        L.v("","savedInstanceState > " + intent.getData());
	    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		
//	    int idProfil = -1;
	    if (intent.hasExtra("idProfil")) {
	    	idProfil = intent.getIntExtra("idProfil", -1);
		}
		else {
			Cursor cursor = getContentResolver().query(intent.getData(), projectionData, null, null, null);
			cursor.moveToFirst();
			long rawContactId = cursor.getLong(0);
			cursor = getContentResolver().query(ContactsContract.RawContacts.CONTENT_URI, projectionRawContact, RawContacts._ID + " = " + rawContactId, null, null);
			cursor.moveToFirst();
			idProfil = Integer.parseInt(cursor.getString(0));
		}
	    if (idProfil == -1) {
	    	Toast.makeText(this, "Contact introuvable.", Toast.LENGTH_LONG).show();
	    	finish();
	    }
		mContactWebteam = ContactProvider.selectContactByIdWebteam(idProfil);
				
	    // Gestion de l'affichage :
	    if (isPortrait())  {
	    	setContentView(R.layout.fiche_eleve);
	    }
	    else {
	    	setContentView(R.layout.fiche_eleve_landscape);
	    }
		prenomNom = (TextView)findViewById(R.id.tvPrenomNom);
		ivAvatarProfil = (ImageView)findViewById(R.id.ivAvatar);
		ivFavoris = (ImageView)findViewById(R.id.ivFavoris);
		lvDefProfil = (ListView)findViewById(R.id.lvDefProfil);
		lvDefProfil.setOnItemClickListener(this);
	    
		getProfil();
		displayFicheEleve();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	public void getProfil() {
		atGetProfil = new ATGetProfil(this, idProfil);
		atGetProfil.execute(this);
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
    	return null;
    }

	void displayFicheEleve() {
		if (mContactWebteam == null) {
			return;
		}
		L.v("FicheEleve", "Afficher champs");
		prenomNom.setText(mContactWebteam.getPrenom() + " " + mContactWebteam.getNom().toUpperCase());
		String fileName = getFilesDir() + Webteam.FOLDER_PHOTO_ENSEARQUE + "/photo_ensearque_id_" + mContactWebteam.getId() + ".jpg";
		//Bitmap image;
		L.v("FicheEleve", "url Image = '" + fileName + "'");
		Bitmap tempImage = BitmapFactory.decodeFile(fileName);
		if (tempImage == null) {
   			L.v("FicheEleve", "tempImage = null, on télécharge l'image..." + getFilesDir());
			DownloadImage.downloadImage(this, mContactWebteam.getUrlImage());
		}
		else {
			L.v("FicheEleve", "L'image est bien dans un fichier.");
			if (image != null) {
				image.recycle();
			}
			image = tempImage;
			displayAvatar();
		}
			
		if (mContactWebteam.getHashMap() == null) {
			L.v("FicheEleve", "hashmap null");
		}
		SimpleAdapter mSchedule = new SimpleAdapter (this, mContactWebteam.getHashMap(), R.layout.itemfiche,
	               new String[] {"type", "valeur"}, new int[] {R.id.tvName, R.id.tvData});
	    // On attribut à notre listView l'adapter que l'on vient de créer
		lvDefProfil.setAdapter(mSchedule);
		if (mContactWebteam.isFavoris()) {
			ivFavoris.setVisibility(View.VISIBLE);
		}
		else {
			ivFavoris.setVisibility(View.GONE);
		}
	}

	public void resizeImage() {
		if (image != null) {
			int width = image.getWidth();
			int height = image.getHeight();
			//int newWidth = 200;
			//int newHeight = 200;
			float scaleWidth = ((float) (getWindowManager().getDefaultDisplay().getWidth() / 2)) / width;
			//float scaleHeight = ((float) newHeight) / height;
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleWidth);
			//matrix.postRotate(45);
			Bitmap imageTemp = image;
			image = Bitmap.createBitmap(imageTemp, 0, 0, width, height, matrix, true);
			imageTemp.recycle();
		}
	}
	
	public void displayAvatar() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ivAvatarProfil.setAnimation(null);
				ivAvatarProfil.setImageBitmap(image);
			}
		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		L.v("FicheEleve", "" + position);
		switch (position) {
		case 3: // Téléphone portable :
			try {
				final CharSequence[] items = {"Appel", "SMS"};

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Action sur ce numéro");
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	if (item == 0) {
				    		Intent callIntent = new Intent(Intent.ACTION_CALL);
					        callIntent.setData(Uri.parse("tel:" + mContactWebteam.getTelephone()));
					        startActivity(callIntent);
				    	}
				    	else {
				    		Intent callIntent = new Intent(getApplicationContext(),SendSMS.class);
					        callIntent.putExtra("numPhone", mContactWebteam.getTelephone());
					        callIntent.putExtra("nameDestinataire", mContactWebteam.getPseudo());
					        startActivity(callIntent);
				    	}
				    }
				});
				builder.create().show();
		    }
			catch (ActivityNotFoundException e) {
		    }
			break;
		case 5: // Téléphone fixe :
			try {
		        Intent callIntent = new Intent(Intent.ACTION_CALL);
		        callIntent.setData(Uri.parse("tel:" + mContactWebteam.getTelephoneFixe()));
		        startActivity(callIntent);
		    }
			catch (ActivityNotFoundException e) {
		    }
			break;
		case 7: // Téléphone parent :
			try {
		        Intent callIntent = new Intent(Intent.ACTION_CALL);
		        callIntent.setData(Uri.parse("tel:" + mContactWebteam.getTelephoneParent()));
		        startActivity(callIntent);
		    }
			catch (ActivityNotFoundException e) {
		    }
			break;
		case 10: // Site internet :
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mContactWebteam.getSiteInternet()));
			startActivity(intent);
			break;
		}
	}

	public boolean isPortrait() {
		return getWindow().getWindowManager().getDefaultDisplay().getHeight() > getWindow().getWindowManager().getDefaultDisplay().getWidth();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.fiche_eleve_, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_favoris:
			if (mContactWebteam.isFavoris()) {
				Toast.makeText(this, "Ce profil est déjà dans vos favoris.", Toast.LENGTH_SHORT).show();
			}
			else {
				mContactWebteam.setFavoris(true);
//				profilBdd.open();
//				profilBdd.updateProfil(profilEleve);
//				profilBdd.close();
				ContactProvider.updateContactWebteam(mContactWebteam);
				Toast.makeText(this, "Ce profil a bien été ajouté à vos favoris.", Toast.LENGTH_SHORT).show();	
				ivFavoris.setVisibility(View.VISIBLE);
			}
		return true;
		case R.id.item_favoris_supprimer:
			if (mContactWebteam.isFavoris()) {
				mContactWebteam.setFavoris(false);
//				profilBdd.open();
//				profilBdd.updateProfil(profilEleve);
//				profilBdd.close();
				ContactProvider.updateContactWebteam(mContactWebteam);
				Toast.makeText(this, "Ce profil a bien été supprimé de vos favoris.", Toast.LENGTH_SHORT).show();	
				ivFavoris.setVisibility(View.GONE);
			}
			else {
				Toast.makeText(this, "Ce profil n'est pas dans vos favoris.", Toast.LENGTH_SHORT).show();
			}
		return true;
		case R.id.item_favoris_actualiser:
			if (CallService.isConnected(this)) {
				ivAvatarProfil.setImageResource(org.example.webteam.R.drawable.ic_popup_sync_1);
				ivAvatarProfil.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_indefinitely));
				DownloadImage.downloadImage(this, mContactWebteam.getUrlImage());
			}
			else {
				Toast.makeText(this, getResources().getStringArray(R.array.exception)[ExceptionService.ID_ERROR_CONNECTED], Toast.LENGTH_SHORT).show();
			}
		return true;
		}
	return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void update(Observable observable, Object data) {
		image = (Bitmap) data;
		if (image != null) {
			resizeImage();
			try {
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
				if (prefs.getBoolean("photo_profil_saved", true)) {
					int quality = (10 - Integer.parseInt(prefs.getString("listPrefQuality", "0"))) * 10;
					
					File f = new File(getFilesDir().toString() + Webteam.FOLDER_WEBTEAM);
					if (!f.exists()) {
						f.mkdir();
						File fBis = new File(getFilesDir().toString() + Webteam.FOLDER_PHOTO_ENSEARQUE);
						fBis.mkdir();
					}
					
					String dir = getFilesDir().toString() + Webteam.FOLDER_PHOTO_ENSEARQUE;
					OutputStream fos = null;
					File file = new File(dir, "photo_ensearque_id_" + mContactWebteam.getId() + ".jpg");
					fos = new FileOutputStream(file);
					
					BufferedOutputStream bos = new BufferedOutputStream(fos);
					image.compress(Bitmap.CompressFormat.JPEG, quality, bos);
					bos.flush();
					bos.close();
				}
				displayAvatar();
			} catch (Exception e) {
				e.printStackTrace();
				L.v("FicheEleve", "exception ecriture" + getFilesDir() + " | " + Environment.getExternalStorageState());
			}
		}
		else {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(FicheEleve.this, "Impossible de télécharger l'image.", Toast.LENGTH_SHORT).show();
				}
			});
			
		}
		ivAvatarProfil.setAnimation(null);
	}

	@Override
	public void workFinish(int state) {
		if (myProgressDialog != null) {
   	    	myProgressDialog.dismiss();
		}
		else {
		    setProgressBarIndeterminateVisibility(false);
		}
		
		ContactWebteam tempProfil = null;
		try {
			tempProfil = atGetProfil.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		if (tempProfil == null) {
			if (mContactWebteam == null) {
				Toast.makeText(this, "Impossible de charger la fiche de l'élève.", Toast.LENGTH_LONG).show();
				finish();
			}
			return;
		}
		
		if (mContactWebteam == null) {
			// Aucun profil en bdd, on l'ajouter.
			
			ContactProvider.insertContactWebteam(tempProfil);
//			profilBdd.open();
//			profilBdd.insertProfil(tempProfil);
//			profilBdd.close();
		}
		else {
			// Il y a un profil en bdd, on va l'updater.
			tempProfil.setFavoris(mContactWebteam.isFavoris());
			ContactProvider.updateContactWebteam(tempProfil);
//			profilBdd.open();
//			profilBdd.updateProfil(tempProfil);
//			profilBdd.close();
			Toast.makeText(this, "Information sur l'élève mis à jour.", Toast.LENGTH_SHORT).show();
		}
		mContactWebteam = tempProfil;
		displayFicheEleve();
	}

	@Override
	public String getStringForWaiting() {
		return null;
	}

	@Override
	public int getNumberOfIncrementForMyProgressDialog() {
		return 0;
	}

	@Override
	public void incrementMyProgressDialog() {
	}
}

