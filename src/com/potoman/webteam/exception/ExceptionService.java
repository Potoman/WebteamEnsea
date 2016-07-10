package potoman.webteam.exception;

import android.content.Context;

public class ExceptionService extends Exception {

	private String error = "";
//	public final static String ERROR_CONNECTED = "Vous n'êtes pas connecté à internet !";
//	public final static String ERROR_SERVER = "La webteam ne répond pas... :S";
	public final static int ID_ERROR_CONNECTED = 0;
	public final static int ID_ERROR_SERVER = 1;
	//public static String ERROR_LOGIN_MDP = "La webteam ne répond pas... :S";
	
	private static final long serialVersionUID = 1L;

	public ExceptionService() {
		error = "";
	}

//	public ExceptionService(String error) {
//		this.error = error;
//	}
	
	public ExceptionService(Context context, int error) {
		//this.error = context.getResources().getStringArray(org.example.webteam.R.array.exception)[error];
		this.error = context.getResources().getStringArray(org.example.webteam.R.array.exception)[error];
	}
	
	public String toString() {
		return error;
	}

}





