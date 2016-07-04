package org.example.webteam.loggin.deleteprofil;

import java.util.List;

import org.example.webteam.R;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;
import org.example.webteam.R.menu;
import org.example.webteam.eleve.ContactWebteam;
import org.json.JSONArray;

import potoman.webteam.bdd.contact.BddContactManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class SupprimerProfil extends Activity implements OnItemClickListener { //, OnCheckedChangeListener {
	ListView myList;
	ProfilAdapter adapter = null;
	public static final String PREFS_NAME = "MyPrefsFileWebteam";
	SharedPreferences variableSauv = null;
	JSONArray profilSaved = null;
	BddContactManager profilBdd;
    List<ContactWebteam> profilLoad = null;
	public static int appuieItem = 0;
	//CheckBox maCheckBox = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
		    setContentView(R.layout.supprimerprofil);
		    this.setTitle("Webteam > Gérer les profils");
			variableSauv = getSharedPreferences(PREFS_NAME, 0);
			
			profilBdd = new BddContactManager(this);
			profilBdd.open();
			//profilLoad = profilBdd.getProfilLogged();
			
			if (profilLoad == null) {
				finish();
				Toast.makeText(getApplicationContext(), "Il n'y a pas de profil d'enregistré !", Toast.LENGTH_LONG).show();
			}
			else {
			    myList = (ListView)findViewById(R.id.lvListeProfil);
			    myList.setOnItemClickListener(this);
			    adapter = new ProfilAdapter(getApplicationContext(), profilLoad);//, maCheckBox);
			    this.myList.setAdapter(adapter);
			}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		profilBdd.close();
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		((CheckBox)arg1.findViewById(R.id.cbSupprimer)).setChecked(!((CheckBox)arg1.findViewById(R.id.cbSupprimer)).isChecked());
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.supprimerprofil_, menu);
	return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.supprimer:
			int count = 0;
				for (int i = profilLoad.size() - 1; i >= 0; i--) {
					if (adapter.stateOfItem.get(i)) {
						profilBdd.deleteProfil(profilLoad.get(i).getId());
						count++;
						//maListeDeProfil.remove(i);
						//adapter.toggle(i);
					}
				}
				switch (count) {
				case 0:
					Toast.makeText(this, "Aucun profil n'as été supprimé.", Toast.LENGTH_SHORT).show();
					break;
				case 1:
					Toast.makeText(this, "Le profil a bien été supprimé.", Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(this, count + " profils ont bien été supprimés.", Toast.LENGTH_SHORT).show();
				}
				finish();
		return true;
		}
	return super.onMenuItemSelected(featureId, item);
	}
}
