package org.example.webteam.favoris;

import java.util.List;

import org.example.webteam.R;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;
import org.example.webteam.R.menu;
import org.example.webteam.eleve.EleveManager;
import org.example.webteam.eleve.FicheEleve;
import org.example.webteam.eleve.ContactWebteam;
import org.example.webteam.trombinoscope.TrombiResultAdapter;

import potoman.webteam.bdd.contact.BddContactManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class FavorisEleve extends EleveManager implements OnItemClickListener {
	
	BddContactManager profilBdd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    setContentView(R.layout.trombiresult);
		registerForContextMenu((ListView)findViewById(R.id.lvTrombiResult));
		
	    this.setTitle("Webteam > Trombi > Résultat");
	    
	    profilBdd = new BddContactManager(this);
		profilBdd.open();
	    
		myList = (ListView)findViewById(R.id.lvTrombiResult);
	    adapter = new TrombiResultAdapter(getApplicationContext(), maListeDeProfil);//, maCheckBox);
	    myList.setAdapter(adapter);
	    myList.setOnItemClickListener(this);
	}
	
	@Override
	protected void onResume() {
		getProfil();
		
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		profilBdd.close();
		super.onDestroy();
	}
	
	public void getProfil() {
		maListeDeProfil.clear();
		List<ContactWebteam> toAdd = profilBdd.getProfilFavoris();
		if (toAdd != null)
			maListeDeProfil.addAll(toAdd);
		if (maListeDeProfil.size() == 0) {
			Toast.makeText(this, "Il n'y a pas de profil d'éléve dans vos favoris.", Toast.LENGTH_LONG).show();
		}
		adapter.synchNewProfil();
		adapter.notifyDataSetChanged();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
	    adapter.switchOpenClose(position);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	  super.onCreateContextMenu(menu, v, menuInfo);
	  MenuInflater inflater = getMenuInflater();
	  inflater.inflate(R.menu.longclicktrombinoscope_, menu);
	  menu.add(0, adapter.lstProfil.get(adapter.lstInfoProfil.get((int)((AdapterContextMenuInfo)menuInfo).id).indexListProfil).getId(), 0, "Voir la fiche du profil");
	  menu.add(0, adapter.lstProfil.get(adapter.lstInfoProfil.get((int)((AdapterContextMenuInfo)menuInfo).id).indexListProfil).getId(), 0, "Supprimer des favoris");
	}
	
	@Override
    public boolean onContextItemSelected(MenuItem item)
    {
		if (item.getTitle().toString().equals("Annuler")) {
		}
		else
			if (item.getTitle().toString().equals("Supprimer des favoris")) {
				profilBdd.deleteProfil(item.getItemId());
				getProfil();
			}
			else {
				Intent monIntent = new Intent(this,FicheEleve.class);
				Bundle variableDePassage = new Bundle();
				variableDePassage.putInt("idProfil", item.getItemId());
			    monIntent.putExtras(variableDePassage); 
				startActivity(monIntent);
			}
	return true;
    }
}
