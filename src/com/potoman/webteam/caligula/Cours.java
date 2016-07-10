package org.example.webteam.caligula;

import java.util.Calendar;

import potoman.tools.L;

public class Cours {
	private int id = 0;
	private String date = "";
	private String heure = "";
	private String duree = "";
	private String activite = "";
	private int stagiaire = -1;
	private String formateur = "";
	private String salle = "";
	
	public Cours() {
		id = 0;
		date = "";
		heure = "";
		duree = "";
		activite = "";
		stagiaire = 0;
		formateur = "";
		salle = "";
	}
	
	public Cours(final int id, final String date, final String heure, final String duree, final String activite, final int stagiaire, final String formateur, final String salle) {
		this.id = id;
		this.date = date;
		this.heure = heure;
		this.duree = duree;
		this.activite = activite;
		this.stagiaire = stagiaire;
		this.formateur = formateur;
		this.salle = salle;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getDate() {
		return date;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getHeure() {
		return heure;
	}
	
	public void setHeure(String heure) {
		this.heure = heure;
	}
	
	public String getDuree() {
		return duree;
	}
	
	public void setDuree(String duree) {
		this.duree = duree;
	}
	
	public String getActivite() {
		return activite;
	}
	
	public void setActivite(String activite) {
		this.activite = activite;
	}
	
	public int getStagiaire() {
		return stagiaire;
	}
	
	public void setStagiaire(int stagiaire) {
		this.stagiaire = stagiaire;
	}
	
	public String getFormateur() {
		return formateur;
	}
	
	public void setFormateur(String formateur) {
		this.formateur = formateur;
	}
	
	public String getSalle() {
		return salle;
	}
	
	public void setSalle(String salle) {
		this.salle = salle;
	}
	
	public String toString() {
		return "id : " + id + ", date : " + date + ", heure : " + heure + ", duree : " + duree + ", salle : " + salle;
	}
	
	public boolean isInto() {
		L.e("Cours|isInto", "heure : " + Calendar.HOUR_OF_DAY + ", minute : " + Calendar.MINUTE);
		return true;
	}
}
