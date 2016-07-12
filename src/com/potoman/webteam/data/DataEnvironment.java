package com.potoman.webteam.data;

import com.potoman.tools.L;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

public final class DataEnvironment {

	private static String PREFS_NAME = "WEBTEAM_PREFERENCE";
	
	private Activity activity = null;
	
	private DataEnvironment() {
	}
	
	private DataEnvironment(Activity activity) {
		this.activity = activity;
	}
	
	private static DataEnvironment instance = null;
	
	public static DataEnvironment getInstance(Activity activity) {
		if (instance == null) {
			instance = new DataEnvironment(activity);
		}
		return instance;
	}
	
	/**
	 * 
	 * @param key : cl� de la donn�e � chercher.
	 * @return la valeur dans les pr�f�rences. Si la cl� n'�xiste pas, la valeur retourn� est <null>.
	 */
	public String getString(KeyPreference key) {
		if (key.getType() != String.class) {
			try {
				throw new Exception("Getter : mauvais type de donn�e.");
			}
			catch (Exception e) {
				L.e("DataEnvironment", e.getMessage());
			}
		}
		return activity.getSharedPreferences(PREFS_NAME, 0).getString(key.getKeyName(), null);
	}

	public void setString(KeyPreference key, String value) {
		if (key.getType() != String.class) {
			try {
				throw new Exception("Setter : mauvais type de donn�e.");
			}
			catch (Exception e) {
				L.e("DataEnvironment", e.getMessage());
			}
		}
		SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key.getKeyName(), value);
		editor.commit();
	}
}
