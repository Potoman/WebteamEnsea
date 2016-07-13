package com.potoman.webteam.accueil;

import com.potoman.tools.CallService;
import com.potoman.tools.L;
import com.potoman.webteam.Preferences;
import com.potoman.webteam.R;
import com.potoman.webteam.anniversaire.Anniversaire;
import com.potoman.webteam.boitemanager.boite.messageprive.BoiteDeMessagePrive;
import com.potoman.webteam.eleve.SupprimerPhotoProfil;
import com.potoman.webteam.exception.ExceptionService;
import com.potoman.webteam.favoris.TabFavoris;
import com.potoman.webteam.ragot.Ragots;
import com.potoman.webteam.trombinoscope.Trombinoscope;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class PageLog extends Activity implements OnClickListener, OnTouchListener {
	Boolean logOk = false;
	LinearLayout linearLayoutMain = null;
	ViewFlipper vfDetails = null;
	Button buttonRagots = null;
	Button buttonTrombi = null;
	Button buttonAnniversaire = null;
	Button buttonFavoris = null;
	Button buttonNews = null;
	Button buttonMessage = null;
	String pseudoCurrent = "";
	String passwordCurrent = "";
	
	//private SharedPreferences preferences = null;
	
	private static final int CODE_RETOUR = 1;
    
	private float downXValue; //Used for grabbing
	
	//private BddProfilManager profilBdd = null;
	
	//private Object data = null;
	private int numViewFlipper = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
    	L.v("PageLog", "onCreate");
    	L.i("PageLog", "def pref = " + PreferenceManager.getDefaultSharedPreferences(this).getString("listPrefQuality", "error"));
//	    if (isPortrait())
	    	setContentView(R.layout.pagelog);
//	    else
//	    	setContentView(R.layout.pageloglandscape);

//	    profilBdd = new BddProfilManager(this);
//	    profilBdd.open();
		
	    linearLayoutMain = (LinearLayout)findViewById(R.id.main_layout_page_log);
	    linearLayoutMain.setOnTouchListener(this);
		buttonRagots = (Button)findViewById(R.id.ragots);
		buttonRagots.setOnClickListener(this);
		buttonTrombi = (Button)findViewById(R.id.trombi);
		buttonTrombi.setOnClickListener(this);
		buttonAnniversaire = (Button)findViewById(R.id.anniversaire);
		buttonAnniversaire.setOnClickListener(this);
		buttonFavoris = (Button)findViewById(R.id.favoris);
		buttonFavoris.setOnClickListener(this);
		buttonMessage = (Button)findViewById(R.id.message);
		buttonMessage.setOnClickListener(this);
		vfDetails = (ViewFlipper)findViewById(R.id.details);

        logOk = true;
		
//		data = getLastNonConfigurationInstance();
//		if (data != null) {
//			numViewFlipper = ((TempData)data).numViewFlipper;
//			this.setTitle("Webteam > Accueil > Page " + numViewFlipper + 1 + "/2");
//			if (numViewFlipper == 1) {
//				vfDetails.showNext();
//			}
//		}
//		else
			this.setTitle("Webteam > Accueil > Page 1/2");
	}

	@Override
	public void onResume() {
		super.onResume();
		//Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show();

//	    if (buttonRagots == null)
//	    	Toast.makeText(this, "button nul resume", Toast.LENGTH_LONG).show();
	    L.v("PageLog", "onResume");
	}
	
	public boolean isPortrait() {
		if (getWindow().getWindowManager().getDefaultDisplay().getHeight() > getWindow().getWindowManager().getDefaultDisplay().getWidth())
			return true;
		else
			return false;
	}
	
	public void onConfigurationChanged(Configuration newConfig) {
	    super.onConfigurationChanged(newConfig);
	    L.v("PageLog", "onConfigurationChanged");
	    linearLayoutMain = null;
	    buttonRagots = null;
	    buttonTrombi = null;
	    buttonAnniversaire = null;
	    buttonFavoris = null;
	    buttonMessage = null;
	    vfDetails = null;
		    
	    // Checks the orientation of the screen
	    if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
	    	setContentView(R.layout.pageloglandscape);
	    } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
	    	setContentView(R.layout.pagelog);
	    }
	    
	    linearLayoutMain = (LinearLayout)findViewById(R.id.main_layout_page_log);
	    linearLayoutMain.setOnTouchListener(this);
	    buttonRagots = (Button)findViewById(R.id.ragots);
		buttonRagots.setOnClickListener(this);
	    buttonTrombi = (Button)findViewById(R.id.trombi);
	    buttonTrombi.setOnClickListener(this);
	    buttonAnniversaire = (Button)findViewById(R.id.anniversaire);
	    buttonAnniversaire.setOnClickListener(this);
	    buttonFavoris = (Button)findViewById(R.id.favoris);
	    buttonFavoris.setOnClickListener(this);
	    buttonMessage = (Button)findViewById(R.id.message);
		buttonMessage.setOnClickListener(this);
		vfDetails = (ViewFlipper)findViewById(R.id.details);
		
		if (numViewFlipper == 1) {
			vfDetails.showNext();
		}
	}
	
	public void onClick(View v) {
		if (v == buttonRagots)
			startActivity(new Intent(this,Ragots.class));
		if (v == buttonTrombi)
			if (CallService.isConnected(this))
				startActivity(new Intent(this,Trombinoscope.class));
			else
				Toast.makeText(this, getResources().getStringArray(R.array.exception)[ExceptionService.ID_ERROR_CONNECTED], Toast.LENGTH_SHORT).show();
		if (v == buttonAnniversaire)
			if (CallService.isConnected(this))
				startActivity(new Intent(this,Anniversaire.class));
			else
				Toast.makeText(this, getResources().getStringArray(R.array.exception)[ExceptionService.ID_ERROR_CONNECTED], Toast.LENGTH_SHORT).show();
		if (v == buttonFavoris)
			startActivity(new Intent(this,TabFavoris.class));
		if (v == buttonMessage)
			startActivity(new Intent(this,BoiteDeMessagePrive.class));
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		switch (arg1.getAction())
        {		
            case MotionEvent.ACTION_DOWN:
            {
                L.v("PageLog", "DOWN");
                // store the X value when the user's finger was pressed down
                downXValue = arg1.getX();
                break;
            }

            case MotionEvent.ACTION_UP:
            {
                // Get the X value when the user released his/her finger
                float currentX = arg1.getX();

                // going backwards: pushing stuff to the right
                if (downXValue + Math.ceil(getWindow().getWindowManager().getDefaultDisplay().getWidth()/3) < currentX)
                {
					vfDetails.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
					vfDetails.showPrevious();
					this.setTitle("Webteam > Accueil > " + (String)vfDetails.getCurrentView().getTag());
					if (numViewFlipper == 0)
						numViewFlipper = 1;
					else
						numViewFlipper = 0;
                }

                // going forwards: pushing stuff to the left
                if (downXValue - Math.ceil(getWindow().getWindowManager().getDefaultDisplay().getWidth()/3) > currentX)
                {
					vfDetails.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
					vfDetails.showNext();
					this.setTitle("Webteam > Accueil > " +  (String)vfDetails.getCurrentView().getTag());
					if (numViewFlipper == 0)
						numViewFlipper = 1;
					else
						numViewFlipper = 0;
                }
                break;
            }
        }
        // if you return false, these actions will not be recorded
        return true;
	}
	
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.general_, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		Intent monIntent = null;
		switch (item.getItemId()) {
		case R.id.itemPreferences:
			monIntent = new Intent(this,Preferences.class);
			startActivityForResult(monIntent, CODE_RETOUR);
		return true;
		case R.id.item_root_supprimer_photo_profil:
			startActivity(new Intent(this, SupprimerPhotoProfil.class));
		return true;
		}
	return super.onMenuItemSelected(featureId, item);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == CODE_RETOUR) {
			Toast.makeText(this, "Modifications terminées", Toast.LENGTH_SHORT).show();
		}
	super.onActivityResult(requestCode, resultCode, data);
	}
}



