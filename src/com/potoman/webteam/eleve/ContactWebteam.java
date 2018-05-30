package com.potoman.webteam.eleve;

import java.util.ArrayList;
import java.util.HashMap;



public class ContactWebteam {
	private String pseudo = "";
	private int id = 0;
	private String prenom = "";
	private String nom = "";
	private String adresse = "";
	private String adresseParent = "";
	private String urlImage = "";
	private String siteInternet = "";
	private String telephone = "";
	private String telephoneFixe = "";
	private String telephoneParent = "";
	private String signature = "";
	private String email = "";
	private String classe = "";
	private String dateDeNaissance = "";
	private boolean favoris = false;
	private String pseudoSquirrel = "";
	private String passwordSquirrel = "";
	private String pseudoClubPhoto = "";
	private String passwordClubPhoto = "";
	
	public ContactWebteam() {
		
	}

	public ContactWebteam(int id) {
		this.id = id;
	}

	private static final String ID="id";
	private static final String CONTENU ="contenu";
	private static final String PSEUDO ="pseudo";
	private static final String NOM ="nom";
	private static final String PRENOM ="prenom";
	private static final String EMAIL ="email";
	private static final String TELEPHONE ="telephone";
	private static final String TELEPHONE_FIXE ="telephoneFixe";
	private static final String TELEPHONE_PARENT ="telephoneParent";
	private static final String CLASSE ="classe";

	public ContactWebteam(JSONObject jObj) {
		this(jObj.getInt(ID),
				jObj.getString(PSEUDO),
				jObj.getString(NOM),
				jObj.getString(PRENOM),
				jObj.getString(EMAIL),
				jObj.getString(TELEPHONE),
				jObj.getString(TELEPHONE_FIXE),
				jObj.getString(TELEPHONE_PARENT),
				jObj.getString(CLASSE));
	}

	public ContactWebteam(int id, String pseudo, String nom, String prenom, String email, String telephone, String telephoneFixe, String telephoneParent, String classe) {
		this.id = id;
		this.pseudo = pseudo;
		this.nom = nom;
		this.prenom = prenom;
		this.email = email;
		this.telephone = telephone;
		this.telephoneFixe = telephoneFixe;
		this.telephoneParent = telephoneParent;
		this.classe = classe;
	}

	public ContactWebteam(int id, String nom, String prenom) {
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
	}

	public String getPseudo() {
		return pseudo;
	}
	/*public Integer getHistoriqueRagot(int index) {
		return this.historiqueRagot.get(index);
	}*/

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	/*public void setHistoriqueRagot(int index, Integer historique) {
		this.historiqueRagot.set(index, historique);
	}*/

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return nom;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresseParent(String adresseParent) {
		this.adresseParent = adresseParent;
	}

	public String getAdresseParent() {
		return adresseParent;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setSiteInternet(String siteInternet) {
		this.siteInternet = siteInternet;
	}

	public String getSiteInternet() {
		return siteInternet;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getClasse() {
		return classe;
	}

	public void setDateDeNaissance(String dateDeNaissance) {
		this.dateDeNaissance = dateDeNaissance;
	}

	public String getDateDeNaissance() {
		return dateDeNaissance;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephoneFixe(String telephoneFixe) {
		this.telephoneFixe = telephoneFixe;
	}

	public String getTelephoneFixe() {
		return telephoneFixe;
	}

	public void setTelephoneParent(String telephoneParent) {
		this.telephoneParent = telephoneParent;
	}

	public String getTelephoneParent() {
		return telephoneParent;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSignature() {
		return signature;
	}
	
	public ArrayList<HashMap<String, String>> getHashMap() {
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
		 
		HashMap<String, String> defProfil;

		defProfil = new HashMap<String, String>();
		defProfil.put("type", "Pseudo :");
		defProfil.put("valeur", pseudo);
		listItem.add(defProfil);
		defProfil = new HashMap<String, String>();
		defProfil.put("type", "Classe :");
		defProfil.put("valeur", classe);
		listItem.add(defProfil);
		defProfil = new HashMap<String, String>();
		defProfil.put("type", "Date de naissance :");
		defProfil.put("valeur", dateDeNaissance);
		listItem.add(defProfil);
		defProfil = new HashMap<String, String>();
		defProfil.put("type", "Téléphone portable :");
		defProfil.put("valeur", telephone);
		listItem.add(defProfil);
		defProfil = new HashMap<String, String>();
		defProfil.put("type", "Adresse : ");
		defProfil.put("valeur", adresse);
		listItem.add(defProfil);
		defProfil = new HashMap<String, String>();
		defProfil.put("type", "Téléphone fixe :");
		defProfil.put("valeur", telephoneFixe);
		listItem.add(defProfil);
		defProfil = new HashMap<String, String>();
		defProfil.put("type", "Adresse des parents :");
		defProfil.put("valeur", adresseParent);
		listItem.add(defProfil);
		defProfil = new HashMap<String, String>();
		defProfil.put("type", "Téléphone des parents :");
		defProfil.put("valeur", telephoneParent);
		listItem.add(defProfil);
		defProfil = new HashMap<String, String>();
		defProfil.put("type", "Email :");
		defProfil.put("valeur", email);
		listItem.add(defProfil);
		defProfil = new HashMap<String, String>();
		defProfil.put("type", "Signature :");
		defProfil.put("valeur", signature);
		listItem.add(defProfil);
		defProfil = new HashMap<String, String>();
		defProfil.put("type", "Site internet :");
		defProfil.put("valeur", siteInternet);
		listItem.add(defProfil);
		
		return listItem;
	}
	
	public String toString() {
		return "id : " + this.id + ", pseudo : " + this.pseudo;
	}
	
	/* TODO: A virer
	 * 
	 */
	public String toStringResultatTrombi() {
		return this.prenom + " " + this.nom;
	}

	public boolean isFavoris() {
		return favoris;
	}

	public void setFavoris(boolean favoris) {
		this.favoris = favoris;
	}
	

	public int getFavorisInt() {
		if (favoris)
			return 1;
		else
			return 0;
	}

	public void setFavorisInt(int favoris) {
		if (favoris == 0)
			this.favoris = false;
		else
			this.favoris = true;
	}

	public String getPseudoSquirrel() {
		return pseudoSquirrel;
	}

	public void setPseudoSquirrel(String pseudoSquirrel) {
		this.pseudoSquirrel = pseudoSquirrel;
	}

	public String getPasswordSquirrel() {
		return passwordSquirrel;
	}

	public void setPasswordSquirrel(String passwordSquirrel) {
		this.passwordSquirrel = passwordSquirrel;
	}

	public String getPseudoClubPhoto() {
		return pseudoClubPhoto;
	}

	public void setPseudoClubPhoto(String pseudoClubPhoto) {
		this.pseudoClubPhoto = pseudoClubPhoto;
	}
	
	public String getPasswordClubPhoto() {
		return passwordClubPhoto;
	}

	public void setPasswordClubPhoto(String passwordClubPhoto) {
		this.passwordClubPhoto = passwordClubPhoto;
	}
}
	
