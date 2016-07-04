package org.example.webteam.caligula;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.example.webteam.Preferences;
import org.example.webteam.R;
import org.example.webteam.R.anim;
import org.example.webteam.R.array;
import org.example.webteam.R.id;
import org.example.webteam.R.layout;
import org.example.webteam.R.menu;
import org.example.webteam.anniversaire.Anniversaire;

import potoman.tools.CallService;
import potoman.tools.L;
import potoman.tools.ManipOnTime;
import potoman.tools.UrlService;
import potoman.webteam.bdd.BddCaligulaManager;
import potoman.webteam.bdd.BddUrlCaligulaManager;
import potoman.webteam.constant.Webteam;
import potoman.webteam.exception.ExceptionCaligula;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class Caligula extends Activity implements OnItemClickListener {

	public static final String PREFS_NAME = "MyPrefsFileWebteam";
	private int mYear;
    private int mMonth;
    private int mDay;
    static final int DATE_DIALOG_ID = 0;
    
    private static int CODE_RETOUR = 1;
    
	Object data = null;
    
    private ViewFlipper vfCaligula = null;
	private ListView lvCaligulaWW = null;	//Which Week
	private CoursWWAdapter adapterWW = null;
	private ListView lvCaligulaWD = null;	//Which Day
	private CoursWDAdapter adapterWD = null;
	private ListView lvCaligulaWC = null;	//Day
	private CoursWCAdapter adapterWC = null;

	private int weekSelected = 0;
	private int daySelected = 0;
	private int classeSelected = 0;
	
	public List<Semaine> maListeDeSemaine = null;
	public List<Jour> maListeDeJour = null;
	public List<Cours> maListeDeCoursForDisplay = null;
	
	final Handler uiThreadCallback = new Handler();

    ProgressDialog myProgressDialog;
	
    //private List<String> listURL = new ArrayList<String>();
//	private HashMap<String, String> mapUrlStagiaire = new HashMap<String, String>();
//	private HashMap<String, String> mapUrlProfesseur = new HashMap<String, String>();

	SparseBooleanArray displayInfo = null;
	
	public final static String askForOpenStagiaire = "http://caligula.ensea.fr/ade/standard/gui/tree.jsp?category=trainee&expand=false&forceLoad=false&reload=false&scroll=0";
	public final static String askForOpenInstructeur = "http://caligula.ensea.fr/ade/standard/gui/tree.jsp?category=instructor&expand=false&forceLoad=false&reload=false&scroll=0";
//	public final static String askForOpen1A = "http://caligula.ensea.fr/ade/standard/gui/tree.jsp?branchId=62&expand=false&forceLoad=false&reload=false&scroll=0";
//	public final static String askForOpen1B = "http://caligula.ensea.fr/ade/standard/gui/tree.jsp?branchId=63&expand=false&forceLoad=false&reload=false&scroll=0";
	
	//public final static String askForOpen1AG1TD1 = "http://caligula.ensea.fr/ade/standard/gui/tree.jsp?branchId=72&expand=false&forceLoad=false&reload=false&scroll=0";
	
	public final static String askForWeek = "http://caligula.ensea.fr/ade/custom/modules/plannings/info.jsp?week="; //bounds
	
	public final static String askForDay = "http://caligula.ensea.fr/ade/custom/modules/plannings/pianoDays.jsp?day=";
	
	//public final static String askForImage = "http://caligula.ensea.fr/ade/custom/modules/plannings/imagemap.jsp?";
	
	public final static String askForOptionBefore1 = "http://caligula.ensea.fr/ade/standard/gui/menu.jsp?on=Planning&sub=Options&href=/custom/modules/plannings/appletparams.jsp&target=main&tree=false";
	public final static String askForOptionBefore2 = "http://caligula.ensea.fr/ade/custom/modules/plannings/appletparams.jsp?";
	public final static String askForOption = "http://caligula.ensea.fr/ade/standard/gui/menu.jsp?on=Planning&sub=Afficher&href=/custom/modules/plannings/plannings.jsp&target=visu&tree=true";
	public final static String askForOptionAfter1 = "http://caligula.ensea.fr/ade/standard/gui/set_tree.jsp?href=/custom/modules/plannings/plannings.jsp&isClickable=true&showTabStage=true&showTree=true&showTabRooms=true&showTabDuration=true&displayConfId=1&showTabHour=true&showTabInstructors=true&showTabActivity=true&showPianoDays=true&showTreeCategory8=true&y=8&showTreeCategory7=true&displayType=0&x=49&showTab=true&showTreeCategory6=true&showTreeCategory5=true&showTreeCategory4=true&showTreeCategory3=true&showTreeCategory2=true&showTabTrainees=true&showTreeCategory1=true&display=true&showPianoWeeks=true&showLoad=false&showTabResources=true&showTabDate=true&changeOptions=true&target=visu";
	public final static String askForOptionAfter2 = "http://caligula.ensea.fr/ade/standard/gui/tree.jsp?noLoad=true";
	public final static String askForOptionAfter3 = "http://caligula.ensea.fr/ade/custom/modules/plannings/linksSelectWeeks.jsp";
	public final static String askForOptionAfter4 = "http://caligula.ensea.fr/ade/custom/modules/plannings/plannings.jsp?isClickable=true&showTabStage=true&showTree=true&showTabRooms=true&showTabDuration=true&displayConfId=1&showTabHour=true&showTabActivity=true&showTabInstructors=true&showPianoDays=true&y=8&showTreeCategory8=true&showTab=true&x=49&displayType=0&showTreeCategory7=true&showTreeCategory6=true&showTreeCategory5=true&showTreeCategory4=true&showTreeCategory3=true&showTabTrainees=true&showTreeCategory2=true&showTreeCategory1=true&showPianoWeeks=true&display=true&showTabDate=true&showTabResources=true&showLoad=false&changeOptions=true";
	public final static String askForOptionAfter5 = "http://caligula.ensea.fr/ade/custom/modules/plannings/pianoDays.jsp?forceLoad=true";
	public final static String askForOptionAfter6 = "http://caligula.ensea.fr/ade/custom/modules/plannings/pianoWeeks.jsp?forceLoad=true";
	public final static String askForOptionAfter7 = "http://caligula.ensea.fr/ade/custom/modules/plannings/info.jsp";
	
	public final static String askForAllWeek = "http://caligula.ensea.fr/ade/custom/modules/plannings/pianoWeeks.jsp?searchWeeks=all";
	private static final String PROJECT_ID = "projectId";
	
	private DefaultHttpClient httpClient = null;
	
	private List<String> date = new ArrayList<String>();
	private List<String> heure = new ArrayList<String>();
	private List<String> duree = new ArrayList<String>();
	private List<String> activite = new ArrayList<String>();
	private List<Integer> stagiaire = new ArrayList<Integer>();
	private List<String> formateur = new ArrayList<String>();
	private List<String> salle = new ArrayList<String>();
	
	private List<Cours> maListeDeCours = null;
	

    private BddCaligulaManager caligulaBdd = null;
    private BddUrlCaligulaManager urlCaligulaBdd = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		
		setContentView(R.layout.caligula);
	    this.setTitle("Caligula");
	    
//    	if (getTimeReference().equals("") || getTimeReference().equals("0")) {
//        	final Calendar c = Calendar.getInstance();
//            mYear = c.get(Calendar.YEAR);
//            mMonth = c.get(Calendar.MONTH);
//            mDay = c.get(Calendar.DAY_OF_MONTH);
//            setTimeReference();
//    	}
//    	else {
    		L.v("Caligula", "TimeReference : " + getTimeReference());
//    	}
    	
    	vfCaligula = (ViewFlipper)findViewById(R.id.vfCaligula);
	    lvCaligulaWW = (ListView)findViewById(R.id.lvCaligulaWW);
	    lvCaligulaWD = (ListView)findViewById(R.id.lvCaligulaWD);
	    lvCaligulaWC = (ListView)findViewById(R.id.lvCaligulaWC);
	    
		caligulaBdd = new BddCaligulaManager(this);
		caligulaBdd.open();
		urlCaligulaBdd = new BddUrlCaligulaManager(this);
		urlCaligulaBdd.open();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		displayInfo = new SparseBooleanArray();
		displayInfo.put(0, prefs.getBoolean("caligula_heure", true));
		displayInfo.put(1, prefs.getBoolean("caligula_date", true));
		displayInfo.put(1, prefs.getBoolean("caligula_duree", true));
		displayInfo.put(2, prefs.getBoolean("caligula_matiere", true));
		displayInfo.put(3, prefs.getBoolean("caligula_salle", true));
		displayInfo.put(4, prefs.getBoolean("caligula_prof", true));
		classeSelected = Integer.parseInt(prefs.getString("listPref", "1"));
		if (urlCaligulaBdd.getUrlCaligula(classeSelected) == null) {
			Toast.makeText(this, "Vous devez avoir été dans les préférences de l'application avant d'utiliser Caligula. Appuyez sur la touche \"menu\".", Toast.LENGTH_LONG).show();
			finish();
		}
		else {
			this.setTitle("Caligula > " + urlCaligulaBdd.getUrlCaligula(classeSelected).getName());
			 maListeDeCours = caligulaBdd.getAllCours(classeSelected);
			 if (maListeDeCours == null) {
				//caligulaBdd.clear();
			    httpClient = new DefaultHttpClient();
			    CallService.setParam(httpClient, false);
			    if (CallService.isConnected(this))
			    	getAllCours();
			    else {
			    	Toast.makeText(this, getResources().getStringArray(R.array.exception)[0], Toast.LENGTH_SHORT).show();
			    	finish();
			    }
			}
			else {
	//			String maString = prefs.getString("listPref", "0");
	//			L.v("Caligula", "Pr�f�rence : " + maString);
	//			maListeDeCours = caligulaBdd.getAllCours(Integer.parseInt(maString));
				buildTree();
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		caligulaBdd.close();
		urlCaligulaBdd.close();
	}
	
	private void getAllCours() {
		myProgressDialog = new ProgressDialog(Caligula.this);
    	myProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    	//myProgressDialog.setMax(listURL.size() + 4);
    	myProgressDialog.setMax(10);
    	myProgressDialog.setTitle(getResources().getStringArray(R.array.waitingForCaligula)[0]);
		myProgressDialog.setMessage(Webteam.getPhraseDAmbiance());
    	myProgressDialog.setCancelable(true);
    	myProgressDialog.show();
    	
    	new Thread() {
       		@Override public void run() {
    			connexion(httpClient, myProgressDialog);
    			HttpResponse response = askForSomething(askForOpenStagiaire, httpClient);
    			try {
    				EntityUtils.toString(response.getEntity());
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
       			myProgressDialog.incrementProgressBy(1);
       			response = askForSomething(askForOpenInstructeur, httpClient);
       			try {
    				EntityUtils.toString(response.getEntity());
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
   				myProgressDialog.incrementProgressBy(1);
   				L.v("Cligula", "URL num�ro : " + classeSelected);
   				response = askForSomething(urlCaligulaBdd.getUrlCaligula(classeSelected).getUrl(), httpClient);
   				try {
    				EntityUtils.toString(response.getEntity());
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
   				myProgressDialog.incrementProgressBy(1);
       			response = askForSomething(askForOptionAfter7, httpClient);
       			try {
    				EntityUtils.toString(response.getEntity());
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
       			myProgressDialog.incrementProgressBy(1);
       			response = askForSomething(askForAllWeek, httpClient);
       			try {
    				EntityUtils.toString(response.getEntity());
    			} catch (Exception e) {
    				e.printStackTrace();
    			}
       			myProgressDialog.incrementProgressBy(1);
       			response = askForSomething(askForOptionAfter7, httpClient);
       			myProgressDialog.incrementProgressBy(1);
       		    Header[] headers = response.getAllHeaders();
       	        for (int j=0; j < headers.length; j++) {
       	            Header h = headers[j];
       	            L.i("Caligula|connexion", "Header names: "+h.getName());
       	            L.i("Caligula|connexion", "Header Value: "+h.getValue());
       	        }
       			if (fullingList(response, classeSelected)) {
       				fullingBdd(false);
       				resetList();
       			}
       			else
       				L.e("Caligula", "ERREUR : RETURN FALSE !!!");
       			myProgressDialog.incrementProgressBy(1);
				uiThreadCallback.post(new Runnable() {public void run() {buildTree();}});
	       	    if (myProgressDialog != null)
	       	    	myProgressDialog.dismiss();
	       	 try {
 				EntityUtils.toString(response.getEntity());
 			} catch (Exception e) {
 				e.printStackTrace();
 			}
       	    }
       	}.start();
	}
	
	private void buildTree() {
		try {
//		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//		String maClasse = prefs.getString("listPref", "0");
		maListeDeSemaine = new ArrayList<Semaine>();
		int numWeek = 0;
			//L.v("Caligula", "classe : " + maClasse);
			List<Cours> maListeDeCours = caligulaBdd.getAllCours(classeSelected);
			if (maListeDeCours != null) {
				for (int i = 0; i < maListeDeCours.size(); i++) {
					L.v("Caligula", "Date � trait�e : " + maListeDeCours.get(i).getDate());
					final GregorianCalendar myGC = ManipOnTime.stringOfDateToCalendar(maListeDeCours.get(i).getDate());
					
					L.d("Caligula", "Date format� : " + myGC.get(GregorianCalendar.DAY_OF_MONTH) + "/" + myGC.get(GregorianCalendar.MONTH) + "/" + myGC.get(GregorianCalendar.YEAR));
					numWeek = numWeekFromDate(myGC) + 1;
					if (numWeek < 1)
						throw new ExceptionCaligula(ExceptionCaligula.NUM_WEEK_MINUS_THAN_ONE);
					L.w("Caligula", "Num�ro de la semaine : " + numWeek);
					if (maListeDeSemaine.isEmpty()) {
						maListeDeSemaine.add(new Semaine(numWeek));
						L.d("Caligula", "ADD");
					}
					else
						if (maListeDeSemaine.get(maListeDeSemaine.size() - 1).getNumero() != numWeek) {
							maListeDeSemaine.add(new Semaine(numWeek));
							L.d("Caligula", "ADD");
						}
					L.i("Caligula" , "Verification que les jours tournent : " + myGC.get(GregorianCalendar.DAY_OF_WEEK));
					
					switch (myGC.get(GregorianCalendar.DAY_OF_WEEK)) {
					case GregorianCalendar.MONDAY:
						maListeDeSemaine.get(maListeDeSemaine.size() - 1).addCours(maListeDeCours.get(i), 0);
						break;
					case GregorianCalendar.TUESDAY:
						maListeDeSemaine.get(maListeDeSemaine.size() - 1).addCours(maListeDeCours.get(i), 1);
						break;
					case GregorianCalendar.WEDNESDAY:
						maListeDeSemaine.get(maListeDeSemaine.size() - 1).addCours(maListeDeCours.get(i), 2);
						break;
					case GregorianCalendar.THURSDAY:
						maListeDeSemaine.get(maListeDeSemaine.size() - 1).addCours(maListeDeCours.get(i), 3);
						break;
					case GregorianCalendar.FRIDAY:
						maListeDeSemaine.get(maListeDeSemaine.size() - 1).addCours(maListeDeCours.get(i), 4);
						break;
					case GregorianCalendar.SATURDAY:
						maListeDeSemaine.get(maListeDeSemaine.size() - 1).addCours(maListeDeCours.get(i), 5);
						break;
					case GregorianCalendar.SUNDAY:
						break;
					}
				}
			
				
			final Calendar c = Calendar.getInstance();
			int numWeekActuel = numWeekFromDate(new GregorianCalendar(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH))) + 1;
			int i = 0;
			

			for (i = 0; i < maListeDeSemaine.size(); i++) {
				if (maListeDeSemaine.get(i).getNumero() == numWeekActuel) {
					break;
				}
			}
			
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			boolean showOld = prefs.getBoolean("caligula_old_week", false);
			if (!showOld) {
				for (; i != 0; i--) {
					maListeDeSemaine.remove(0);
				}
			}
		    adapterWW = new CoursWWAdapter(getApplicationContext(), maListeDeSemaine, i, lvCaligulaWW);
		    
		    lvCaligulaWW.setAdapter(adapterWW);
			lvCaligulaWW.setOnItemClickListener(this);
//			lvCaligulaWW.scrollBy(0, 1);
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					adapterWW.scrollTo(itemToScroll);
//				}
//			}).start();
			
			}
			else {
				Toast.makeText(this, "Il n'y a pas d'emploi du temps pour ce groupe. Veuillez en sélectionner un autre.", Toast.LENGTH_LONG).show();
				if (adapterWW != null)
					adapterWW.clear();
			}
		}
		catch (ExceptionCaligula e) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
		}
	}
	
	public int numWeekFromDate(final GregorianCalendar myGC) {
		L.v("Caligula|numWeekFromDate", "numWeekFromDate: maDate : jour : " + myGC.get(GregorianCalendar.DAY_OF_MONTH) + ", mois : " + myGC.get(GregorianCalendar.MONTH) + ", ann�e : " + myGC.get(GregorianCalendar.YEAR));
		getTimeReference();
        int sommeJourActuel = 0;
        for (int i = 7; i < myGC.get(GregorianCalendar.MONTH); i++)
        	sommeJourActuel += Anniversaire.lagMonth[i];
        if (sommeJourActuel == 0)
        	for (int i = 7; i < 12; i++)
            	sommeJourActuel += Anniversaire.lagMonth[i];
        for (int i = 0; i < myGC.get(GregorianCalendar.MONTH) && myGC.get(GregorianCalendar.MONTH) < 7; i++)
        	sommeJourActuel += Anniversaire.lagMonth[i];
        
        
        sommeJourActuel += myGC.get(GregorianCalendar.DAY_OF_MONTH) + 1;
        L.v("Caligula|numWeekFromDate", "Somme des jours actuels : " + sommeJourActuel);
        L.v("Caligula|numWeekForDate", "numDay : " + myGC.get(GregorianCalendar.DAY_OF_WEEK));
        switch (myGC.get(GregorianCalendar.DAY_OF_WEEK)) {
        case GregorianCalendar.MONDAY:
        	//sommeJourActuel -= 5;
        	break;
        case GregorianCalendar.TUESDAY:
        	sommeJourActuel -= 1;
        	break;
        case GregorianCalendar.WEDNESDAY:
        	sommeJourActuel -= 2;
        	break;
        case GregorianCalendar.THURSDAY:
        	sommeJourActuel -= 3;
        	break;
        case GregorianCalendar.FRIDAY:
        	sommeJourActuel -= 4;
        	break;
        case GregorianCalendar.SATURDAY:
        	sommeJourActuel -= 5;
        	break;
        case GregorianCalendar.SUNDAY:
        	sommeJourActuel -= 6;
        	break;
        }
        int sommeJourPasse = 0;	
        L.v("Caligula|numWeekFromDate", "mMonth : " + mMonth + ", mDay : " + mDay);
        for (int i = 7; i < mMonth; i++)
        	sommeJourPasse += Anniversaire.lagMonth[i];
        for (int i = 0; i < mMonth && mMonth < 7; i++)
        //for (int i = 0; i < mMonth && i < 7; i++)
        	sommeJourPasse += Anniversaire.lagMonth[i];
        sommeJourPasse += mDay;
        L.v("Caligula|numWeekFromDate", "Somme des jours actuel : " + sommeJourActuel);
        L.v("Caligula|numWeekFromDate", "Somme des jours pass�s : " + sommeJourPasse);
        L.e("Caligula|numWeekFromDate", "Soustraction : " + (sommeJourActuel - sommeJourPasse));
        float myFloat = sommeJourActuel - sommeJourPasse;
        myFloat /= 7;
        return (int) Math.round(myFloat);
	}
	
	
	private boolean fullingList(HttpResponse response, int classe) {
		L.v("Caligula", "LECTURE DE LA REPONSE...");
		try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);//, 1000000);
			String ligne;
			while((ligne = bufferedReader.readLine()) != null) {
				L.v("Caligula", "ligne:" + ligne);
				if (ligne.length() > 10) {
					int index = 8;
					if (ligne.substring(0, index).equals("<tr><td>")) {
						
						L.v("Caligula", ligne.substring(index, ligne.indexOf("</td><td>", index)));
											
						date.add(ligne.substring(index, ligne.indexOf("</td><td>", index)).replace("&nbsp;", ", "));
		
				        index = ligne.indexOf("</td><td>", index) + 9;
				        //Heure
						L.v("Caligula", ligne.substring(index, ligne.indexOf("</td><td>", index)).replace("&nbsp;", ", "));
				        heure.add(ligne.substring(index, ligne.indexOf("</td><td>", index)).replace("&nbsp;", ", "));
				        
				        index = ligne.indexOf("</td><td>", index) + 9;
				        //Duree
						L.v("Caligula", ligne.substring(index, ligne.indexOf("</td><td>", index)).replace("&nbsp;", ", "));
				        duree.add(ligne.substring(index, ligne.indexOf("</td><td>", index)).replace("&nbsp;", ", "));
				        
				        index = ligne.indexOf(")\">", index) + 3;
				        //Activit�
				        activite.add(ligne.substring(index, ligne.indexOf("</a>", index)).replace("&nbsp;", ", "));
				
				        index = ligne.indexOf("</td><td>", index) + 9;
				        //Stagiaire
						L.v("Caligula", "" + classe);
				        stagiaire.add(classe);
				
				        index = ligne.indexOf("</td><td>", index) + 9;
				        //Formateur
						L.v("Caligula", ligne.substring(index, ligne.indexOf("</td><td>", index)).replace("&nbsp;", ", "));
				        formateur.add(ligne.substring(index, ligne.indexOf("</td><td>", index)).replace("&nbsp;", ", "));
				        
				        index = ligne.indexOf("</td><td>", index) + 9;
				        //Salle
						L.v("Caligula", ligne.substring(index, ligne.indexOf("</td><td>", index)).replace("&nbsp;", ", "));
				        salle.add(ligne.substring(index, ligne.indexOf("</td><td>", index)).replace("&nbsp;", ", "));
					}
				}
			}
			bufferedReader.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			L.e("Caligula", "ERREUR DANS LA LECTURE DE LA REPONSE !!! ");
		}
        L.v("Caligula", "TRAITEMENT TERMINE, " + date.size() + " entr�es !!!");
        return true;
	}
	
	private void fullingBdd(boolean toList) {
		L.v("Caligula", "fullingBdd");
		int size = date.size();
		Cours monCours = new Cours();
		for (int i = 0; i < size; i++) {
			monCours.setDate(date.get(i));
			monCours.setHeure(heure.get(i).replace(":", "h"));
			monCours.setDuree(duree.get(i));
			monCours.setActivite(activite.get(i));
			monCours.setStagiaire(stagiaire.get(i));
			monCours.setFormateur(formateur.get(i));
			monCours.setSalle(salle.get(i));
			L.v("Caligula", "INSERT !!!");
			caligulaBdd.insertCours(monCours);
			if (toList) {
				if (maListeDeCours == null)
					maListeDeCours = new ArrayList<Cours>();
				maListeDeCours.add(monCours);
			}
		}
	}
	
	private void resetList() {
		date.clear();
        heure.clear();
        duree.clear();
        activite.clear();
        stagiaire.clear();
        formateur.clear();
        salle.clear();
	}
	
	public static HttpResponse askForSomething(String url, DefaultHttpClient myDefaultHttpClient) {
	    L.v("Caligula", "Appel de : " + url);
		HttpPost httppost = new HttpPost(url);
		CallService.setParam(httppost, false);
	    try {
	    	CallService.setParam(myDefaultHttpClient, false);
//	    	HttpContext context = new BasicHttpContext();
//	    	myDefaultHttpClient.setRedirectHandler(new DefaultRedirectHandler() {
//			    @Override
//			    public URI getLocationURI(HttpResponse response,
//			                              HttpContext context) throws ProtocolException {
//			        L.v("location !!!", "plop");
//			        //Capture the Location header here
//			        L.v("location !!!", Arrays.toString(response.getHeaders("Location")));
//
//			        return super.getLocationURI(response,context);
//			    }
//			});
	    	
	    	HttpResponse response = myDefaultHttpClient.execute(httppost); //, context);
	    	L.v("status", "status code = " + response.getStatusLine().getStatusCode());
	    	L.v("status", "reason phrase = " + response.getStatusLine().getReasonPhrase());
//	    	HttpUriRequest currentReq = (HttpUriRequest) context.getAttribute( 
//	                ExecutionContext.HTTP_REQUEST);
//	        HttpHost currentHost = (HttpHost)  context.getAttribute( 
//	                ExecutionContext.HTTP_TARGET_HOST);
//	        if (currentReq != null && currentReq.getURI() != null) {
//		        String currentUrl = (currentReq.getURI().isAbsolute()) ? currentReq.getURI().toString() : (currentHost.toURI() + currentReq.getURI());
//		    	L.v("location !!!", currentUrl);
//		    	if (currentUrl.contains(PROJECT_ID)) {
//			    	response.addHeader(PROJECT_ID, currentUrl.substring(currentUrl.indexOf(PROJECT_ID) + PROJECT_ID.length() + 1));
//		    	}
//	        }
	    	CallService.setParam(response, false);
			return response;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	public static void connexion(DefaultHttpClient myDefaultHttpClient, ProgressDialog myProgressDialog) {
		Header[] headers = null;
		try {
	    L.v("Caligula", "Entre dans connexion");
    	HttpPost httppost = null;
    	
	    HttpResponse response = askForSomething(UrlService.URL_CALIGULA_ONE, myDefaultHttpClient);
	    try {
			EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    headers = response.getAllHeaders();
        for (int i = 0; i < headers.length; i++) {
            Header h = headers[i];
            L.i("Caligula|connexion", "Header names: " + h.getName());
            L.i("Caligula|connexion", "Header Value: " + h.getValue());
        }
	    response = askForSomething(UrlService.URL_CALIGULA_TWO, myDefaultHttpClient);
	    headers = response.getAllHeaders();
        for (int i = 0; i < headers.length; i++) {
            Header h = headers[i];
            L.i("Caligula|connexion", "Header names: " + h.getName());
            L.i("Caligula|connexion", "Header Value: " + h.getValue());
        }
	    try {
			EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    response = askForSomething(UrlService.URL_CALIGULA_TREE, myDefaultHttpClient);
	    headers = response.getAllHeaders();
        for (int i = 0; i < headers.length; i++) {
            Header h = headers[i];
            L.i("Caligula|connexion", "Header names: " + h.getName());
            L.i("Caligula|connexion", "Header Value: " + h.getValue());
        }
    	
        try {
			EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
        CallService.setParam(response, false);
        myProgressDialog.incrementProgressBy(1);
        
	    L.v("Caligula", "Appel de la deuxième url...");
	    CallService.setParam(myDefaultHttpClient, false);
	    myProgressDialog.incrementProgressBy(1);
	    
        try {
			EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        List<String> nameData = new ArrayList<String>();
		nameData.add("login");
		nameData.add("password");
		nameData.add("x");
		nameData.add("y");
		List<String> valeurData = new ArrayList<String>();
		valeurData.add("xxxxxxxxxx");
		valeurData.add("xxxxxxxxxx");
		valeurData.add("45");
		valeurData.add("10");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); //On crée la liste qui contiendra tous nos paramètres
    	for (int i = 0; i < nameData.size(); i++) {
    		nameValuePairs.add(new BasicNameValuePair(nameData.get(i), valeurData.get(i)));
    	}
        CallService.setParam(response, false);
    	httppost = new HttpPost(UrlService.URL_CALIGULA_FOR__CLICK);
    	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	    CallService.setParam(httppost, false);
    	
	    response = myDefaultHttpClient.execute(httppost);

	    headers = response.getAllHeaders();
	    L.v("Caligula", "locale = " + response.getLocale());
	    for (int i = 0; i < headers.length; i++) {
            Header h = headers[i];
            L.i("Caligula|connexion", "Header names: " + h.getName());
            L.i("Caligula|connexion", "Header Value: " + h.getValue());
        }
        
        try {
			EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	    response = askForSomething(UrlService.URL_CALIGULA_FIVE, myDefaultHttpClient);
	    
	    for (int i = 0; i < headers.length; i++) {
            Header h = headers[i];
            L.i("Caligula|connexion", "Header names: " + h.getName());
            L.i("Caligula|connexion", "Header Value: " + h.getValue());
        }
        
        try {
			EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	    response = askForSomething(UrlService.URL_CONNECTION_GO, myDefaultHttpClient);
        
	    headers = response.getAllHeaders();
	    L.v("Caligula", "locale = " + response.getLocale());
        for (int i = 0; i < headers.length; i++) {
            Header h = headers[i];
            L.i("Caligula|connexion", "Header names: " + h.getName());
            L.i("Caligula|connexion", "Header Value: " + h.getValue());
        }
        
        try {
			EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
        
	    response = askForSomething(askForOptionBefore1, myDefaultHttpClient);

        try {
			EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    response = askForSomething(askForOptionBefore2, myDefaultHttpClient);
	    

	    /*
	     * 
	     * Initialisation de l'affichage en tableau :
	     * 
	     */
	    
	    L.v("Caligula", "Ajout des variables");
        nameData.clear();
		nameData.add("changeOptions");
		nameData.add("display");
		nameData.add("displayConfId");
		nameData.add("displayType");
		nameData.add("isClickable");
		nameData.add("showLoad");
		nameData.add("showPianoDays");
		nameData.add("showPianoWeeks");
		nameData.add("showTab");
		nameData.add("showTabActivity");
		nameData.add("showTabDate");
		nameData.add("showTabDuration");
		nameData.add("showTabHour");
		nameData.add("showTabInstructors");
		nameData.add("showTabResources");
		nameData.add("showTabRooms");
		nameData.add("showTabStage");
		nameData.add("showTabTrainees");
		nameData.add("showTree");
		nameData.add("showTreeCategory1");
		nameData.add("showTreeCategory2");
		nameData.add("showTreeCategory3");
		nameData.add("showTreeCategory4");
		nameData.add("showTreeCategory5");
		nameData.add("showTreeCategory6");
		nameData.add("showTreeCategory7");
		nameData.add("showTreeCategory8");
		nameData.add("x");
		nameData.add("x");
		nameData.add("y");
		nameData.add("y");
		valeurData.clear();
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("1");
		valeurData.add("0");
		valeurData.add("true");
		valeurData.add("false");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("true");
		valeurData.add("30");
		valeurData.add("");
		valeurData.add("8");
		valeurData.add("");
		
		L.v("Caligula", "Création de la méthode post pour les options");
    	httppost = new HttpPost(askForOption);
    	CallService.setParam(httppost, false);
    	httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
    	
	    L.v("Caligula", "Appel de la deuxième url...");
	    CallService.setParam(myDefaultHttpClient, false);

        try {
			EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    response = myDefaultHttpClient.execute(httppost);	//Op ! On a récupèrer le cookie !
	    L.v("Caligula", "Appel done.");
	    CallService.setParam(response, false);
	    myProgressDialog.incrementProgressBy(1);
	    try {
			EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    
	    response = askForSomething(askForOptionAfter1, myDefaultHttpClient);
	    try {
			EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    response = askForSomething(askForOptionAfter2, myDefaultHttpClient);	//Récupèrer les adresses pour les stagiaires & les formateurs
	    try {
			EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    response = askForSomething(askForOptionAfter3, myDefaultHttpClient);
	    try {
			EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    response = askForSomething(askForOptionAfter4, myDefaultHttpClient);
	    try {
			EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    response = askForSomething(askForOptionAfter5, myDefaultHttpClient);
	    try {
			EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    response = askForSomething(askForOptionAfter6, myDefaultHttpClient);
	    try {
			EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
	    response = askForSomething(askForOptionAfter7, myDefaultHttpClient);
	    try {
			EntityUtils.toString(response.getEntity());
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
		catch (Exception e) {
			e.printStackTrace();
			L.e("Caligula", "ERREUR LORS DE LA CONNEXION !!!");
		}
	}
	
	public void lireReponse(HttpResponse response) {
        L.v("Caligula", "LECTURE DE LA REPONSE...");
		try {
		StringBuffer stringBuffer = null;
	    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
		stringBuffer = new StringBuffer();
		String ligne;
		String normal = "";
		while((ligne = bufferedReader.readLine()) != null) {
			stringBuffer.append(ligne);
			L.v("Caligula", "Ligne>" + ligne);
			normal = normal + ligne; 
		}
		bufferedReader.close();
		}
		catch (Exception e) {
			L.e("Caligula", "ERREUR DANS LA LECTURE DE LA REPONSE !!! ");
		}
        L.v("Caligula", "LECTURE TERMINE");
	}
	
	public String getTimeReference() {

		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String workString =  preferences.getString("firstMonday", "1/0/1970");
		L.v("Caligula", "time r�f�rence : " + workString);
		
		int indexOfFirstSlash = workString.indexOf("/");
		int indexOfSecondSlash = workString.indexOf("/", indexOfFirstSlash + 1);
		mDay = Integer.parseInt(workString.substring(0, indexOfFirstSlash));
		mMonth = Integer.parseInt(workString.substring(indexOfFirstSlash + 1, indexOfSecondSlash));
		mYear = Integer.parseInt(workString.substring(indexOfSecondSlash + 1, workString.length()));
		return mDay + "/" + mMonth + "/" + mYear;
	}
        
    //Touche "menu"
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.caligula_, menu);
    	return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		//Intent monIntent = null;
		switch (item.getItemId()) {
//		case R.id.menu_caligula_change_date:
//			showDialog(DATE_DIALOG_ID);
//			break;
		case R.id.menu_caligula_refresh:
			if (CallService.isConnected(this)) {
				caligulaBdd.clear();
				weekSelected = 0;
				daySelected = 0;
		    	if (vfCaligula.getCurrentView() == lvCaligulaWC) {
					vfCaligula.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
					vfCaligula.showPrevious();
					vfCaligula.showPrevious();
		    	} else
			    	if (vfCaligula.getCurrentView() == lvCaligulaWD) {
			    		vfCaligula.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
						vfCaligula.showPrevious();
			    	}
			    httpClient = new DefaultHttpClient();
			    CallService.setParam(httpClient, false);
				getAllCours();
			}
			else
				Toast.makeText(this, getResources().getStringArray(R.array.exception)[0], Toast.LENGTH_SHORT).show();
			break;
		case R.id.menu_caligula_preferences:
			Intent monIntent = new Intent(this,Preferences.class);
			startActivityForResult(monIntent, CODE_RETOUR);
			break;
		}
	return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public void onItemClick(AdapterView<?> adaptaterView, View view, int position,
			long arg3) {
		L.v("Caligula", "ItemClick");
		// TODO Auto-generated method stub
		L.v("Caligula", "Tag : " + view.getTag());
		if (((String)view.getTag()).equals("WW")) {
			L.v("Caligula", "onClickItem : next");
		    adapterWD = new CoursWDAdapter(getApplicationContext(), maListeDeSemaine.get(position).getMalisteDeJour(), (adapterWW.getSemaineActuelle() == position) ? true : false);
			lvCaligulaWD.setAdapter(adapterWD);
			lvCaligulaWD.setOnItemClickListener(this);	
			weekSelected = position;
            // Set the animation
			vfCaligula.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
             // Flip!
			vfCaligula.showNext();
			this.setTitle("S" + maListeDeSemaine.get(position).getNumero());
		}
		if (((String)view.getTag()).equals("WD")) {
			L.v("Caligula", "onClickItem : next");
		    adapterWC = new CoursWCAdapter(getApplicationContext(), maListeDeSemaine.get(weekSelected).getMalisteDeJour().get(position).getMaListeDeCours(), displayInfo, (adapterWD.isBrightAllowed() && maListeDeSemaine.get(weekSelected).getMalisteDeJour().get(position).getJour() == adapterWD.getJourActuel()) ? true : false);
			lvCaligulaWC.setAdapter(adapterWC);
			lvCaligulaWC.setOnItemClickListener(this);	
			daySelected = position;
            // Set the animation
			vfCaligula.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
             // Flip!
			vfCaligula.showNext();
			this.setTitle("S" + maListeDeSemaine.get(weekSelected).getNumero() + " > " + maListeDeSemaine.get(weekSelected).getMalisteDeJour().get(position).toString() + " " + maListeDeSemaine.get(weekSelected).getMalisteDeJour().get(position).getDate());
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
	    	L.v("Caligula", "pressButtonBack");
	    	L.v("Caligula", "" + vfCaligula.getCurrentView().getId());
	    	if (vfCaligula.getCurrentView() == lvCaligulaWC) {
	    		L.v("Caligula", "wc");
				vfCaligula.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
				vfCaligula.showPrevious();
				this.setTitle("S" + maListeDeSemaine.get(weekSelected).getNumero());
			    return false;
	    	}
	    	if (vfCaligula.getCurrentView() == lvCaligulaWD) {
	    		L.v("Caligula", "wd");
				vfCaligula.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
				vfCaligula.showPrevious();
				this.setTitle("Caligula > " + urlCaligulaBdd.getUrlCaligula(classeSelected).getName());
			    return false;
	    	}
	    	if (vfCaligula.getCurrentView() == lvCaligulaWW) {
	    		L.v("Caligula", "ww");
			    return super.onKeyDown(keyCode, event);
	    	}
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == CODE_RETOUR) {
			L.v("Root", "Modifications terminées");
			Toast.makeText(this, "Modifications terminées", Toast.LENGTH_SHORT).show();
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			displayInfo.put(0, prefs.getBoolean("caligula_heure", true));
			displayInfo.put(1, prefs.getBoolean("caligula_duree", true));
			displayInfo.put(2, prefs.getBoolean("caligula_matiere", true));
			displayInfo.put(3, prefs.getBoolean("caligula_salle", true));
			displayInfo.put(4, prefs.getBoolean("caligula_prof", true));
			
			
			if (classeSelected != Integer.parseInt(prefs.getString("listPref", "" + urlCaligulaBdd.getAllUrlCaligula().get(0).getId()))) {
				weekSelected = 0;
				daySelected = 0;
				classeSelected = Integer.parseInt(prefs.getString("listPref", "" + urlCaligulaBdd.getAllUrlCaligula().get(0).getId()));
				if (vfCaligula.getCurrentView() == lvCaligulaWC) {
					vfCaligula.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
					vfCaligula.showPrevious();
					vfCaligula.showPrevious();
		    	}
		    	if (vfCaligula.getCurrentView() == lvCaligulaWD) {
		    		vfCaligula.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
		    		vfCaligula.showPrevious();
		    	}
		    	maListeDeSemaine.clear();
				//buildTree();
			}
			if (adapterWW != null)
				adapterWW.notifyDataSetChanged();
			if (adapterWD != null)
				adapterWD.notifyDataSetChanged();
			if (adapterWC != null)
				adapterWC.notifyDataSetChanged();
			//String maString = prefs.getString("listPref", "0");
			
//			preferences = PreferenceManager.getDefaultSharedPreferences(this);
//			boolean comeFromPreferences = preferences.getBoolean("manuel", false);
		}
		else if (requestCode == Webteam.RESPONSE_FORCE_CLOSE_PREFERENCE) {
			
		}
	super.onActivityResult(requestCode, resultCode, data);
	}

	public int getWeekSelected() {
		return weekSelected;
	}
	
	public int getDaySelected() {
		return daySelected;
	}
}
