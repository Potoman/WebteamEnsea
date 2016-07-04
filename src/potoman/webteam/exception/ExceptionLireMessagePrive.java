package potoman.webteam.exception;

public class ExceptionLireMessagePrive extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private static final String[] TYPE_ERROR = new String[] {
		"Etat finished de l'AsynchTask..."
	};
	
	public static final int STATE_FINISH_ASYNCH_TASK = 0;

	int myError = 0;
	
	public ExceptionLireMessagePrive(final int TYPE_ERROR) {
		myError = TYPE_ERROR;
	}
	
	public String toString() {
		return TYPE_ERROR[myError];
	}
}
