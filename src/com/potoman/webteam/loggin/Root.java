package com.potoman.webteam.loggin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.example.webteam.R;

import com.potoman.tools.CallService;
import com.potoman.tools.IObserver;
import com.potoman.tools.L;
import com.potoman.webteam.Preferences;
import com.potoman.webteam.TempData;
import com.potoman.webteam.accueil.PageLog;
import com.potoman.webteam.boitemanager.boite.email.BoiteDeMessageEmail;
import com.potoman.webteam.caligula.Caligula;
import com.potoman.webteam.clubphoto.ActClubPhoto;
import com.potoman.webteam.constant.Webteam;
import com.potoman.webteam.credit.Credit;
import com.potoman.webteam.historique.Historique;
import com.potoman.webteam.task.ATConnexionSquirrel;
import com.potoman.webteam.task.ATConnexionWebteam;
import com.potoman.webteam.task.IWorkFinishOfAsyncTask;
import com.potoman.webteam.task.ATConnexionWebteam.RetourConnexion;
import com.potoman.webteam.task.clubphoto.ATConnexionClubPhoto;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class Root extends Activity implements OnClickListener, IWorkFinishOfAsyncTask, IObserver {
    /** Called when the activity is first created. */
	static final String[] COUNTRIES = new String[] {
		  "Potoman"
		};

	private TextView tvTitrePanneauWebteam = null;
	private TextView tvVersionApplicationWebteam = null;
	private TextView tvPseudoWebteam = null;
	private TextView tvPasswordWebteam = null;
	private TextView tvSeSouvenirWebteam = null;
	private EditText etPseudoWebteam = null;
	private EditText etPasswordWebteam = null;
	private Button bConnexionWebteam = null;
	private Button bConnexionCaligula = null;
	private Button bConnexionSquirrel = null;
	private Button bConnexionClubPhoto = null;
//	private Button bPassConnexionClubPhoto = null;
	private CheckBox cbSeSouvenirWebteam = null;
	
	private SharedPreferences preferences = null;
	private ProgressDialog myProgressDialog;
	
	private boolean alreadyDisplayVersion = false;

//	public static final int NOTIFICATION_NEW_VERSION = 1;
//	private NotificationManager myNotificationManager = null;
//	private Notification myNotification = null;
	private RemoteViews contentView = null;
//	private DownloadMAJ myDownloadMAJ = null;
//	private Intent myPromptInstall = null;

	private ATConnexionWebteam atConnexion = null;
	
	private RelativeLayout rlRoot = null;
	
//    public static String VERSION = "";

    public static int NBR_RAGOT = 50;
    
    @Override
    public Object onRetainNonConfigurationInstance() {
        TempData temp = new TempData();
        temp.alreadyDisplayVersion = alreadyDisplayVersion;
        return temp;
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
//	    getContact();
	    
	    setContentView(R.layout.root_view_flipper);
	    rlRoot = (RelativeLayout) findViewById(R.id.root_rl_main);
		
		
		tvTitrePanneauWebteam = (TextView) findViewById(R.id.root_tv_titre_webteam);
//		tvTitrePanneauCaligula = (TextView) findViewById(R.id.root_tv_titre_caligula);
//		tvTitrePanneauSquirrel = (TextView) findViewById(R.id.root_tv_titre_squirrel);
//		tvTitrePanneauAiensea = (TextView) findViewById(R.id.root_tv_titre_aiensea);
		tvVersionApplicationWebteam = (TextView) findViewById(R.id.root_tv_description_webteam);
//		tvVersionApplicationCaligula = (TextView) findViewById(R.id.root_tv_description_caligula);
//		tvVersionApplicationSquirrel = (TextView) findViewById(R.id.root_tv_description_squirrel);
//		tvVersionApplicationAiensea = (TextView) findViewById(R.id.root_tv_description_aiensea);
		tvPseudoWebteam = (TextView) findViewById(R.id.root_tv_pseudo_webteam);
		tvPasswordWebteam = (TextView) findViewById(R.id.root_tv_password_webteam);
		tvSeSouvenirWebteam = (TextView) findViewById(R.id.root_tv_se_souvenir_webteam);
//		tvPseudoSquirrel = (TextView) findViewById(R.id.root_tv_pseudo_squirrel);
//		tvPasswordSquirrel = (TextView) findViewById(R.id.root_tv_password_squirrel);
//		tvSeSouvenirSquirrel = (TextView) findViewById(R.id.root_tv_se_souvenir_squirrel);
		etPseudoWebteam = (EditText) findViewById(R.id.root_et_pseudo_webteam);
		etPasswordWebteam = (EditText) findViewById(R.id.root_et_password_webteam);
//		etPseudoSquirrel = (EditText) findViewById(R.id.root_et_pseudo_squirrel);
//		etPasswordSquirrel = (EditText) findViewById(R.id.root_et_password_squirrel);
		bConnexionWebteam = (Button) findViewById(R.id.root_b_connexion_webteam);
		bConnexionCaligula = (Button) findViewById(R.id.root_b_connexion_caligula);
		bConnexionSquirrel = (Button) findViewById(R.id.root_b_connexion_squirrel);
		bConnexionClubPhoto = (Button) findViewById(R.id.root_b_connexion_club_photo);
//		bPassConnexionClubPhoto = (Button) findViewById(R.id.root_b_pass_connexion_club_photo);
		bConnexionCaligula.setVisibility(View.GONE);
		bConnexionSquirrel.setVisibility(View.GONE);
		bConnexionClubPhoto.setVisibility(View.GONE);
//		bPassConnexionClubPhoto.setVisibility(View.GONE);
		cbSeSouvenirWebteam = (CheckBox) findViewById(R.id.root_cb_se_souvenir_webteam);
//		cbSeSouvenirSquirrel = (CheckBox) findViewById(R.id.root_cb_se_souvenir_squirrel);
		
		
		
		
		
		
	    // A garder précieusement =)
	    //PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
	    
	    //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
	    //getWindow().setBackgroundDrawableResource(R.drawable.fond_480_800);
	    L.v("Root", "onCreate");
    	
	    preferences = PreferenceManager.getDefaultSharedPreferences(this);
		
	    Object data = getLastNonConfigurationInstance();
		if (data != null) {
			alreadyDisplayVersion = ((TempData) data).alreadyDisplayVersion;
		}
	    threadManagerBitmap.start();
	}

	@Override
	protected void onRestart() {
		super.onRestart();

	    L.v("Root", "onRestart");
	}
	
	@Override
	public void onResume() {
		super.onResume();
		String versionNo = "";
		PackageInfo pInfo = null;
		try{
			pInfo = getPackageManager().getPackageInfo("org.example.webteam",PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			pInfo = null;
		}
		if (pInfo != null) {
			versionNo = pInfo.versionName;
		}
//		String versionNoBis = versionNo;
//		versionNoBis = versionNoBis.replace("è", "e");
//		versionNoBis = versionNoBis.replace("é", "e");
//		versionNoBis = versionNoBis.replace("ê", "e");
//		versionNoBis = versionNoBis.replace("ë", "e");
//		versionNoBis = versionNoBis.replace("ï", "i");
//		versionNoBis = versionNoBis.replace("î", "i");
//		versionNoBis = versionNoBis.replace("à", "a");
//		versionNoBis = versionNoBis.replace("â", "a");
//		versionNoBis = versionNoBis.replace("ô", "o");
//		versionNoBis = versionNoBis.replace("ö", "o");
//		versionNoBis = versionNoBis.replace("ù", "u");
//		versionNoBis = versionNoBis.replace("ü", "u");
//		versionNoBis = versionNoBis.replace("û", "u");
//		versionNoBis = versionNoBis.replace(".", "_");
//		VERSION = "WebteamAndroid_v" + versionNoBis + ".apk";
//		L.v("Root", "onResume - version : " + VERSION);

		tvVersionApplicationWebteam.setText("Created by Potoman | Powered by Eclipse | " + versionNo);

		bConnexionWebteam.setOnClickListener(this);
		bConnexionCaligula.setOnClickListener(this);
		bConnexionSquirrel.setOnClickListener(this);
		bConnexionClubPhoto.setOnClickListener(this);
//		bPassConnexionClubPhoto.setOnClickListener(this);
		
	    refreshComponentView();
	    
	   initEditText();
	    
		
		Display display = getWindowManager().getDefaultDisplay();
		widthScreen = display.getWidth();
		//vg = (ViewGroup) findViewById(R.id.vg);
		vg = (ViewGroup) findViewById(R.id.root_rl_main);
		vg.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					threadManagerBitmap.clear();
					moveX = (int) event.getX();
					startX = (int) event.getX();
					break;
				case MotionEvent.ACTION_MOVE:
					moveBackground((int) event.getX() - moveX);
					moveX = (int) event.getX();
					break;
				case MotionEvent.ACTION_UP:
					fixBackground((int) event.getX(), startX > (int) event.getX());
				default:
					break;
				}
				return true;
			}
		});
		moveBackground(0);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void initEditText() {
		boolean seSouvenir = preferences.getBoolean("sesouvenirwebteam", true);
		if (seSouvenir && oldDestX == 0) {
			cbSeSouvenirWebteam.setChecked(seSouvenir);
			etPseudoWebteam.setText(preferences.getString(Webteam.PSEUDO, ""));
			etPasswordWebteam.setText(preferences.getString(Webteam.PASSWORD, ""));
		}

		seSouvenir = preferences.getBoolean("sesouvenirsquirrel", true);
		if (seSouvenir && oldDestX == 2) {
			cbSeSouvenirWebteam.setChecked(seSouvenir);
			etPseudoWebteam.setText(preferences.getString(Webteam.PSEUDO_WEBMAIL, ""));
			etPasswordWebteam.setText(preferences.getString(Webteam.PASSWORD_WEBMAIL, ""));
		}
		
		seSouvenir = preferences.getBoolean("sesouvenirclubphoto", true);
		if (seSouvenir && oldDestX == 3) {
			cbSeSouvenirWebteam.setChecked(seSouvenir);
			etPseudoWebteam.setText(preferences.getString(Webteam.PSEUDO_CLUB_PHOTO, ""));
			etPasswordWebteam.setText(preferences.getString(Webteam.PASSWORD_CLUB_PHOTO, ""));
		}
	}

	private ATConnexionSquirrel myConnexionSquirrel = null;
	private ATConnexionClubPhoto myConnexionClubPhoto = null;
	private HttpClient httpClient = null;
	public static final int RESULT_CONNEXION_OK = 0;
	public static final int RESULT_CONNEXION_KO = 1;
	
	@Override
	public void onClick(View v) {
		if (v == bConnexionWebteam) {
			String pseudoLogged = "";
			String passwordLogged = "";
			pseudoLogged = etPseudoWebteam.getText().toString();
			passwordLogged = etPasswordWebteam.getText().toString();
			if (pseudoLogged.equals("") || passwordLogged.equals("")) {
				Toast.makeText(this, "Veille bien à remplir les champs avant de lancer une connexion.", Toast.LENGTH_LONG).show();
			}
			else {
				if (pseudoLogged.equals(preferences.getString(Webteam.PSEUDO, "")) && 
					passwordLogged.equals(preferences.getString(Webteam.PASSWORD, ""))) {
					getConnexion(pseudoLogged, passwordLogged);
					launchPageLog();
				}
				else {
					getConnexion(pseudoLogged, passwordLogged);
				}
			}
		}
		else if (v == bConnexionCaligula) {
			startActivity(new Intent(this, Caligula.class));
		}
		else if (v == bConnexionSquirrel) {
			if (PreferenceManager.getDefaultSharedPreferences(this).getString(Webteam.PSEUDO_WEBMAIL, "").equals("")) {
				
				if (etPseudoWebteam.getText().toString().trim().equals("") ||  etPasswordWebteam.getText().toString().trim().equals("")) {
					Toast.makeText(this, "Veuillez remplir les champs.", Toast.LENGTH_SHORT).show();
					return;
				}
				
				myProgressDialog = ProgressDialog.show(this, getResources().getStringArray(R.array.waitingConnexionSquirrel)[0], Webteam.getPhraseDAmbiance(), true, true, new OnCancelListener() {
					public void onCancel(DialogInterface dialog) {
						finish();
					}
				});
				
				httpClient = new DefaultHttpClient();
			    CallService.setParam(httpClient, false);
				myConnexionSquirrel = new ATConnexionSquirrel(this, etPseudoWebteam.getText().toString(), etPasswordWebteam.getText().toString(), httpClient, IWorkFinishOfAsyncTask.AT_CONNEXION_SQUIRREL_FOR_LOGG);
				myConnexionSquirrel.execute(this);
			}
			else {
				Intent monIntent = new Intent(this, BoiteDeMessageEmail.class);
				startActivity(monIntent);
			}
		}
		else if (v == bConnexionClubPhoto) {
			if (PreferenceManager.getDefaultSharedPreferences(this).getString(Webteam.PSEUDO_CLUB_PHOTO, "").equals("")) {
				if (etPseudoWebteam.getText().toString().trim().equals("") ||  etPasswordWebteam.getText().toString().trim().equals("")) {
					Toast.makeText(this, "Veuillez remplir les champs.", Toast.LENGTH_SHORT).show();
					return;
				}
				if (CallService.isConnected(this)) {
					myProgressDialog = ProgressDialog.show(this, getResources().getStringArray(R.array.waitingConnexionClubPhoto)[0], Webteam.getPhraseDAmbiance(), true, true, new OnCancelListener() {
						public void onCancel(DialogInterface dialog) {
							finish();
						}
					});
	
					httpClient = new DefaultHttpClient();
				    CallService.setParam(httpClient, false);
				    myConnexionClubPhoto = new ATConnexionClubPhoto(this, etPseudoWebteam.getText().toString(), etPasswordWebteam.getText().toString(), httpClient);
				    myConnexionClubPhoto.execute(this);
				}
				else {
					Toast.makeText(this, "Vous n'êtes pas connecté à internet, et vos identifiants ne sont pas enregistré, vous ne pourrez pas voir toutes les photos.", Toast.LENGTH_LONG).show();
					Intent monIntent = new Intent(this, ActClubPhoto.class);
					startActivity(monIntent);
				}
			}
			else {
				Intent monIntent = new Intent(this, ActClubPhoto.class);
				startActivity(monIntent);
			}
		}
//		else if (v == bPassConnexionClubPhoto) {
//			startActivity(new Intent(this, AClubPhoto.class));
//		}
	}

	private void clearInfoLog() {
		Editor editor = preferences.edit();
		editor.putInt(Webteam.ID, -1);
		editor.putString(Webteam.PSEUDO, "");
		editor.putString(Webteam.PASSWORD, "");
		editor.putString(Webteam.PSEUDO_CLUB_PHOTO, "");
		editor.putString(Webteam.PASSWORD_CLUB_PHOTO, "");
		editor.commit();
		etPseudoWebteam.setText("");
		etPasswordWebteam.setText("");
	}
	
	private void saveInfoLog(int id, String pseudo) {
		Editor editor = preferences.edit();
		editor.putInt(Webteam.ID, id);
		editor.putString(Webteam.PSEUDO, pseudo);
		editor.putString(Webteam.PASSWORD, etPasswordWebteam.getText().toString());
		editor.commit();
	}
	
	private void launchPageLog() {
		ActivityManager am = (ActivityManager) this.getSystemService(Activity.ACTIVITY_SERVICE);
		String packageName = am.getRunningTasks(1).get(0).topActivity.getPackageName();
		String className = am.getRunningTasks(1).get(0).topActivity.getClassName();
		if (packageName.equals(getApplication().getPackageName()) && className.equals(Root.class.getName())) {
			startActivity(new Intent(this, PageLog.class));
		}
	}
	
	public void getConnexion(String pseudo, String password) {
		atConnexion = new ATConnexionWebteam(this, pseudo, password);
		atConnexion.execute(getApplicationContext());
	}
	
	@Override
	public void workFinish(int state) {
		switch (state) {
		case ATConnexionWebteam.STATE_ERROR_IN_PARSING_JSON:
			Toast.makeText(this, "Erreur dans la lecture du message de retour.", Toast.LENGTH_SHORT).show();
			break;
		case ATConnexionWebteam.STATE_NOT_INTERNET:
			Toast.makeText(this, "Vous n'êtes pas connecté à internet.", Toast.LENGTH_SHORT).show();
			break;
		case ATConnexionWebteam.STATE_OK:
			try {
				RetourConnexion retourConnexion = atConnexion.get();
				if (retourConnexion == null) {
					Toast.makeText(this, "Mauvais login et/ou mots de passe.", Toast.LENGTH_SHORT).show();
				}
				else if (retourConnexion.version.equals("")) {
					if (!retourConnexion.pseudo.equals(preferences.getString(Webteam.PSEUDO, "")) || 
							!retourConnexion.password.equals(preferences.getString(Webteam.PASSWORD, ""))) {
						saveInfoLog(retourConnexion.id, retourConnexion.pseudo);
						launchPageLog();
					}
					else {
						saveInfoLog(retourConnexion.id, retourConnexion.pseudo);
					}
				}
				else {
					if (!retourConnexion.pseudo.equals(preferences.getString(Webteam.PSEUDO, "")) || 
							!retourConnexion.password.equals(preferences.getString(Webteam.PASSWORD, ""))) {
						saveInfoLog(retourConnexion.id, retourConnexion.pseudo);
						launchPageLog();
					}
					else {
						saveInfoLog(retourConnexion.id, retourConnexion.pseudo);
					}
//					// On télécharge la nouvelle version de l'appli :
//					sendNotification(retourConnexion.version);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			break;
		case ATConnexionWebteam.STATE_WRONG_LOGGIN:
			Toast.makeText(this, "Mauvais login et/ou mots de passe.", Toast.LENGTH_SHORT).show();
			break;
		}
		
	}
	
//    private void sendNotification(String versionAppli) {
//    	L.v("Root", "Before VersionAppli = " + versionAppli);
//    	if (alreadyDisplayVersion) {
//    		return;
//    	}
//    	L.v("Root", "VersionAppli = " + versionAppli);
//    	Toast.makeText(this, "Il y a une version plus récente de la Webteam. Va la télécharger !!!", Toast.LENGTH_LONG).show();
//    	alreadyDisplayVersion = true;
//    	
//    	
//    	myDownloadMAJ = new DownloadMAJ(this, versionAppli);
//		if (myDownloadMAJ != null) {
//			
//			//On prépare la notification...
//			String ns = Context.NOTIFICATION_SERVICE;
//    		myNotificationManager = (NotificationManager) getSystemService(ns);
//    		
//    		int icon = R.drawable.stat_sys_download_anim0;
//    		CharSequence tickerText = "Webteam : MAJ";
//    		long when = System.currentTimeMillis();
//    		
//    		myNotification = new Notification(icon, tickerText, when);
//    		
//    		//FileOutputStream fos = openFileOutput(nomNewVersion, MODE_WORLD_READABLE | MODE_WORLD_WRITEABLE);
//
//    		// write the .apk content here ... flush() and close()
//
//    		// Now start the standard instalation window
//    		L.w("Root", "getApplicationContext().getFilesDir()" + getApplicationContext().getFilesDir());
//    		File fileLocation = new File(getApplicationContext().getFilesDir(), versionAppli);
//    		
////    		myPromptInstall = new Intent(Intent.ACTION_VIEW);
////    		myPromptInstall.setDataAndType(Uri.fromFile(fileLocation), "image/jpeg"); //"application/vnd.android.package-archive");
//    		
//    		myPromptInstall = new Intent(Intent.ACTION_VIEW);
//    		myPromptInstall.setDataAndType(Uri.fromFile(fileLocation), "application/vnd.android.package-archive");
//    		
//    		
//    		
//    		PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);
//
//    		myNotification.setLatestEventInfo(getApplicationContext(), "Webteam", "Webteam : MAJ", contentIntent);
//    		
//    		myNotification.defaults |= Notification.DEFAULT_LIGHTS;
//    		myNotification.ledARGB = 0xffcf178b;
//    		myNotification.ledOnMS = 300;
//    		myNotification.ledOffMS = 1000;
//    		myNotification.flags |= Notification.FLAG_SHOW_LIGHTS;
//    		myNotification.flags |= Notification.FLAG_ONGOING_EVENT;
//    		
//    		contentView = new RemoteViews(getPackageName(), R.layout.test_progress_bar);
//    		contentView.setTextViewText(R.id.tv_notification_mise_a_jour, "MAJ Webteam : " + versionAppli.substring(versionAppli.indexOf("v"), versionAppli.indexOf(".apk")).replace("_", "."));
//    		if (Integer.parseInt(Build.VERSION.SDK) >= 11) {
//    			contentView.setTextColor(R.id.tv_notification_mise_a_jour, getResources().getColor(R.color.white));
//    			contentView.setTextColor(R.id.tv_notification_mise_a_jour_pourcentage, getResources().getColor(R.color.white));
//    		}
//    		//contentView.setTextColor(R.id.tv_notification_mise_a_jour, "MAJ Webteam : " + nomNewVersion.substring(nomNewVersion.indexOf("v"), nomNewVersion.indexOf(".apk")).replace("_", "."));
//    		
//    		contentView.setProgressBar(R.id.pb_notification_mise_a_jour, 100, 0, true);
//    		myNotification.contentView = contentView;
//    		
//    		//On envoie la notification :
//    		myNotificationManager.notify(NOTIFICATION_NEW_VERSION, myNotification);
//			myDownloadMAJ.execute(this);
//		}
//		else {
//			L.v("Root", "Et bim ! Le singleton !");
//		}
//    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.root_, menu);
		return true;
	}
    
    private int CODE_RETOUR = 1;
    
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.itemPreferences:
			Intent monIntent = new Intent(this,Preferences.class);
			startActivityForResult(monIntent, CODE_RETOUR);
		return true;
		case R.id.itemProfil:
			clearInfoLog();
		return true;
		case R.id.itemCredit:
			startActivity(new Intent(this,Credit.class));
		return true;
		case R.id.itemHistorique:
			startActivity(new Intent(this,Historique.class));
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
	
//	public void actualizeNotification(final int pourcentage, final int tailleKo, final int downloadKo) {
//		
//		if (pourcentage == 100) {
//			PendingIntent contentIntent = PendingIntent.getActivity(this, 0, myPromptInstall, 0);
//    		myNotification.setLatestEventInfo(getApplicationContext(), "Webteam", "Webteam : MAJ", contentIntent);
//    		myNotification.flags |= Notification.FLAG_AUTO_CANCEL;
//    		myNotification.flags ^= Notification.FLAG_ONGOING_EVENT;
//		}
//		
//		contentView.setProgressBar(R.id.pb_notification_mise_a_jour, 100, pourcentage, false);
//		
//		if (tailleKo >= 1000) {
//			float newTailleMo = (float)tailleKo / 1000;
//			if (downloadKo >= 1000) {
//				float newDownloadMo = (float)downloadKo / 1000;
//				contentView.setTextViewText(R.id.tv_notification_mise_a_jour_pourcentage, newDownloadMo + "Mo / " + newTailleMo + "Mo");
//			}
//			else
//				contentView.setTextViewText(R.id.tv_notification_mise_a_jour_pourcentage, downloadKo + "ko / " + newTailleMo + "Mo");
//		}
//		else
//			if (downloadKo >= 1000) {
//				float newDownloadMo = (float)downloadKo / 1000;
//				contentView.setTextViewText(R.id.tv_notification_mise_a_jour_pourcentage, newDownloadMo + "Mo / " + tailleKo + "Ko");
//			}
//			else
//				contentView.setTextViewText(R.id.tv_notification_mise_a_jour_pourcentage, downloadKo + "ko / " + tailleKo + "Ko");
//		myNotification.contentView = contentView;
//		
//		
//		
//		myNotificationManager.notify(NOTIFICATION_NEW_VERSION, myNotification);
//	}
	
	@Override
	public String getStringForWaiting() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfIncrementForMyProgressDialog() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void incrementMyProgressDialog() {
		// TODO Auto-generated method stub
		
	}

	private void refreshComponentView() {
		Animation aButtonConnexionCaligula0To1 = new TranslateAnimation(0, 0, 280, 0);
		aButtonConnexionCaligula0To1.setDuration(1000);
		aButtonConnexionCaligula0To1.setFillAfter(false);
		aButtonConnexionCaligula0To1.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.VISIBLE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.GONE);
//				bPassConnexionClubPhoto.setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.VISIBLE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.GONE);
//				bPassConnexionClubPhoto.setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.VISIBLE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.GONE);
//				bPassConnexionClubPhoto.setVisibility(View.GONE);
			}
		});

		Animation aTextViewPseudoPasswordSeSouvenir0To1 = new TranslateAnimation(0, -600, 0, 0);
		aTextViewPseudoPasswordSeSouvenir0To1.setDuration(1000);
		aTextViewPseudoPasswordSeSouvenir0To1.setFillAfter(false);
		aTextViewPseudoPasswordSeSouvenir0To1.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				tvPseudoWebteam.setVisibility(View.GONE);
				tvPasswordWebteam.setVisibility(View.GONE);
				tvSeSouvenirWebteam.setVisibility(View.GONE);
			}
		});

		Animation aEditTextAndCheckBoxPseudoPasswordSeSouvenir0To1 = new TranslateAnimation(0, 600, 0, 0);
		aEditTextAndCheckBoxPseudoPasswordSeSouvenir0To1.setDuration(1000);
		aEditTextAndCheckBoxPseudoPasswordSeSouvenir0To1.setFillAfter(false);
		aEditTextAndCheckBoxPseudoPasswordSeSouvenir0To1.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				etPseudoWebteam.setVisibility(View.GONE);
				etPasswordWebteam.setVisibility(View.GONE);
				cbSeSouvenirWebteam.setVisibility(View.GONE);
			}
		});
		
		Animation aButtonConnexionWebteam1To0 = new TranslateAnimation(0, 0, -280, 0);
		aButtonConnexionWebteam1To0.setDuration(1000);
		aButtonConnexionWebteam1To0.setFillAfter(false);
		aButtonConnexionWebteam1To0.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				bConnexionWebteam.setVisibility(View.VISIBLE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.GONE);
