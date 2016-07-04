package potoman.webteam.exception;

import android.content.Context;

public class ExceptionServiceConnected extends ExceptionService {
	
	private static final long serialVersionUID = 1L;
	
	public ExceptionServiceConnected(Context context) {
		super(context, ID_ERROR_CONNECTED);
	}
}
