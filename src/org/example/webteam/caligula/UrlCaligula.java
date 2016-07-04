
package org.example.webteam.caligula;

public class UrlCaligula {
	
	public static int ELEVE = 0;
	public static int PROFESSEUR = 0;
	
	private int id = 0;
	private String name = "";
	private String url = "";
	private int what = 0;
	
	public UrlCaligula() {
	}
	
	public UrlCaligula(int id, String name, String url, int what) {
		this.setId(id);
		this.setName(name);
		this.setUrl(url);
		this.setWhat(what);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getWhat() {
		return what;
	}

	public void setWhat(int what) {
		this.what = what;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String toString() {
		return "ID : " + id + ", name : " + name + ", url : " + url + ", what : " + what;
	}
	
	
}
