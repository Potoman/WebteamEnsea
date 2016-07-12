package com.potoman.webteam;


import java.util.List;

import org.json.JSONObject;

import com.potoman.webteam.boitemanager.item.boite.ItemBoiteDeMessage;
import com.potoman.webteam.boitemanager.message.Message;
import com.potoman.webteam.eleve.ContactWebteam;
import com.potoman.webteam.task.ATGetProfil;

import android.os.AsyncTask;


public class TempData {
	
	public int tempLayoutAffiche = -1;
	public JSONObject tempReponseJSON = null;
	public String ajoutRagot = "";

	//Utile pour Ragots.java :
		//Utile lorsque l'on ajoute un ragot pour �viter d'afficher deux fois le Toast.
	public boolean alreadyDisplayWarningInPost = false;
	
	//Utile pour Anniversaire.java :
	public int lag = 0;
	public int year = 0;
    public int month = 0;
    public int day = 0;
    
    //Utile pour Root.java :
    public boolean alreadyDisplayVersion = false;
    
    //Utile pour PageLog.java :
    public int numViewFlipper = 0;
    
    //Utile pour FicheEleve.java :
    public ContactWebteam profilEleve = null;
    
    //Utile pour la classe BoiteDeMessage :
    	//Avans je sauvegardais les r�ponse JSON ce qui est carr�ment une mauvaise chose, car � chaque basculement de l'�cran je devait retraiter les donn�e.
    	//Maintenant, je traite les donn�es et ensuite j'enregistre le r�sultat final dans la variable temporaire. Comme cela, � chaque basculement de l'�cran,
    	//On a juste besoin de r�affecter la donn�e. Il va de soit qu'il faut modifier ainsi tout les codes, surtout celui des ragots, qui gagnerons beaucoup
    	//en nombre de ligne de code.
    public List<ItemBoiteDeMessage> maListeDeBoiteDeMessage = null;
//    public int boiteDeReceptionLoad = BoiteDeMessage.RETURN_ERROR;
//    public int boiteDEnvoieLoad = BoiteDeMessage.RETURN_ERROR;
    
    //Utile pour la classe LireMessagePrive :
    public Message monMessage = null;
	public AsyncTask aSynchTask;
}