//				bPassConnexionClubPhoto.setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				bConnexionWebteam.setVisibility(View.VISIBLE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.GONE);
//				bPassConnexionClubPhoto.setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				bConnexionWebteam.setVisibility(View.VISIBLE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.GONE);
//				bPassConnexionClubPhoto.setVisibility(View.GONE);
			}
		});

		Animation aTextViewPseudoPasswordSeSouvenir1To0 = new TranslateAnimation(-600, 0, 0, 0);
		aTextViewPseudoPasswordSeSouvenir1To0.setDuration(1000);
		aTextViewPseudoPasswordSeSouvenir1To0.setFillAfter(false);
		aTextViewPseudoPasswordSeSouvenir1To0.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				tvPseudoWebteam.setVisibility(View.VISIBLE);
				tvPasswordWebteam.setVisibility(View.VISIBLE);
				tvSeSouvenirWebteam.setVisibility(View.VISIBLE);
			}
		});

		Animation aEditTextAndCheckBoxPseudoPasswordSeSouvenir1To0 = new TranslateAnimation(600, 0, 0, 0);
		aEditTextAndCheckBoxPseudoPasswordSeSouvenir1To0.setDuration(1000);
		aEditTextAndCheckBoxPseudoPasswordSeSouvenir1To0.setFillAfter(false);
		aEditTextAndCheckBoxPseudoPasswordSeSouvenir1To0.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				etPseudoWebteam.setVisibility(View.VISIBLE);
				etPasswordWebteam.setVisibility(View.VISIBLE);
				cbSeSouvenirWebteam.setVisibility(View.VISIBLE);
			}
		});

		Animation aButtonConnexionCaligula2To1 = new TranslateAnimation(0, 0, 530, 0);
		aButtonConnexionCaligula2To1.setDuration(1000);
		aButtonConnexionCaligula2To1.setFillAfter(false);
		aButtonConnexionCaligula2To1.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.VISIBLE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.GONE);
