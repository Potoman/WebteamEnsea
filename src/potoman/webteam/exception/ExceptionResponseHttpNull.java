package potoman.webteam.exception;

public class ExceptionResponseHttpNull extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String info = "";
	
	public String toString() {
		return "La réponse HTTP est nulle. Info : " + info;
	}
	
	public ExceptionResponseHttpNull(final String info) {
		this.info = info;
	}
}
