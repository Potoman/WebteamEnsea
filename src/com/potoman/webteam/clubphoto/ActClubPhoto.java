package com.potoman.webteam.clubphoto;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.example.webteam.R;

import com.potoman.tools.CallService;
import com.potoman.tools.IObserver;
import com.potoman.tools.L;
import com.potoman.webteam.bdd.BddFolderClubPhotoManager;
import com.potoman.webteam.bdd.BddUrlImgClubPhotoManager;
import com.potoman.webteam.bdd.data.FolderClubPhoto;
import com.potoman.webteam.bdd.data.UrlImgClubPhoto;
import com.potoman.webteam.clubphoto.activity.ImageGridActivity;
import com.potoman.webteam.clubphoto.activity.Constants.Extra;
import com.potoman.webteam.constant.Webteam;
import com.potoman.webteam.ragot.Ragots;
import com.potoman.webteam.task.clubphoto.ATConnexionClubPhoto;
import com.potoman.webteam.task.clubphoto.ATGetAllCategoryClubPhoto;
import com.potoman.webteam.task.clubphoto.ATGetCategoryClubPhoto;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ActClubPhoto extends Activity implements IObserver, OnItemClickListener {

	private ListView lvFolder = null;
	private BddFolderClubPhotoManager bddCategory = null;
	private BddUrlImgClubPhotoManager bddUrlImg = null;

	private SharedPreferences preferences = null;

	private HttpClient httpClientLogged = null;

	private ATConnexionClubPhoto atConnexionClubPhoto = null;
	private ATGetAllCategoryClubPhoto atGetFolderClubPhoto = null;
	private ATGetCategoryClubPhoto atGetCategory = null;

	private FolderClubPhotoAdapter adapter = null;
	private ProgressDialog myProgressDialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.club_photo);

		httpClientLogged = new DefaultHttpClient();
		CallService.setParam(httpClientLogged, true);
		
		lvFolder = (ListView) findViewById(R.id.lvFolderClubPhoto);
		adapter = new FolderClubPhotoAdapter(getApplicationContext());
		lvFolder.setAdapter(adapter);
		lvFolder.setOnItemClickListener(this);
		

		preferences = PreferenceManager.getDefaultSharedPreferences(this);

		bddUrlImg = new BddUrlImgClubPhotoManager(this);
		
		bddCategory = new BddFolderClubPhotoManager(this);
		bddCategory.open();

		Map<Integer, FolderClubPhoto> mapFolder = bddCategory.getAllFolder();
		L.v("ActClubPhoto", "size folder in bdd = " + mapFolder.size());

		bddCategory.close();
		
		if (CallService.isConnected(this)) {
			if (mapFolder.isEmpty()) {
				getFolder();
			}
			else {
				adapter.putAll(mapFolder);
			}
		}
		else {
			Toast.makeText(
					this,
					"Veuillez être connecté à internet pour avoir accès au club photo.",
					Toast.LENGTH_LONG).show();
			finish();
		}
	}

    //Touche "menu"
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.main_club_photo, menu);
    	return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_main_club_photo_refresh:
			if (CallService.isConnected(this)) {
				bddCategory.open();
				bddCategory.clear();
				bddCategory.close();
				
				bddUrlImg.open();
				bddUrlImg.clear();
				bddUrlImg.close();
				
				adapter.clear();
				
				getFolder();
			}
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	private void getFolder() {
		myProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.waiting_load_all_category_club_photo), Webteam.getPhraseDAmbiance(), true, true, new OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				finish();
			}
		});
		preferences.getString(Webteam.PASSWORD_CLUB_PHOTO, "");
		
		atConnexionClubPhoto = new ATConnexionClubPhoto(this,
				preferences.getString(Webteam.PSEUDO_CLUB_PHOTO, null),
				preferences.getString(Webteam.PASSWORD_CLUB_PHOTO, null),
				httpClientLogged);
		atConnexionClubPhoto.execute();
	}

	@Override
	public void update(Object observable, Object data) {
		if (observable == atConnexionClubPhoto) {
			atGetFolderClubPhoto = new ATGetAllCategoryClubPhoto(this, httpClientLogged);
			atGetFolderClubPhoto.execute();
		}
		else if (observable == atGetFolderClubPhoto) {
			myProgressDialog.dismiss();
			boolean isLoad = false;
			try {
				isLoad = atGetFolderClubPhoto.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			if (!isLoad) {
				L.e("AClubPhoto", "update - load raté");				
				return;
			}
			else {
				Map<Integer, FolderClubPhoto> mapFolder = (Map<Integer, FolderClubPhoto>) data;
				
				bddCategory.open();

				bddCategory.insertFolder(mapFolder.values());

				bddCategory.close();
				adapter.putAll(mapFolder);
			}
		}
		else if (observable == atGetCategory) {
			myProgressDialog.dismiss();
			boolean isLoad = false;
			try {
				isLoad = atGetCategory.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			if (isLoad) {
				List<UrlImgClubPhoto> listUrlImg = (List<UrlImgClubPhoto>) data;
				
				bddUrlImg.open();
				bddUrlImg.insertUrlImg(listUrlImg);
				bddUrlImg.close();
				
				launchGridView(listUrlImg);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		L.v("AClubPhoto", "CLICK");
		FolderClubPhoto folderClicked = adapter.getItem(position);
		if (adapter.isOpen(position)) {
			adapter.closeFolder(position);
		}
		else {
			adapter.openFolder(folderClicked.getCategory());
			L.v("AClubPhoto", "folderClicked = " + folderClicked.getCategory());
		}
		if (folderClicked.getCountPhotoInFolder() != 0) {
			if (atGetCategory != null) {
				atGetCategory.cancel(true);
			}
			
			bddUrlImg.open();
			List<UrlImgClubPhoto> listUrlImg = bddUrlImg.getFolder(folderClicked.getCategory());
			bddUrlImg.close();
			
			if (listUrlImg.isEmpty()) {
				myProgressDialog = ProgressDialog.show(this, getResources().getString(R.string.waiting_load_url_from_category_club_photo), Webteam.getPhraseDAmbiance(), true, true, new OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
//						finish();
					}
				});
				
				atGetCategory = new ATGetCategoryClubPhoto(this, httpClientLogged, folderClicked, 
						preferences.getString(Webteam.PSEUDO_CLUB_PHOTO, null),
						preferences.getString(Webteam.PASSWORD_CLUB_PHOTO, null));
				atGetCategory.execute();
			}
			else {
				launchGridView(listUrlImg);
			}
		}
	}
	
	private void launchGridView(List<UrlImgClubPhoto> listUrlImg) {
		Intent intent = new Intent(this, ImageGridActivity.class);
		String[] tabUrl = new String[listUrlImg.size()];
		int index = 0;
		for (UrlImgClubPhoto urlImg : listUrlImg) {
			tabUrl[index] = urlImg.getUrlImg();
			index++;
		}
		intent.putExtra(Extra.IMAGES, tabUrl);
		startActivity(intent);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	if (atConnexionClubPhoto != null) {
	    		atConnexionClubPhoto.cancel(true);
	    	}
	    	if (atGetFolderClubPhoto != null) {
	    		atGetFolderClubPhoto.cancel(true);
	    	}
	    	if (atGetCategory != null) {
	    		atGetCategory.cancel(true);
	    	}
	    }
	    return super.onKeyDown(keyCode, event);
	}
}
