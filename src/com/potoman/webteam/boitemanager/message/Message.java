package org.example.webteam.boitemanager.message;

import potoman.tools.L;
import potoman.webteam.bdd.BddMessageManager;

public abstract class Message {

	public final static int CONTENU_LOAD = -1;
	public final static int CONTENU_NOT_LOAD = 0; 

	public final static int ETAT_NON_LU = 0;
	public final static int ETAT_LU = 1;
	public final static int ETAT_SUPPRIME = 2;
	public final static int ETAT_BROUILLON = 3;
	
	protected int id = 0;
	protected String titre = "";
	protected String data = "";
	protected String time = "";
	protected int favoris = 0;
	protected int reponse = 0;
	protected int contenuLoad = CONTENU_NOT_LOAD;
	protected int etatBoite = ETAT_NON_LU;
	protected String nomBoite = "";
	
	public int getId() {
		return id;
	}
	public String getTitre() {
		return titre;
	}
	public String getData() {
		return data;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTime() {
		return time;
	}
	public int getFavoris() {
		return favoris;
	}
	public int getReponse() {
		return reponse;
	}
	public int getContenuLoad() {
		return contenuLoad;
	}
	public int getEtatBoite() {
		return etatBoite;
	}
	public String getNomBoite() {
		return nomBoite;
	}
	public void setId(int id) {
		this.id = id;
	}
	public void setData(String data) {
		this.data = data;
	}
	public void setContenuLoad(int contenuLoad) {
		this.contenuLoad = contenuLoad;
	}
	public void setEtatBoite(int etatBoite) {
		this.etatBoite = etatBoite;
	}
	
	public void insertIfNotInBddElseUpdate(BddMessageManager messageBdd) {
		if (messageBdd.getMessage(id, nomBoite) == null) {
			L.v("Message", "Insert => Id du message = " + id + ", boite = " + nomBoite);
			messageBdd.insert(this);
		}
		else {
			messageBdd.updateMessageState(this);
			L.v("Message", "Update => Id du message = " + id + ", boite = " + nomBoite);
		}
	}
}







