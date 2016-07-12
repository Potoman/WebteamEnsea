package com.potoman.webteam.ragot;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;

public class Ragot {
	private int id;
	private int idPseudo;
	private String pseudo = "";
	private String ragot = "";
	private String ragotAfterParse = "";
	private long date;
	private int nbrLien;
	private List<Uri> lienUri = new ArrayList<Uri>(); 
	private boolean justDate;
	private int history;
	private int favoris;
	private int separateur; //Le ragot n'est pas vraiment un.
	private String news = "";
	
	public Ragot() {
		this.setSeparateur(1);
	}
	
	public Ragot(int id, String ragot, int idPseudo, String pseudo, long date, int history, int favoris, int separateur) {
		this.id = id;
		this.ragot = ragot;
		this.idPseudo = idPseudo;
		this.pseudo = pseudo;
		this.date = date;
		this.history = history;
		this.setFavoris(favoris);
		this.setSeparateur(separateur);
		parseLien(ragot);
	}
	
	public Ragot(String ragot, int idPseudo, String pseudo, long date, int history, int favoris, int separateur, String news) {
		this.ragot = ragot;
		this.idPseudo = idPseudo;
		this.pseudo = pseudo;
		this.date = date;
		this.history = history;
		this.setFavoris(favoris);
		this.setSeparateur(separateur);
		parseLien(ragot);
		this.news = news;
	}
	
	/*
	public Ragot(int history, boolean favoris, int id, int idPseudo, String pseudo, String ragot, long date, boolean justDate) {
		this.setId(id);
		this.idPseudo = idPseudo;
		this.pseudo = pseudo;
		this.date = date;
		this.setJustDate(justDate);
		
		parseLien(ragot);
	}
	
	public Ragot(int idPseudo, String pseudo, String ragot, long date, boolean justDate) {
		this.idPseudo= idPseudo;
		this.pseudo = pseudo;
		this.date = date;
		this.setJustDate(justDate);
		
		parseLien(ragot);
	}*/

	
	
