package knowledge.moviesrecomendator3000.model;

import android.graphics.Bitmap;

public class Movie {

	private String title;
    Bitmap poster;
	
	public Movie() {
		
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

    public Bitmap getPoster() {
        return poster;
    }

    public void setPoster(Bitmap poster) {
        this.poster = poster;
    }
	
}
