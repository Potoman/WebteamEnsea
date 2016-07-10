package org.example.webteam.favoris;

import java.util.ArrayList;
import java.util.List;

import org.example.webteam.R;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;
import org.example.webteam.eleve.FicheEleve;
import org.example.webteam.ragot.Ragot;
import org.example.webteam.ragot.RagotAdapter;
import org.example.webteam.ragot.RagotManager;

import potoman.tools.L;
import potoman.webteam.bdd.BddRagotManager;
import potoman.webteam.bdd.BddUriManager;
import potoman.webteam.constant.Webteam;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

public class FavorisRagot extends RagotManager {
	
    Activity saveActivityForProgressBar = this;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //requestWindowFeature(Window.FEATURE_PROGRESS);
	    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	    setContentView(R.layout.favoris_ragot);
	    this.setTitle("Webteam > Favoris > Ragot");
	    
	    //setProgressBarVisibility(true);
	    //setProgressBarIndeterminate(true);
	    
	    L.v("Ragots", "onCreate");
	    
		registerForContextMenu((ListView)findViewById(R.id.lvFavorisRagot));
		
		//InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		//inputMethodManager.hideSoftInputFromWindow(ragotAPoster.getWindowToken(), 0);
		
		cible = Webteam.maListeDeFavorisRagot;
		
		ragotBdd = new BddRagotManager(this);
		ragotBdd.setCible(cible);
		ragotBdd.open();
		
		uriBdd = new BddUriManager(this);
		uriBdd.open();
		
		if (cible == null) {
			L.v("FavorisRagot", "Pointeur null au début.");
			cible = new ArrayList<Ragot>();
		}
		
		cible.clear();
		getFavorisRagot();
		//remplirListRagot();
		if (cible.size() != 0) {
			actualiseJustDate();
			afficherRagots();
		}
		else {
			Toast.makeText(this, "Il n'y a pas de ragot dans vos favoris.", Toast.LENGTH_LONG).show();
			//finish();
		}
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ragotBdd.close();
		//Toast.makeText(this, "BOLT !", Toast.LENGTH_SHORT).show();
	}
	
	public boolean isPortrait() {
		if (getWindow().getWindowManager().getDefaultDisplay().getHeight() > getWindow().getWindowManager().getDefaultDisplay().getWidth())
			return true;
		else
			return false;
	}
	
	public void afficherRagots() {
		L.v("FavorisRagot", "Top");
		myList = (ListView)findViewById(R.id.lvFavorisRagot);
	    adapter = new RagotAdapter(getApplicationContext(), cible, null, isPortrait());//, maCheckBox);
	    myList.setAdapter(adapter);
	    myList.setOnItemClickListener(this);
	}
	
	private void getFavorisRagot() {
		//Webteam.maListeDeRagot.clear();
		List<Ragot> toAdd = ragotBdd.getOnlyFavoris();
		if (toAdd != null) {
			cible.addAll(toAdd);
		}
	}
	
	@Override
    public boolean onContextItemSelected(MenuItem item)
    {
		switch (item.getGroupId()) {
		case 0:
			Intent monIntent = new Intent(this,FicheEleve.class);
			Bundle variableDePassage = new Bundle();
			variableDePassage.putInt("idProfil", item.getItemId());
		    monIntent.putExtras(variableDePassage); 
			startActivity(monIntent);
			break;
		case 1:
			L.v("Ragots", "onContextItemSelected : " + item.getItemId());
			ragotBdd.switchFavoris(item.getItemId());
			int i = 0;
			for (i = 0; i < cible.size(); i++)
				if (cible.get(i).getId() == item.getItemId())
					break;
			cible.remove(i);
			Webteam.maListeDeRagot.clear();	//Un peu moche, mais permet la resynchronisation des favoris. On laisse :)
			adapter.notifyDataSetChanged();
			break;
		case 2:
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getTitle().toString()));
			lienCurrentlySeen = item.getTitle().toString();
			startActivityForResult(intent, item.getItemId());
			break;
		case 3:
			//Annuler
			break;
		}
	return true;
    }
	
	
}
