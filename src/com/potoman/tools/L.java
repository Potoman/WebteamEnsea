package com.potoman.tools;


import android.util.Log;

public class L {
	
	private static final int SIZE = 1000;

	public static boolean DEBUG = true;
	
	private static final int VERBOSE_V = 0;
	private static final int VERBOSE_D = 1;
	private static final int VERBOSE_I = 2;
	private static final int VERBOSE_W = 3;
	private static final int VERBOSE_E = 4;
	
	public static void v(String nameClass, String debug) { if (DEBUG) dislayBySize(nameClass, debug, VERBOSE_V); }

	public static void d(String nameClass, String debug) { if (DEBUG) dislayBySize(nameClass, debug, VERBOSE_D); }
	
	public static void i(String nameClass, String debug) { if (DEBUG) dislayBySize(nameClass, debug, VERBOSE_I); }
	
	public static void w(String nameClass, String debug) { if (DEBUG) dislayBySize(nameClass, debug, VERBOSE_W); }
	
	public static void e(String nameClass, String debug) { if (DEBUG) dislayBySize(nameClass, debug, VERBOSE_E); }

	private static void dislayBySize(final String nameClass, final String debug, final int verbose) {
		if (debug.length() < SIZE) {
			displayLine(nameClass, debug, verbose);
		}
		else {
			float nbrLigne = debug.length() / SIZE;
			int nbrLigneTotal = (int) Math.floor(nbrLigne);
			for (int i = 0; i < nbrLigneTotal; i++)
				displayLine(nameClass, "[" + i + "]" + debug.substring(SIZE * i, SIZE * (i + 1)) + "[/" + i + "]", verbose);
			displayLine(nameClass, "[" + nbrLigneTotal + "]" + debug.substring(nbrLigneTotal * SIZE) + "[/" + nbrLigneTotal + "]", verbose);
		}
	}
	
	private static void displayLine(final String nameClass, final String debug, final int verbose) {
		switch (verbose) {
		case VERBOSE_D:
			Log.d(nameClass, debug);
			break;
		case VERBOSE_E:
			Log.e(nameClass, debug);
			break;
		case VERBOSE_I:
			Log.i(nameClass, debug);
			break;
		case VERBOSE_V:
			Log.v(nameClass, debug);
			break;
		case VERBOSE_W:
			Log.w(nameClass, debug);
			break;
		}
	}
}
