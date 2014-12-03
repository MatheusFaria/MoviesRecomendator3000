package knowledge.moviesrecomendator3000.controller;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import knowledge.moviesrecomendator3000.model.Movie;

public class MovieController {

    static boolean waiting;

    public static ArrayList<Movie> getMovies(ArrayList<String> moviesIRIs) throws Exception {
        final ArrayList<Movie> movies = new ArrayList();

        waiting = true;
        final AsyncHttpClient client = new AsyncHttpClient();

        for(String movieIRI : moviesIRIs) {
            String movieTitle = getMovieTitle(movieIRI);
            String URL = "http://www.omdbapi.com/?t="+movieTitle+"&r=json";

            client.get(URL, new JsonHttpResponseHandler() {
                @Override
                public synchronized void onSuccess(int statusCode, Header[] headers, JSONObject movie) {

                    try {
                        Movie newMovie = new Movie();

                        String movieTitle = movie.getString("Title");
                        String posterURL = movie.getString("Poster");

                        newMovie.setTitle(movieTitle);
                        newMovie.setPosterURL(posterURL);

                        movies.add(newMovie);
                        Log.i("Movie", newMovie.getTitle());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return movies;
    }

    private static String getMovieTitle(String movieIRI) throws Exception {
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

    public static void stopWaiting() {
        Log.i("waiting", "stopping");
        waiting = false;
    }
}