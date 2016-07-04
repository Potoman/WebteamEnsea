package potoman.webteam.bdd.data;

public class UrlImgClubPhoto {

	private int id = -1;
	private int category = -1;
	private String urlImg = null;
	
	
	public UrlImgClubPhoto(int id, int category, String urlImg) {
		this.id = id;
		this.category = category;
		this.urlImg = urlImg;
	}
	
	public int getId() {
		return id;
	}
	
	public int getCategory() {
		return category;
	}
	
	public String getUrlImg() {
		return urlImg;
	}

}
