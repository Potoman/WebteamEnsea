package com.potoman.webteam.task.clubphoto;

public abstract class AUrlClubPhoto {

	static final String URL_ROOT = "http://photo.asso-ensea.net";
	static final String URL_CONNEXION = "/ws.php?format=json&method=pwg.session.login";
	static final String URL_LIST_FOLDER = "/ws.php?format=json&method=pwg.categories.getList&recursive=false";
	static final String URL_FOLDER = "/ws.php?format=json&method=pwg.categories.getImages&per_page=500&cat_id=<ID>&recursive=false&page=<PAGE>";
}
