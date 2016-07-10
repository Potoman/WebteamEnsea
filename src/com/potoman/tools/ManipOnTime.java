package potoman.tools;

import java.util.GregorianCalendar;

public class ManipOnTime {
	public static GregorianCalendar stringOfDateToCalendar(final String date) {
		L.d("Caligula|descriptionDate", "DescriptionDate entrée : " + date);
		int indexOfFirstSlash = date.indexOf("/");
		int indexOfSecondSlash = date.indexOf("/", indexOfFirstSlash + 1);
		int day = Integer.parseInt(date.substring(0, indexOfFirstSlash));
		int month = Integer.parseInt(date.substring(indexOfFirstSlash + 1, indexOfSecondSlash));
		int year = Integer.parseInt(date.substring(indexOfSecondSlash + 1, date.length()));
		L.d("Caligula|descriptionDate", "Date parsé : " + day + "/" + month + ", from : " + date);
		return new GregorianCalendar(year, month - 1, day);
	}
}
