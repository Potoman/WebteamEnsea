package com.potoman.webteam.exception;

public class ExceptionCaligula extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String[] TYPE_ERROR = new String[] {
		"Le numéro de la semaine est inférieur à 1, ce qui n'est pas normal. Vérifier bien la date du lundi de la semaine numéro 1."
	};
	
	public static final int NUM_WEEK_MINUS_THAN_ONE = 0;

	int myError = 0;
	
	public ExceptionCaligula(final int TYPE_ERROR) {
		myError = TYPE_ERROR;
	}
	
	public String toString() {
		return TYPE_ERROR[myError];
	}
}
