package potoman.tools;

import java.util.GregorianCalendar;

public class ObjectToDay {
	
	public static String displayDay(int dayOfWeek) {
		switch (dayOfWeek) {
		case GregorianCalendar.MONDAY:
			return "Lundi";
		case GregorianCalendar.TUESDAY:
			return "Mardi";
		case GregorianCalendar.WEDNESDAY:
			return "Mercredi";
		case GregorianCalendar.THURSDAY:
			return "Jeudi";
		case GregorianCalendar.FRIDAY:
			return "Vendredi";
		case GregorianCalendar.SATURDAY:
			return "Samedi";
		case GregorianCalendar.SUNDAY:
			return "Dimanche";
		default:
			return "" + dayOfWeek;
		}
	}
	
	public static String displayDay(String text) {
		if (text.equals("2"))
			return "Lundi";
		if (text.equals("3"))
			return "Mardi";
		if (text.equals("4"))
			return "Mercredi";
		if (text.equals("5"))
			return "Jeudi";
		if (text.equals("6"))
			return "Vendredi";
		if (text.equals("7"))
			return "Samedi";
		if (text.equals("1"))
			return "Dimanche";
		if (text.equals("Lun."))
			return "Lundi";
		if (text.equals("Mar."))
			return "Mardi";
		if (text.equals("Mer."))
			return "Mercredi";
		if (text.equals("Jeu."))
			return "Jeudi";
		if (text.equals("Ven."))
			return "Vendredi";
		if (text.equals("Sam."))
			return "Samedi";
		if (text.equals("Dim."))
			return "Dimanche";
		if (text.equals("Mon"))
			return "Lundi";
		if (text.equals("Tue"))
			return "Mardi";
		if (text.equals("Wed"))
			return "Mercredi";
		if (text.equals("Thu"))
			return "Jeudi";
		if (text.equals("Fri"))
			return "Vendredi";
		if (text.equals("Sat"))
			return "Samedi";
		if (text.equals("Sun"))
			return "Dimanche";
		return text;
	}
	
	public static String displayMonth(final int month) {
		switch (month) {
		case GregorianCalendar.JANUARY:
			return "janvier";
		case GregorianCalendar.FEBRUARY:
			return "février";
		case GregorianCalendar.MARCH:
			return "mars";
		case GregorianCalendar.APRIL:
			return "avril";
		case GregorianCalendar.MAY:
			return "mai";
		case GregorianCalendar.JUNE:
			return "juin";
		case GregorianCalendar.JULY:
			return "juillet";
		case GregorianCalendar.AUGUST:
			return "août";
		case GregorianCalendar.SEPTEMBER:
			return "septembre";
		case GregorianCalendar.OCTOBER:
			return "octobre";
		case GregorianCalendar.NOVEMBER:
			return "novembre";
		case GregorianCalendar.DECEMBER:
			return "décembre";
		default:
			return "" + month;
		}
	}
	
}

