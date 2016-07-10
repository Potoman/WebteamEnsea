package org.example.webteam.trombinoscope;

import org.example.webteam.R;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;
import org.example.webteam.R.menu;
import org.example.webteam.eleve.EleveManager;
import org.example.webteam.eleve.FicheEleve;
import org.example.webteam.eleve.ContactWebteam;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import potoman.tools.L;
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

public class TrombiResult extends EleveManager implements OnItemClickListener {
	private JSONObject resultatRecherche;
	//Button buttonOuvrir = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);

	    setContentView(R.layout.trombiresult);
		registerForContextMenu((ListView)findViewById(R.id.lvTrombiResult));
		
	    this.setTitle("Trombinoscope > Resultat");
	    
		getProfil();
		
		myList = (ListView)findViewById(R.id.lvTrombiResult);
	    adapter = new TrombiResultAdapter(getApplicationContext(), maListeDeProfil);//, maCheckBox);
	    myList.setAdapter(adapter);
	    myList.setOnItemClickListener(this);
	}
	
	public void getProfil() {
		Intent intent = getIntent();
		try {
			resultatRecherche = new JSONObject(intent.getCharSequenceExtra("resultatRecherche").toString());
			JSONArray arrayListeDesProfils = resultatRecherche.getJSONArray("contenu");
			maListeDeProfil.clear();
			int index = 0;
				while (!arrayListeDesProfils.isNull(index)) {
					maListeDeProfil.add(new ContactWebteam(arrayListeDesProfils.getJSONObject(index).getInt("id"), arrayListeDesProfils.getJSONObject(index).getString("pseudo"), arrayListeDesProfils.getJSONObject(index).getString("nom"), arrayListeDesProfils.getJSONObject(index).getString("prenom"), arrayListeDesProfils.getJSONObject(index).getString("email"), arrayListeDesProfils.getJSONObject(index).getString("telephone"), arrayListeDesProfils.getJSONObject(index).getString("telephoneFixe"), arrayListeDesProfils.getJSONObject(index).getString("telephoneParent"), arrayListeDesProfils.getJSONObject(index).getString("classe")));
					index++;
				}
			
			
		} catch (JSONException e) {
			L.v("TrombiResult","Impossible de cr�er le r�sultat de la recherche.");
		}
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
	}

	@Override
    public boolean onContextItemSelected(MenuItem item)
    {
		if (item.getTitle().toString().equals("Annuler")) {
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
