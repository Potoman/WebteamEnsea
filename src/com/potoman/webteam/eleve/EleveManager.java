package com.potoman.webteam.eleve;

import java.util.ArrayList;
import java.util.List;

import org.example.webteam.R;
import org.example.webteam.R.id;
import org.example.webteam.R.menu;

import com.potoman.webteam.trombinoscope.TrombiResultAdapter;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

public class EleveManager extends Activity {

	public ListView myList;
	public static List<ContactWebteam> maListeDeProfil = new ArrayList<ContactWebteam>();
	public TrombiResultAdapter adapter = null;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	MenuInflater inflater = getMenuInflater();
	inflater.inflate(R.menu.trombinoscope_result_, menu);
	return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		boolean flag = true;
		switch (item.getItemId()) {
		case R.id.itemToutOuvrir:
			if (adapter.lstInfoProfil.size() == 0)
				break;
			while (flag) {
				for (int i = 0; i < adapter.lstInfoProfil.size(); i++) {
					if (i == adapter.lstInfoProfil.size() - 1) {
						//On est au bout...
						if (adapter.lstInfoProfil.get(i).typeInfo == TrombiResultAdapter.ITEM_NOM_PRENOM) {
							adapter.switchOpenClose(i);
							flag = false;
						}
						break;
					}
					if (adapter.lstInfoProfil.get(i).typeInfo == TrombiResultAdapter.ITEM_NOM_PRENOM && adapter.lstInfoProfil.get(i + 1).typeInfo == TrombiResultAdapter.ITEM_NOM_PRENOM) {
						adapter.switchOpenClose(i);
						break;
					}
				}
			}
			break;
		case R.id.itemToutFermer:
			if (adapter.lstInfoProfil.size() == 0)
				break;
			while (flag) {
				int i;
				for (i = 0; i < adapter.lstInfoProfil.size(); i++) {
					if (adapter.lstInfoProfil.get(i).typeInfo != TrombiResultAdapter.ITEM_NOM_PRENOM) {
						adapter.switchOpenClose(i);
						break;
					}
					if (i == adapter.lstInfoProfil.size() - 1 && adapter.lstInfoProfil.get(i).typeInfo == TrombiResultAdapter.ITEM_NOM_PRENOM) {
					flag = false;
					break;
					}
				}
			}
			break;
		}
	return super.onMenuItemSelected(featureId, item);
	}
}
