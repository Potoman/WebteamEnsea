package org.example.webteam.ragot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.example.webteam.R;
import org.example.webteam.R.drawable;
import org.example.webteam.R.menu;
import org.example.webteam.eleve.FicheEleve;

import potoman.tools.L;
import potoman.webteam.bdd.BddRagotManager;
import potoman.webteam.bdd.BddUriManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class RagotManager extends Activity implements  OnItemLongClickListener, OnItemClickListener {

	public ListView myList;
	
	public List<Ragot> cible = null;

	public RagotAdapter adapter = null;
	public BddRagotManager ragotBdd;
	public BddUriManager uriBdd;
    
	public String lienCurrentlySeen = "";
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ragotBdd.close();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		//if (CallService.isConnected(this)) {
		if (cible.get(position).getSeparateur() == 0) {
			Intent monIntent = new Intent(this, FicheEleve.class);
			Bundle variableDePassage = new Bundle();
			variableDePassage.putInt("idProfil", cible.get(position).getIdPseudo());
		    monIntent.putExtras(variableDePassage); 
			startActivity(monIntent);
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		  Toast.makeText(this, "long click", Toast.LENGTH_SHORT).show();
		// TODO Auto-generated method stub
		return false;
	}

	
	//On crï¿½e un menu contextuel qui nous permettra de supprimer facilement un favoris avec un long click.
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	  super.onCreateContextMenu(menu, v, menuInfo);
	  MenuInflater inflater = getMenuInflater();
	  inflater.inflate(R.menu.longclickragots_, menu);
	  //Groupe 0 : Voir la fiche du profil
	  //Groupe 1 : Favoris
	  //Groupe 2 : Liens
	  //Groupe 3 : Annuler
	  menu.setHeaderTitle("Action");
	  
	  int i = 1;
	  if (cible.get((int)((AdapterContextMenuInfo)menuInfo).id).getSeparateur() == 0) {
		  
		  L.v("Ragots", "menuItem : id = " + (int)((AdapterContextMenuInfo)menuInfo).id + "; favoris : " + cible.get((int)((AdapterContextMenuInfo)menuInfo).id).getFavoris() + ", ragot : " + cible.get((int)((AdapterContextMenuInfo)menuInfo).id).getRagot());
			menu.add(0, cible.get((int)((AdapterContextMenuInfo)menuInfo).id).getIdPseudo(), 0, "Voir la fiche du profil");
			//*********************************CENSURE***************************************
			if (cible.get((int)((AdapterContextMenuInfo)menuInfo).id).getFavoris() == 0)
				menu.add(1, cible.get((int)((AdapterContextMenuInfo)menuInfo).id).getId(), 1, "Mettre en favoris");
			else
				menu.add(1, cible.get((int)((AdapterContextMenuInfo)menuInfo).id).getId(), 1, "Retirer des favoris");
			
			for (i = 1; i <= cible.get((int)((AdapterContextMenuInfo)menuInfo).id).getNbrLien(); i++)
				menu.add(2, i, i + 2, (CharSequence)cible.get((int)((AdapterContextMenuInfo)menuInfo).id).getLien(i - 1).toString());
	  }
	  menu.add(3, 1, i + 2, "Annuler"); //TODO : modifier le 666.
	}
	
	public void actualiseJustDate() {
		SimpleDateFormat dateWeekAndDayFormat = new SimpleDateFormat("w d");
		for (int i = 0; i < cible.size(); i++) {
			if (i == 0) {
				cible.get(i).setJustDate(true);
			} else
				if (cible.get(i - 1).getSeparateur() == 0) {
					if (!dateWeekAndDayFormat.format(
							new Date(cible.get(i - 1).getDate()*1000)
							).equals(
						dateWeekAndDayFormat.format(
							new Date(cible.get(i).getDate()*1000)
						)))
						cible.get(i).setJustDate(true);
					else
						cible.get(i).setJustDate(false);
				}
				else {
					if (!dateWeekAndDayFormat.format(
							new Date(cible.get(i - 2).getDate()*1000)
							).equals(
						dateWeekAndDayFormat.format(
							new Date(cible.get(i).getDate()*1000)
						)))
						cible.get(i).setJustDate(true);
					else
						cible.get(i).setJustDate(false);
				}
		}
	}
	
	protected void remplirListRagot() {
		//cible.clear();
		List<Ragot> toAdd = ragotBdd.get50LastRagot(cible.size());
		if (toAdd != null) {
			cible.addAll(toAdd);
		}
		//cible.add(new Ragot(contenuJSON.getJSONObject(index).getInt("id_posteur"), contenuJSON.getJSONObject(index).getString("pseudo"), contenuJSON.getJSONObject(index).getString("ragot"), contenuJSON.getJSONObject(index).getString("time"), news, false));
		actualiseJustDate();
	}
	
	public boolean loadOldRagot() {
		List<Ragot> toAdd = ragotBdd.get50LastRagot(cible.size());
		if (toAdd != null) {
			cible.addAll(toAdd);
			actualiseJustDate();
			return true;
		}
		else
			return false;
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		final String tempUri = lienCurrentlySeen;
		lienCurrentlySeen = "";
		
		if (!tempUri.equals("")) {
			if (uriBdd.getUri(tempUri) == null) {
			    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				    @Override
				    public void onClick(DialogInterface dialog, int which) {
				        switch (which){
				        case DialogInterface.BUTTON_POSITIVE:
				            uriBdd.insertUri(Uri.parse(tempUri));
				            break;
				        case DialogInterface.BUTTON_NEGATIVE:
				            //No button clicked
				            break;
				        }
				    }
				};
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this); 
				builder.setMessage("Voulez-vous ajouter ce lien à  vos favoris ?").setPositiveButton("Pire !", dialogClickListener)
				    .setNegativeButton("C'est du noob...", dialogClickListener).setTitle("Ajout en favoris...").setIcon(R.drawable.icon).show();
			}
			else
				Toast.makeText(this, "Ce lien se trouve dans vos favoris.", Toast.LENGTH_SHORT).show();
		}
	}
}


