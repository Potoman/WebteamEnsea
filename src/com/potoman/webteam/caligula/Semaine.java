package org.example.webteam.caligula;

import java.util.ArrayList;
import java.util.List;


public class Semaine {
	private int numero = 0;
	private List<Jour> maListeDeJour = null;
	
	public Semaine(int numero) {
		this.setNumero(numero);
		maListeDeJour = new ArrayList<Jour>();
//		maListeDeJour.add(new Jour(0));	//Lundi
//		maListeDeJour.add(new Jour(1));	//Mardi
//		maListeDeJour.add(new Jour(2));	//Mercredi
//		maListeDeJour.add(new Jour(3));	//Jeudi
//		maListeDeJour.add(new Jour(4));	//Vendredi
//		maListeDeJour.add(new Jour(5));	//Samedi
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}
	
	public List<Jour> getMalisteDeJour() {
		return maListeDeJour;
	}
	
	public void addCours(Cours monCours, int day) {
		if (maListeDeJour.size() == 0) {
			maListeDeJour.add(new Jour(day));
			maListeDeJour.get(0).addCours(monCours);
		}
		else {
			for (int i = 0; i < maListeDeJour.size(); i++) {
				if (maListeDeJour.get(i).getJour() == day) {
					maListeDeJour.get(i).addCours(monCours);
					break;
				}
				else {
					if (maListeDeJour.get(i).getJour() > day) {
						maListeDeJour.add(i, new Jour(day));
						maListeDeJour.get(i).addCours(monCours);
						break;
					}
					else {
						if (maListeDeJour.size() - 1 == i) {
							maListeDeJour.add(new Jour(day));
							maListeDeJour.get(i + 1).addCours(monCours);
							break;
						}
					}
				}
			}
		}
		
	}
}
