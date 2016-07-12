package com.potoman.webteam.favoris;

import org.example.webteam.R;
import org.example.webteam.R.drawable;
import org.example.webteam.R.layout;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

/*
 * On d�clare ici la table avec les onglet pour le menu de l'entreprise. Cela lui permet de switcher
 * entre les favoris et les menus d�roulants.
 */

public class TabFavoris extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.tab_favoris);
	    setTitle("Webteam > Favoris");
	    Resources res = getResources(); // Resource object to get Drawables
	    TabHost tabHost = getTabHost();  // The activity TabHost
	    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
	    Intent intent;  // Reusable Intent for each tab

	    // Create an Intent to launch an Activity for the tab (to be reused)
	    intent = new Intent().setClass(this, FavorisRagot.class); //FavorisAll, FavorisEleve & FavorisUrl

	    // Initialize a TabSpec for each tab and add it to the TabHost
	    //***********************************************************CENSURE********************************************************
	    spec = tabHost.newTabSpec("aide").setIndicator("Ragots",
	                      res.getDrawable(R.drawable.favoris))
	                  .setContent(intent);
	    tabHost.addTab(spec);

	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, FavorisLien.class);
	    spec = tabHost.newTabSpec("favoris").setIndicator("Url",
	                      res.getDrawable(R.drawable.favoris))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    // Do the same for the other tabs
	    intent = new Intent().setClass(this, FavorisEleve.class);
	    spec = tabHost.newTabSpec("eleve").setIndicator("Eleve",
	                      res.getDrawable(R.drawable.favoris))
	                  .setContent(intent);
	    tabHost.addTab(spec);
	    
	    tabHost.setCurrentTab(0);
	}
}