//				bPassConnexionClubPhoto.setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.VISIBLE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.GONE);
//				bPassConnexionClubPhoto.setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.VISIBLE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.GONE);
//				bPassConnexionClubPhoto.setVisibility(View.GONE);
			}
		});

		Animation aButtonConnexionSquirrel1To2 = new TranslateAnimation(0, 0, -530, 0);
		aButtonConnexionSquirrel1To2.setDuration(1000);
		aButtonConnexionSquirrel1To2.setFillAfter(false);
		aButtonConnexionSquirrel1To2.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.VISIBLE);
				bConnexionClubPhoto.setVisibility(View.GONE);
//				bPassConnexionClubPhoto.setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.VISIBLE);
				bConnexionClubPhoto.setVisibility(View.GONE);
//				bPassConnexionClubPhoto.setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.VISIBLE);
				bConnexionClubPhoto.setVisibility(View.GONE);
//				bPassConnexionClubPhoto.setVisibility(View.GONE);
			}
		});
		
		Animation aButtonConnexionClubPhoto2To3 = new TranslateAnimation(0, 0, 250, 0);
		aButtonConnexionClubPhoto2To3.setDuration(1000);
		aButtonConnexionClubPhoto2To3.setFillAfter(false);
		aButtonConnexionClubPhoto2To3.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.VISIBLE);
			}
		});
		
