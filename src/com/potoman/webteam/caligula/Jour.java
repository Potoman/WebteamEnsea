package com.potoman.webteam.caligula;

import java.util.ArrayList;
import java.util.List;

public class Jour {
	private List<Cours> maListeDeCours = null;
	private int numero = 0; //0 à 5...
	
	private static final String[] NOM_JOUR = new String[] {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi"};
	public Jour(int numero) {
		maListeDeCours = new ArrayList<Cours>();
		this.numero = numero;
	}
	
	public void addCours(Cours monCours) {
		maListeDeCours.add(monCours);
	}
	
	public List<Cours> getMaListeDeCours() {
		return maListeDeCours;
	}
	
	public int getJour() {
		return numero;
	}
	
	public String toString() {
		return NOM_JOUR[numero];
	}
	
	public String getDate() {
//		for (int i = 0; i < maListeDeCours.size(); i++) {
//			if (maListeDeCours.get(i) != null) {
//				return maListeDeCours.get(i).getDate();
//			}
//		} 
//		return "";
		return maListeDeCours.get(0).getDate();
	}
}
