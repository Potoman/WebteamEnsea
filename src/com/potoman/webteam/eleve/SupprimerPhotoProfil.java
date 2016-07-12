package com.potoman.webteam.eleve;

import java.io.File;

import org.example.webteam.R;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;
import org.example.webteam.R.menu;

import com.potoman.webteam.constant.Webteam;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class SupprimerPhotoProfil extends Activity{

	PhotoProfilAdapter adapter = null;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    this.setTitle("Webteam > Gérer les photos");
	    setContentView(R.layout.supprimer_photo_profil);
	    adapter = new PhotoProfilAdapter(this, getFilesDir() + Webteam.FOLDER_PHOTO_ENSEARQUE);
	    GridView gridview = (GridView) findViewById(R.id.gvPhotoProfil);
	    gridview.setAdapter(adapter);

	    gridview.setOnItemClickListener(new OnItemClickListener() {
	        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	            //Toast.makeText(SupprimerPhotoProfil.this, "" + position, Toast.LENGTH_SHORT).show();
	            Intent monIntent = new Intent(SupprimerPhotoProfil.this, FicheEleve.class);
				Bundle variableDePassage = new Bundle();
				File f = new File(getFilesDir() + Webteam.FOLDER_PHOTO_ENSEARQUE);
				variableDePassage.putInt("idProfil", Integer.parseInt(f.list()[position].substring(f.list()[position].lastIndexOf("_") + 1, f.list()[position].length() - 4)));
			    monIntent.putExtras(variableDePassage); 
				startActivity(monIntent);
	        }
	    });
	}
	
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.supprimer_photo_profil_, menu);
	return true;
	}
    
    @Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_supprimer_photo_profil_supprimer:
			File f = new File(getFilesDir() + Webteam.FOLDER_PHOTO_ENSEARQUE);
	    	for (int i = f.list().length - 1; i >= 0 ; i--) {
	    		File toDelete = new File(getFilesDir() + Webteam.FOLDER_PHOTO_ENSEARQUE + "/" + f.list()[i]);
	    		toDelete.delete();
	    	}
	    	adapter.notifyDataSetChanged();
		return true;
		}
	return super.onMenuItemSelected(featureId, item);
	}
}
