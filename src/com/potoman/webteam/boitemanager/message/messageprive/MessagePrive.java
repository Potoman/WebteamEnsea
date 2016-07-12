package com.potoman.webteam.boitemanager.message.messageprive;

import com.potoman.tools.L;
import com.potoman.webteam.bdd.BddMessageManager;
import com.potoman.webteam.boitemanager.message.Message;

public class MessagePrive extends Message {

	private int idFrom = 0;
	private String pseudoFrom = ""; //Cette information sert uniquement � �tre affich�e.
	private int idTo = 0;
	private String pseudoTo = ""; //Cette information sert uniquement � �tre affich�e. 
	
	//Constructeur utilisé pour la réception du service JSON :
	public MessagePrive(int id, 
			String nomBoite, 
			int idFrom, 
			String pseudoFrom, 
			int idTo, 
			String pseudoTo, 
			String titre, 
			String time) {
		this.id = id;
		this.nomBoite = nomBoite;
		this.idFrom = idFrom;
		this.pseudoFrom = pseudoFrom;
		this.idTo = idTo;
		this.pseudoTo = pseudoTo;
		this.titre = titre;
		this.data = "";
		this.time = time;
		this.favoris = 0;
		this.reponse = 0;
		this.contenuLoad = CONTENU_NOT_LOAD;
		this.etatBoite = ETAT_SUPPRIME;
	}
	
	public MessagePrive(int id, 
			String nomBoite, 
			int idFrom, 
			String pseudoFrom, 
			int idTo, 
			String pseudoTo, 
			String titre, 
			String data, 
			String time, 
			int contenuLoad, 
			int etatBoite) {
		this.id = id;
		this.nomBoite = nomBoite;
		this.idFrom = idFrom;
		this.pseudoFrom = pseudoFrom;
		this.idTo = idTo;
		this.pseudoTo = pseudoTo;
		this.titre = titre;
		this.data = data;
		this.time = time;
		this.favoris = favoris;
		this.reponse = reponse;
		this.contenuLoad = contenuLoad;
		this.etatBoite = etatBoite;
	}	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdFrom() {
		return idFrom;
	}
	public void setIdFrom(int idFrom) {
		this.idFrom = idFrom;
	}
	public String getPseudoFrom() {
		return pseudoFrom;
	}
	public void setPseudoFrom(String pseudoFrom) {
		this.pseudoFrom = pseudoFrom;
	}
	public int getIdTo() {
		return idTo;
	}
	public void setIdTo(int idTo) {
		this.idTo = idTo;
	}
	public String getPseudoTo() {
		return pseudoTo;
	}
	public void setPseudoTo(String pseudoTo) {
		this.pseudoTo = pseudoTo;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getFavoris() {
		return favoris;
	}
	public void setFavoris(int favoris) {
		this.favoris = favoris;
	}
	public int getReponse() {
		return reponse;
	}
	public void setReponse(int reponse) {
		this.reponse = reponse;
	}

	public String getTitre() {
		return titre;
	}

	public void setTitre(String titre) {
		this.titre = titre;
	}
	
	public String toString() {
//		L.v("MP", "id : " + id);
//		L.v("MP", "idFrom : " + idFrom);
//		L.v("MP", "pseudoFrom : " + pseudoFrom);
//		L.v("MP", "idTo : " + idTo);
//		L.v("MP", "pseudoTo : " + pseudoTo);
//		L.v("MP", "titre : " + titre);
//		L.v("MP", "data : " + data);
//		L.v("MP", "time : " + time);
//		L.v("MP", "favoris : " + favoris);
//		L.v("MP", "reponse : " + reponse);
//		L.v("MP", "contenuLoad : " + contenuLoad);
//		L.v("MP", "etatBoiteDEnvois : " + etatBoiteDEnvois);
//		L.v("MP", "etatBoiteDeReception : " + etatBoiteDeReception);
		return "\"id\" : " + id + 
				", \"idFrom\" : " + idFrom + 
				", \"pseudoFrom\" : " + pseudoFrom +
				", \"idTo\" : " + idTo + 
				", \"pseudoTo\" : " + pseudoTo + 
				", \"titre\" : " + titre + 
				", \"data\" : " + data + 
				", \"time\" : " + time +
				", \"favoris\" : " + favoris +
				", \"reponse\" : " + reponse +
				", \"contenuLoad\" : " + contenuLoad;
	}

	public int getContenuLoad() {
		return contenuLoad;
	}

	public void setContenuLoad(int contenuLoad) {
		this.contenuLoad = contenuLoad;
	}

	public int getEtatBoiteDEnvois() {
		return 0;
		//return etatBoiteDEnvois;
	}

	

//	public void setEtatBoiteDEnvois(int etatBoiteDEnvois) {
//		this.etatBoiteDEnvois = etatBoiteDEnvois;
//	}
//
//	public int getEtatBoiteDeReception() {
//		return etatBoiteDeReception;
//	}
//
//	public void setEtatBoiteDeReception(int etatBoiteDeReception) {
//		this.etatBoiteDeReception = etatBoiteDeReception;
//	}
//	
//	public abstract int getBoite();
//	
//	//public abstract int getEtatBoite();
//
//	public abstract void insertIfNotInBddElseUpdate(BddMessageManager messageBdd);
}