//		Animation aButtonPassConnexionClubPhoto2To3 = new TranslateAnimation(0, 0, 300, 0);
//		aButtonPassConnexionClubPhoto2To3.setDuration(1000);
//		aButtonPassConnexionClubPhoto2To3.setFillAfter(false);
//		aButtonPassConnexionClubPhoto2To3.setAnimationListener(new AnimationListener() {
//			
//			@Override
//			public void onAnimationStart(Animation animation) {
//				bPassConnexionClubPhoto.setVisibility(View.VISIBLE);
//			}
//			
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//				bPassConnexionClubPhoto.setVisibility(View.VISIBLE);
//			}
//			
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				bPassConnexionClubPhoto.setVisibility(View.VISIBLE);
//			}
//		});
		
		Animation aButtonConnexionSquirrel3To2 = new TranslateAnimation(0, 0, -250, 0);
		aButtonConnexionSquirrel3To2.setDuration(1000);
		aButtonConnexionSquirrel3To2.setFillAfter(false);
		aButtonConnexionSquirrel3To2.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.VISIBLE);
				bConnexionClubPhoto.setVisibility(View.GONE);
//				bPassConnexionClubPhoto.setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.VISIBLE);
				bConnexionClubPhoto.setVisibility(View.GONE);
//				bPassConnexionClubPhoto.setVisibility(View.GONE);
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.VISIBLE);
				bConnexionClubPhoto.setVisibility(View.GONE);
