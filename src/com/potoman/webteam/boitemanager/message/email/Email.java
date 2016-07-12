package com.potoman.webteam.boitemanager.message.email;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.potoman.tools.L;
import com.potoman.webteam.bdd.BddMessageManager;
import com.potoman.webteam.boitemanager.message.Message;
import com.potoman.webteam.loggin.Root;

public class Email extends Message {
	
	private String emailFrom = ""; //Cette information sert uniquement � �tre affich�e.
	private JSONObject emailTo = null; //Cette information sert uniquement � �tre affich�e. 
	
	private String proprio = "";
	
	private JSONObject pieceJointe = null;

	public Email(int id, String emailFrom, String emailTo, String titre, String time, int etatBoite, String nomBoite, String proprio) {
		this.id = id;
		this.emailFrom = emailFrom;
		try {
			this.emailTo = new JSONObject(emailTo);
		} catch (JSONException e) {
			L.e("Email", "Constructeur : JSON");
			e.printStackTrace();
		}
		this.titre = titre;
		this.data = "";
		this.time = time;
		this.favoris = 0;
		this.reponse = 0;
		this.contenuLoad = Message.CONTENU_NOT_LOAD;
		this.etatBoite = etatBoite;
		this.nomBoite = nomBoite;
		this.proprio = proprio;
		this.pieceJointe = null;
	}
	
	//Constructeur utilisé par par le BDD :
	public Email(int id, String emailFrom, String emailTo, String titre, String data, String time, int reponse, int contenuLoad, int etatBoite, String nomBoite, String proprio) {
		this.id = id;
		this.emailFrom = emailFrom;
		try {
			this.emailTo = new JSONObject(emailTo);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		this.titre = titre;
		this.data = data;
		this.time = time;
		this.reponse = reponse;
		this.contenuLoad = contenuLoad;
		this.etatBoite = etatBoite;
		this.nomBoite = nomBoite;
		this.proprio = proprio;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmailFrom() {
		return emailFrom;
	}
	public void setEmailFrom(String emailFrom) {
		this.emailFrom = emailFrom;
	}
	public List<String> getEmailTo() {
		List<String> maListDEmailTo = new ArrayList<String>();
		JSONArray jsonArrayOfEmail;
		try {
			jsonArrayOfEmail = emailTo.getJSONArray("emails");
			JSONObject jsonObjectMyEmail = null;
			int i = 0;
			while (!jsonArrayOfEmail.isNull(i)) {
				jsonObjectMyEmail = jsonArrayOfEmail.getJSONObject(i);
				maListDEmailTo.add(jsonObjectMyEmail.getString("email"));
				i++;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return maListDEmailTo;
	}
	
	public String getEmailToString() {
		return emailTo.toString();
	}
	
	public String getEmailToForSend() {
		String out = "";
		JSONArray jsonArrayOfEmail;
		try {
			jsonArrayOfEmail = emailTo.getJSONArray("emails");
			JSONObject jsonObjectMyEmail = null;
			int i = 0;
			while (!jsonArrayOfEmail.isNull(i)) {
				jsonObjectMyEmail = jsonArrayOfEmail.getJSONObject(i);
				if (out.equals(""))
					out = out + jsonObjectMyEmail.getString("email");
				else
					out = out + "; " + jsonObjectMyEmail.getString("email");
				i++;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
		return out;
	}
	
	public void setEmailTo(String emailTo) {
		try {
			this.emailTo = new JSONObject(emailTo);
		} catch (JSONException e) {
			L.e("Email", "setEmailTo : JSON");
			e.printStackTrace();
		}
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getReponse() {
		return reponse;
	}
	public void setReponse(int reponse) {
		this.reponse = reponse;
	}
	public int getContenuLoad() {
		return contenuLoad;
	}
	public void setContenuLoad(int contenuLoad) {
		this.contenuLoad = contenuLoad;
	}
	public int getEtatBoite() {
		return etatBoite;
	}
	public void setEtatBoite(int etatBoite) {
		this.etatBoite = etatBoite;
	}

	public String getProprio() {
		return proprio;
	}

	public void setProprio(String proprio) {
		this.proprio = proprio;
	}

	public JSONObject getPieceJointe() {
		return pieceJointe;
	}

	public void setPieceJointe(String pieceJointe) {
		if (!pieceJointe.equals("")) {
			try {
				this.pieceJointe = new JSONObject(pieceJointe);
			} catch (JSONException e) {
				L.e("Email", "setPieceJointe : JSON");
				e.printStackTrace();
			}
		}
	}
}
