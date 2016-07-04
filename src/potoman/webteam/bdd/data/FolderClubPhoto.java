package potoman.webteam.bdd.data;

public class FolderClubPhoto {

	private int category = -1;
	private Integer categoryContainer = null;
	private String title = null;
	private Integer countPhotoInFolder = null;
	private Integer countSubFolder = null;
	private Integer countPhotoInSubFolder = null;
	private int depth = 0;
	
	public FolderClubPhoto(int category, Integer categoryContainer, String title, int countPhotoInFolder, int countSubFolder, int countPhotoInSubFolder, int depth) {
		this.category = category;
		this.categoryContainer = categoryContainer;
		this.title = title;
		this.countPhotoInFolder = countPhotoInFolder;
		this.countSubFolder = countSubFolder;
		this.countPhotoInSubFolder = countPhotoInSubFolder;
		this.depth = depth;
	}

	public int getCategory() {
		return category;
	}

	public Integer getCategoryContainer() {
		return categoryContainer;
	}

//	public void setCategoryContainer(Integer categoryContainer) {
//		this.categoryContainer = categoryContainer;
//	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getCountPhotoInFolder() {
		return countPhotoInFolder;
	}

	public void setCountPhotoInFolder(Integer countPhotoInFolder) {
		this.countPhotoInFolder = countPhotoInFolder;
	}

	public Integer getCountSubFolder() {
		return countSubFolder;
	}

	public void setCountSubFolder(Integer countSubFolder) {
		this.countSubFolder = countSubFolder;
	}

	public Integer getCountPhotoInSubFolder() {
		return countPhotoInSubFolder;
	}

	public void setCountPhotoInSubFolder(Integer countPhotoInSubFolder) {
		this.countPhotoInSubFolder = countPhotoInSubFolder;
	}
	
	public int getDepth() {
		return depth;
	}
}
