package org.example.task;

public interface IWorkFinishOfAsyncTask {

	public static final int AT_GET_BOITE_DE_RECEPTION = 0;
	public static final int AT_GET_BOITE_D_ENVOIS = 1;
	public static final int AT_GET_BOITE_DE_RECEPTION_SQUIRREL = 2;
	public static final int AT_GET_BOITE_D_ENVOIS_SQUIRREL = 3;
	public static final int AT_CONNEXION_SQUIRREL_FOR_LOGG = 4;
	public static final int AT_CONNEXION_SQUIRREL_FOR_READ = 5;
	public static final int AT_CONNEXION_SQUIRREL_FOR_DELETE = 6;
	public static final int AT_CONNEXION_SQUIRREL_FOR_ANSWER = 7;
	//public static final int AT_CONNEXION_SQUIRREL_FOR_SYNCH = 8;
	public static final int AT_SUPPRIMER_MESSAGE_PRIVE_OK = 9;
	public static final int AT_SUPPRIMER_MESSAGE_PRIVE_KO = 10;
	public static final int AT_GET_EMAIL = 11;
	public static final int AT_GET_MESSAGE_PRIVE = 12;
	public static final int AT_SUPPRIMER_EMAIL = 13;
	public static final int AT_GET_RAGOT = 14;
	public static final int AT_GET_ELEVE = 15;
	public static final int AT_LISTE_BOITE = 16;
	
	public void workFinish(int state);
	
	public String getStringForWaiting();
	
	public int getNumberOfIncrementForMyProgressDialog();
	
	public void incrementMyProgressDialog();
}
