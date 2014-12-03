package knowledge.moviesrecomendator3000.model;

public class Movie {

	private String title;

    private String posterURL;
	
	public Movie(){
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }
	
}
