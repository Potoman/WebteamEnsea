package org.example.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.example.webteam.R;
import org.example.webteam.eleve.FicheEleve;
import org.example.webteam.eleve.ContactWebteam;
import org.example.webteam.ragot.Ragot;
import org.json.JSONException;
import org.json.JSONObject;

import potoman.tools.CallService;
import potoman.tools.L;
import potoman.tools.UrlService;
import potoman.webteam.constant.Webteam;
import potoman.webteam.exception.ExceptionService;

import android.content.Context;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class ATGetProfil extends AsyncTask<Context, Void, ContactWebteam> {

	private IWorkFinishOfAsyncTask observer = null;
	private Integer idProfil = null;
	
	public ATGetProfil(IWorkFinishOfAsyncTask observer, Integer idProfil) {
		this.observer = observer;
		this.idProfil = idProfil;
	}
	
	@Override
	protected ContactWebteam doInBackground(Context... params) {
		
		ContactWebteam profilEleve = null;
		JSONObject reponseJSON = null;
		
		List<String> nameData = new ArrayList<String>();
		nameData.add("idProfil");
		List<String> valeurData = new ArrayList<String>();
		valeurData.add("" + idProfil);
		
		try {
			reponseJSON = CallService.getJsonPost(params[0], 
					UrlService.createUrlFicheEleve(
							PreferenceManager.getDefaultSharedPreferences((Context) params[0]).getString(Webteam.PSEUDO, ""), 
							PreferenceManager.getDefaultSharedPreferences((Context) params[0]).getString(Webteam.PASSWORD, "")), 
					nameData, 
					valeurData);
		} catch (final ExceptionService e) { 
			e.printStackTrace();
			return null;
		}
		
		if (reponseJSON == null) {
			return null;
		}
		
		int erreur = 0;
		try {
			erreur = (Integer) reponseJSON.getInt("erreur");
			profilEleve = new ContactWebteam();
			if (erreur == 1) {
				profilEleve.setId(reponseJSON.getInt("id"));
				profilEleve.setPrenom(reponseJSON.getString("prenom"));
				profilEleve.setNom(reponseJSON.getString("nom"));
				profilEleve.setPseudo(reponseJSON.getString("pseudo"));
				profilEleve.setClasse(reponseJSON.getString("classe"));
		    	SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
		    	profilEleve.setDateDeNaissance(sdfDate.format(new Date(Long.parseLong(reponseJSON.getString("dateDeNaissance"))*1000)));
				profilEleve.setTelephone(reponseJSON.getString("telephone"));
				profilEleve.setTelephoneFixe(reponseJSON.getString("telephoneFixe"));
				profilEleve.setTelephoneParent(reponseJSON.getString("telephoneParent"));
				profilEleve.setEmail(reponseJSON.getString("email"));
				profilEleve.setAdresse(reponseJSON.getString("residence"));
				profilEleve.setAdresseParent(reponseJSON.getString("adresseDesParents"));
				profilEleve.setSiteInternet(reponseJSON.getString("siteInternet"));
				profilEleve.setUrlImage(reponseJSON.getString("urlImageProfil"));
				L.v("urlImageProfil", "" + profilEleve.getUrlImage());
				profilEleve.setSignature(reponseJSON.getString("signature"));
			}
		} catch (JSONException e) {
			e.printStackTrace();
			profilEleve = null;
			return null;
		}
		
		return profilEleve;
	}
	
	@Override
	protected void onPostExecute(ContactWebteam profilEleve) {
		L.w("GetFicheEleve", "FINISH !");
		if (isCancelled()) {
			return;
		}
		observer.workFinish(IWorkFinishOfAsyncTask.AT_GET_ELEVE);
		super.onPostExecute(profilEleve);
	}
	
}