//				bPassConnexionClubPhoto.setVisibility(View.GONE);
			}
		});
		
		Animation aButtonConnexionClubPhoto3To4 = new TranslateAnimation(0, 0, 0, 400);
		aButtonConnexionClubPhoto3To4.setDuration(1000);
		aButtonConnexionClubPhoto3To4.setFillAfter(false);
		aButtonConnexionClubPhoto3To4.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.GONE);
			}
		});
		
//		Animation aButtonPassConnexionClubPhoto3To4 = new TranslateAnimation(0, 0, 0, 400);
//		aButtonPassConnexionClubPhoto3To4.setDuration(1000);
//		aButtonPassConnexionClubPhoto3To4.setFillAfter(false);
//		aButtonPassConnexionClubPhoto3To4.setAnimationListener(new AnimationListener() {
//			
//			@Override
//			public void onAnimationStart(Animation animation) {
//				bPassConnexionClubPhoto.setVisibility(View.VISIBLE);
//			}
//			
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//				bPassConnexionClubPhoto.setVisibility(View.VISIBLE);
//			}
//			
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				bPassConnexionClubPhoto.setVisibility(View.GONE);
//			}
//		});
		
		Animation aButtonConnexionClubPhoto4To3 = new TranslateAnimation(0, 0, 400, 0);
		aButtonConnexionClubPhoto4To3.setDuration(1000);
		aButtonConnexionClubPhoto4To3.setFillAfter(false);
		aButtonConnexionClubPhoto4To3.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				bConnexionWebteam.setVisibility(View.GONE);
				bConnexionCaligula.setVisibility(View.GONE);
				bConnexionSquirrel.setVisibility(View.GONE);
				bConnexionClubPhoto.setVisibility(View.VISIBLE);
			}
		});
		
