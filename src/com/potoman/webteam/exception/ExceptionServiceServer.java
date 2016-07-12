package com.potoman.webteam.exception;

import android.content.Context;

public class ExceptionServiceServer extends ExceptionService {
	
	private static final long serialVersionUID = 1L;
	
	public ExceptionServiceServer(Context context) {
		super(context, ID_ERROR_SERVER);
	}
}