	public int getIdPseudo() {
		return idPseudo;
	}
	public String getPseudo() {
		return pseudo;
	}
	public String getRagot() {
		return ragot;
	}
	public long getDate() {
		return date;
	}
	public int getNbrLien() {
		return nbrLien;
	}
	public Uri getLien(int index) {
		return this.lienUri.get(index);
	}
	public void setIdPseudo(int idPseudo) {
		this.idPseudo = idPseudo;
	}
	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
	public void setRagot(String ragot) {
		this.ragot = ragot;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public void setNbrLien(int nbrLien) {
		this.nbrLien = nbrLien;
	}
	public void setLien(int index, Uri lienUri) {
		this.lienUri.set(index, lienUri);
	}

	public void parseLien(String ragot) {
	int offset = 0;
	int indexDeb = ragot.length();
	int indexFin = 0;
	//int count = 0;
	lienUri.clear();
		while (ragot.indexOf("http", offset)  != -1) {
			indexDeb = ragot.indexOf("http", offset);
			indexFin = ragot.indexOf(" ", ragot.indexOf("http", offset) + 1);
			if (indexFin == -1 || indexFin > ragot.length() - 1)
				indexFin = ragot.length();
		lienUri.add(Uri.parse(ragot.substring(indexDeb, indexFin)));
		//count++;
		offset = ragot.indexOf("http", offset) + 1;
			if (offset == 0)
				break;
		}
		for (int i = 0; i <= lienUri.size() - 1; i++) {
			if (lienUri.get(i).toString().startsWith("http://www.youtube.com/")) {
				ragot = ragot.replace(lienUri.get(i).toString(), " <img src=\"youtube.png\"> ");
			}
			else if (lienUri.get(i).toString().startsWith("http://www.dailymotion.com/")) {
				ragot = ragot.replace(lienUri.get(i).toString(), " <img src=\"dailymotion.png\"> ");
			}
			else if (lienUri.get(i).toString().startsWith("https://webteam.ensea.fr/profil.php?id=")){
				ragot = ragot.replace(lienUri.get(i).toString(), " <img src=\"user.png\"> ");
			}
			else {	
				ragot = ragot.replace(lienUri.get(i).toString(), " <img src=\"lien.png\"> ");
			}
		}

		ragot = ragot.replaceAll("0:&#41;", " <img src=\"emo_im_angel.png\"> ");
		ragot = ragot.replaceAll("0:-&#41;", " <img src=\"emo_im_angel.png\"> ");
		ragot = ragot.replaceAll(":&#41;", " <img src=\"emo_im_happy.png\"> ");
		ragot = ragot.replaceAll(":-&#41;", " <img src=\"emo_im_happy.png\"> ");
		ragot = ragot.replaceAll("=&#41;", " <img src=\"emo_im_laughing.png\"> ");
		ragot = ragot.replaceAll(":D", " <img src=\"emo_im_laughing.png\"> ");
		ragot = ragot.replaceAll(":-D", " <img src=\"emo_im_laughing.png\"> ");
		ragot = ragot.replaceAll("&#36;&#41;", " <img src=\"emo_im_money_mouth.png\"> ");
		ragot = ragot.replaceAll("8&#41;", " <img src=\"emo_im_cool.png\"> ");
		ragot = ragot.replaceAll("8-&#41;", " <img src=\"emo_im_cool.png\"> ");
		ragot = ragot.replaceAll(":p", " <img src=\"emo_im_tongue_sticking_out.png\"> ");
		ragot = ragot.replaceAll(":P", " <img src=\"emo_im_tongue_sticking_out.png\"> ");
		ragot = ragot.replaceAll(":-p", " <img src=\"emo_im_tongue_sticking_out.png\"> ");
		ragot = ragot.replaceAll(":-P", " <img src=\"emo_im_tongue_sticking_out.png\"> ");
		ragot = ragot.replaceAll(";&#41;", " <img src=\"emo_im_winking.png\"> ");
		ragot = ragot.replaceAll(";-&#41;", " <img src=\"emo_im_winking.png\"> ");
		ragot = ragot.replaceAll("oO", " <img src=\"emo_im_wtf.png\"> ");
		ragot = ragot.replaceAll("Oo", " <img src=\"emo_im_wtf.png\"> ");
		ragot = ragot.replaceAll(":\'&#40;", " <img src=\"emo_im_crying.png\"> ");
		ragot = ragot.replaceAll(":&#40;", " <img src=\"emo_im_sad.png\"> ");
		ragot = ragot.replaceAll(":-&#40;", " <img src=\"emo_im_sad.png\"> ");
		ragot = ragot.replaceAll(":s", " <img src=\"emo_im_lips_are_sealed.png\"> ");
		ragot = ragot.replaceAll(":S;", " <img src=\"emo_im_lips_are_sealed.png\"> ");
		ragot = ragot.replaceAll(":-s", " <img src=\"emo_im_lips_are_sealed.png\"> ");
		ragot = ragot.replaceAll(":-S;", " <img src=\"emo_im_lips_are_sealed.png\"> ");
		ragot = ragot.replaceAll("&#60;3", " <img src=\"coeur.png\"> ");
	this.setRagotAfterParse(ragot);
	this.nbrLien = this.lienUri.size(); 
	}

	public void setJustDate(boolean justDate) {
		this.justDate = justDate;
	}	
	
	public boolean getJustDate() {
		return justDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getHistory() {
		return history;
	}

	public void setHistory(int history) {
		this.history = history;
	}
	
	public String toString() {
		return "ragot : id_posteur : " + idPseudo + ", history : " + history + ", ragot : " + ragot + ", parse : " + ragotAfterParse;
	}

	public String getRagotAfterParse() {
		return ragotAfterParse;
	}

	public void setRagotAfterParse(String ragotAfterParse) {
		this.ragotAfterParse = ragotAfterParse;
	}

	public int getFavoris() {
		return favoris;
	}

	public void setFavoris(int favoris) {
		this.favoris = favoris;
	}

	public int getSeparateur() {
		return separateur;
	}

	public void setSeparateur(int separateur) {
		this.separateur = separateur;
	}
	
	public void setNews(String news) {
		this.news = news;
	}
	
	public String getNews() {
		return news;
	}
}