//		Animation aButtonPassConnexionClubPhoto4To3 = new TranslateAnimation(0, 0, 400, 0);
//		aButtonPassConnexionClubPhoto4To3.setDuration(1000);
//		aButtonPassConnexionClubPhoto4To3.setFillAfter(false);
//		aButtonPassConnexionClubPhoto4To3.setAnimationListener(new AnimationListener() {
//			
//			@Override
//			public void onAnimationStart(Animation animation) {
//				bPassConnexionClubPhoto.setVisibility(View.VISIBLE);
//			}
//			
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//				bPassConnexionClubPhoto.setVisibility(View.VISIBLE);
//			}
//			
//			@Override
//			public void onAnimationEnd(Animation animation) {
//				bPassConnexionClubPhoto.setVisibility(View.VISIBLE);
//			}
//		});
		
		switch (destX) {
		case 0:
			tvTitrePanneauWebteam.setText("Webteam");
			
			if (oldDestX == 1) {
				tvPseudoWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir1To0);
				etPseudoWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir1To0);
				tvPasswordWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir1To0);
				etPasswordWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir1To0);
				tvSeSouvenirWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir1To0);
				cbSeSouvenirWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir1To0);
				bConnexionWebteam.startAnimation(aButtonConnexionWebteam1To0);
				bConnexionCaligula.clearAnimation();
				bConnexionSquirrel.clearAnimation();
				oldDestX = 0;
			}
			break;
		case 1:
			tvTitrePanneauWebteam.setText("Caligula");

			if (oldDestX == 0) {
				tvPseudoWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir0To1);
				etPseudoWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir0To1);
				tvPasswordWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir0To1);
				etPasswordWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir0To1);
				tvSeSouvenirWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir0To1);
				cbSeSouvenirWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir0To1);
				bConnexionWebteam.clearAnimation();
				bConnexionCaligula.startAnimation(aButtonConnexionCaligula0To1);
				bConnexionSquirrel.clearAnimation();
				oldDestX = 1;
			}
			if (oldDestX == 2) {
				tvPseudoWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir0To1);
				etPseudoWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir0To1);
				tvPasswordWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir0To1);
				etPasswordWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir0To1);
				tvSeSouvenirWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir0To1);
				cbSeSouvenirWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir0To1);
				bConnexionWebteam.clearAnimation();
				bConnexionCaligula.startAnimation(aButtonConnexionCaligula2To1);
				bConnexionSquirrel.clearAnimation();
				oldDestX = 1;
			}
			break;
		case 2:
			tvTitrePanneauWebteam.setText("WebMail");

			if (oldDestX == 1) {
				tvPseudoWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir1To0);
				etPseudoWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir1To0);
				tvPasswordWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir1To0);
				etPasswordWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir1To0);
				tvSeSouvenirWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir1To0);
				cbSeSouvenirWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir1To0);
				bConnexionWebteam.clearAnimation();
				bConnexionCaligula.clearAnimation();
				bConnexionSquirrel.startAnimation(aButtonConnexionSquirrel1To2);
				oldDestX = 2;
			}
			if (oldDestX == 3) {
				bConnexionWebteam.clearAnimation();
				bConnexionCaligula.clearAnimation();
				bConnexionSquirrel.startAnimation(aButtonConnexionSquirrel3To2);
				bConnexionClubPhoto.clearAnimation();
//				bPassConnexionClubPhoto.clearAnimation();
				oldDestX = 2;
			}
			break;
		case 3:
			tvTitrePanneauWebteam.setText("Club Photo");
			
			if (oldDestX == 2) {
				tvPseudoWebteam.clearAnimation();
				etPseudoWebteam.clearAnimation();
				tvPasswordWebteam.clearAnimation();
				etPasswordWebteam.clearAnimation();
				tvSeSouvenirWebteam.clearAnimation();
				cbSeSouvenirWebteam.clearAnimation();
				bConnexionWebteam.clearAnimation();
				bConnexionCaligula.clearAnimation();
				bConnexionSquirrel.clearAnimation();
				bConnexionClubPhoto.startAnimation(aButtonConnexionClubPhoto2To3);
//				bPassConnexionClubPhoto.startAnimation(aButtonPassConnexionClubPhoto2To3);
				oldDestX = 3;
			}
			if (oldDestX == 4) {
				tvPseudoWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir1To0);
				etPseudoWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir1To0);
				tvPasswordWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir1To0);
				etPasswordWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir1To0);
				tvSeSouvenirWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir1To0);
				cbSeSouvenirWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir1To0);
				bConnexionWebteam.clearAnimation();
				bConnexionCaligula.clearAnimation();
				bConnexionSquirrel.clearAnimation();
				bConnexionClubPhoto.startAnimation(aButtonConnexionClubPhoto4To3);
