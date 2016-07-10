package org.example.webteam.favoris;

import java.util.List;

import org.example.webteam.R;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;
import org.example.webteam.R.menu;

import potoman.webteam.bdd.BddUriManager;
import potoman.webteam.constant.Webteam;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class FavorisLien extends Activity implements OnItemClickListener {

	ListView myList;
	
    Activity saveActivityForProgressBar = this;
    
    BddUriManager uriBdd = null;
    
    FavorisLienAdapter adapter = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //requestWindowFeature(Window.FEATURE_PROGRESS);
	    requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	    setContentView(R.layout.favoris_lien);
	    this.setTitle("Webteam > Favoris > Lien");
	    
	    adapter = new FavorisLienAdapter(getApplicationContext(), Webteam.maListeDUriFavoris);
	    
		registerForContextMenu((ListView)findViewById(R.id.lvFavorisLien));
				
		uriBdd = new BddUriManager(this);
		uriBdd.open();
		
		
		getFavorisLien();
		if (Webteam.maListeDUriFavoris.size() != 0)
			afficherLien();
		else
			Toast.makeText(this, "Il n'y a pas de lien dans vos favoris.", Toast.LENGTH_LONG).show();
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		uriBdd.close();
		//Toast.makeText(this, "BOLT !", Toast.LENGTH_SHORT).show();
	}
	
	public void afficherLien() {
		myList = (ListView)findViewById(R.id.lvFavorisLien);
	    myList.setAdapter(adapter);
	    myList.setOnItemClickListener(this);
	}
	
	private void getFavorisLien() {
		Webteam.maListeDUriFavoris.clear();
		List<Uri> toAdd = uriBdd.getAllUri();
		if (toAdd != null) {
			Webteam.maListeDUriFavoris.addAll(toAdd);
		}
		//adapter.notifyDataSetInvalidated();
		adapter.notifyDataSetChanged();
	}
	
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		//TODO : Si il faut remodifier la suppression des profils par quelque chose qu'y fonctionne, tiliser cette m�thode :
		//adapter.stateOfItem.put(position, !adapter.stateOfItem.get(position));
		//adapter.notifyDataSetChanged();
		
		
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Webteam.maListeDUriFavoris.get(position).toString()));
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.url_favoris_, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.supprimer:
			int count = 0;
				for (int i = Webteam.maListeDUriFavoris.size() - 1; i >= 0; i--) {
					if (adapter.stateOfItem.get(i)) {
						uriBdd.deleteProfil(Webteam.maListeDUriFavoris.get(i));
						Webteam.maListeDUriFavoris.remove(i);
						//adapter.lstCheckBox.get(i).setChecked(false);
						count++;
					}
				}
				switch (count) {
				case 0:
					Toast.makeText(this, "Aucun lien n'as été supprimé.", Toast.LENGTH_SHORT).show();
					break;
				case 1:
					Toast.makeText(this, "Le lien a bien été supprimé.", Toast.LENGTH_SHORT).show();
					break;
				default:
					Toast.makeText(this, count + " liens ont bien été supprimés.", Toast.LENGTH_SHORT).show();
				}
				getFavorisLien();
				adapter.actualiseSparseBooleanArray();
				adapter.notifyDataSetChanged();
		return true;
		}
	return super.onMenuItemSelected(featureId, item);
	}
}
