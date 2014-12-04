package knowledge.moviesrecomendator3000.controller;

import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;

import knowledge.moviesrecomendator3000.model.Movie;
import knowledge.moviesrecomendator3000.util.MovieListener;
import knowledge.moviesrecomendator3000.util.MovieUtil;
import knowledge.moviesrecomendator3000.util.MyJsonHttpResponseHandler;

public class MovieController {

    private static ArrayList<Movie> movies;

    public static ArrayList<Movie> getMovies(ArrayList<String> moviesIRIs,
            final MovieListener slidingPaneActivity) throws Exception {

        movies = new ArrayList<Movie>();

        for(String movieIRI : moviesIRIs) {
            String movieTitle = MovieUtil.getMovieTitle(movieIRI);
            String URL = "http://www.omdbapi.com/?t="+movieTitle+"&r=json";

            AsyncHttpClient client = new AsyncHttpClient();
            client.get(URL, new MyJsonHttpResponseHandler(slidingPaneActivity));
        }

        return movies;
    }

    public static ArrayList<Movie> getMovies() {
        return movies;
    }

    public static void addMovie(Movie movie) {
        movies.add(movie);
    }
}