package knowledge.moviesrecomendator3000.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MovieUtil {

    public static String getMovieTitle(String movieIRI) throws Exception {
        String iriParts[] = movieIRI.split("#");
        String movieTitle = "";

        if (iriParts.length == 2) {
            movieTitle = iriParts[1].replaceAll("_", "+");
            movieTitle = movieTitle.substring(0, movieTitle.length()-1);
        } else {
            throw new Exception("Invalid Movie IRI");
        }

        return movieTitle;
    }

    public static Bitmap getBitmap(String stringPosterURL) throws IOException, ExecutionException, InterruptedException {
        URL posterURL = new URL(stringPosterURL);
        Bitmap poster = new PosterRetriever().execute(posterURL).get();

        return poster;
    }
}
