package com.potoman.webteam.task;


import com.potoman.tools.IObserver;

import android.os.AsyncTask;

public abstract class ATGeneric<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
	
	private boolean resultOk = false;
	private IObserver observer = null;
	
	public ATGeneric(IObserver observer) {
		this.observer = observer;
		resultOk = false;
	}
	
	public IObserver getObserver() {
		return observer;
	}
	
	public boolean getResult() {
		return resultOk;
	}
	
	public void setResult(boolean resultOk) {
		this.resultOk = resultOk;
	}
}