//				bPassConnexionClubPhoto.startAnimation(aButtonPassConnexionClubPhoto4To3);
				oldDestX = 3;
			}
			break;
		case 4:
			tvTitrePanneauWebteam.setText("AIEnsea");

			if (oldDestX == 3) {
				tvPseudoWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir0To1);
				etPseudoWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir0To1);
				tvPasswordWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir0To1);
				etPasswordWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir0To1);
				tvSeSouvenirWebteam.startAnimation(aTextViewPseudoPasswordSeSouvenir0To1);
				cbSeSouvenirWebteam.startAnimation(aEditTextAndCheckBoxPseudoPasswordSeSouvenir0To1);
				bConnexionWebteam.clearAnimation();
				bConnexionCaligula.clearAnimation();
				bConnexionSquirrel.clearAnimation();
				bConnexionClubPhoto.startAnimation(aButtonConnexionClubPhoto3To4);
//				bPassConnexionClubPhoto.startAnimation(aButtonPassConnexionClubPhoto3To4);
				oldDestX = 4;
			}
			break;
		}
		initEditText();
	}
	
	/*
	 * Gestion du nouveau fond dynamique :
	 */

	private static final String NAME_IMAGE = "/res/drawable-hdpi/fond_480_800x5.png";
	private static final int WIDTH_PX = 480;
	private static final int HEIGHT_PX = 800;
	private static final int COUNT_DISPLAY = 5;
	
	private static final Bitmap SOURCE_BITMAP = BitmapFactory.decodeStream(Root.class.getResourceAsStream(NAME_IMAGE));
	
	ViewGroup vg = null;
	
	int widthScreen = 0;
	
	private BufferBitmapToDisplay threadManagerBitmap = new BufferBitmapToDisplay();
	private int oldDestX = 0;
	private int destX = 0;
	private int moveX = 0;
	private int offsetX = 0;
	private int startX = 0;
	private Bitmap previous = null;
	
	private void moveBackground(int moveX) {
		offsetX -= moveX;
		if (offsetX + WIDTH_PX > SOURCE_BITMAP.getWidth()) {
			offsetX = WIDTH_PX * (COUNT_DISPLAY - 1);
		}
		else if (offsetX < 0) {
			offsetX = 0;
		}
		threadManagerBitmap.addBitmapIntoBuffer(offsetX);
	}
	
	private void fixBackground(int getX, boolean moveFingerToLeft) {
		if (getX < widthScreen / 3) {
			if (moveFingerToLeft) {
				oldDestX = destX;
				destX++;
			}
			else {
				// Do nothing.
			}
		}
		else if (getX < 2 * widthScreen / 3) {
			if (moveFingerToLeft) {
				oldDestX = destX;
				destX++;
			}
			else {
				oldDestX = destX;
				destX--;
			}
		}
		else {
			if (moveFingerToLeft) {
				// Do nothing.
			}
			else {
				oldDestX = destX;
				destX--;
			}
		}
		if (destX < 0) {
			oldDestX = 0;
			destX = 0;
		}
		else if (destX > COUNT_DISPLAY - 1) {
			oldDestX = COUNT_DISPLAY - 1;
			destX = COUNT_DISPLAY - 1;
		}
		refreshComponentView();
		int offsetXInit = offsetX;
		offsetX = destX * WIDTH_PX;
		int delta = (offsetX - offsetXInit) / 10;
		for (int i = 0; i < 10; i++) {
			if (i < 9) {
				offsetXInit += delta;
				threadManagerBitmap.addBitmapIntoBuffer(offsetXInit);
			}
			else {
				threadManagerBitmap.addBitmapIntoBuffer(offsetX);
			}
		}
	}

	class BufferBitmapToDisplay extends Thread {

		boolean cancel = false;
		
		
		private List<Integer> bufferOffsetX = new ArrayList<Integer>();
		
		@Override
		public void run() {
			super.run();
			
			while (!cancel) {
				synchronized (bufferOffsetX) {
					if (bufferOffsetX.size() > 0) {
						final int offsetX = bufferOffsetX.get(0);
						bufferOffsetX.remove(0);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								displayImg(SOURCE_BITMAP, offsetX, 0, WIDTH_PX, HEIGHT_PX, null, false);
								//displayImg(Bitmap.createBitmap(SOURCE_BITMAP, offsetX, 0, WIDTH_PX, HEIGHT_PX, null, false));
							}
						});
						
					}
				}
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void clear() {
			synchronized (bufferOffsetX) {
				bufferOffsetX.clear();
			}
		}
		
		public void addBitmapIntoBuffer(int offsetX) {
			synchronized (bufferOffsetX) {
				bufferOffsetX.add(offsetX);
			}
		}
		
		public void cancel() {
			cancel = true;
		}
	}

	public void displayImg(Bitmap sourceBitmap, int offsetX, int offsetY, int widthPx, int heightPx, Matrix m, boolean b) {
	//public void displayImg(Bitmap bitmap) {
		if (previous != null) {
			previous.recycle();
		}
		previous = Bitmap.createBitmap(sourceBitmap, offsetX, offsetY, widthPx, heightPx, m, b);
		Drawable d = new BitmapDrawable(getResources(), previous);
		vg.setBackgroundDrawable(d);
	}

	@Override
	public void update(Object observable, Object data) {
		if (observable == myConnexionSquirrel) {
			if (myProgressDialog != null) {
				myProgressDialog.dismiss();
			}
			try {
				switch (myConnexionSquirrel.get()) {
				case RESULT_CONNEXION_KO:
					Toast.makeText(this, "Erreur dans les login/mot de passe...", Toast.LENGTH_SHORT).show();
					break;
				case RESULT_CONNEXION_OK:
					
					SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
					Editor editor = preference.edit();
					editor.putString(Webteam.PSEUDO_WEBMAIL, etPseudoWebteam.getText().toString());
					editor.putString(Webteam.PASSWORD_WEBMAIL, etPasswordWebteam.getText().toString());
					editor.commit();
					
					Toast.makeText(this, "Connexion réussis, chargement des emails...", Toast.LENGTH_SHORT).show();
					Intent monIntent = new Intent(this, BoiteDeMessageEmail.class);
					startActivity(monIntent);
					break;
				}
			} catch (InterruptedException e) {
				Toast.makeText(this, "Problème lors de la connexion...", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			} catch (ExecutionException e) {
				Toast.makeText(this, "Problème lors de la connexion...", Toast.LENGTH_SHORT).show();
				e.printStackTrace();
			}
		}
		else if (observable == myConnexionClubPhoto) {
			if (myProgressDialog != null) {
				myProgressDialog.dismiss();
			}
			boolean isLoggedToClubPhoto = false;
			try {
				isLoggedToClubPhoto = myConnexionClubPhoto.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
			if (isLoggedToClubPhoto) {
				SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(this);
				Editor editor = preference.edit();
				editor.putString(Webteam.PSEUDO_CLUB_PHOTO, etPseudoWebteam.getText().toString());
				editor.putString(Webteam.PASSWORD_CLUB_PHOTO, etPasswordWebteam.getText().toString());
				editor.commit();
				
				Intent monIntent = new Intent(this, ActClubPhoto.class);
				startActivity(monIntent);
			}
			else {
				Toast.makeText(this, "Mauvais loggin :(", Toast.LENGTH_SHORT).show();
			}
		}
	}
}


