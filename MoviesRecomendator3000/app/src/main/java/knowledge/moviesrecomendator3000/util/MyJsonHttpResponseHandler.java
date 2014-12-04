package knowledge.moviesrecomendator3000.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import knowledge.moviesrecomendator3000.controller.MovieController;
import knowledge.moviesrecomendator3000.model.Movie;

public class MyJsonHttpResponseHandler extends JsonHttpResponseHandler {

    private MovieListener slidingPaneActivity;

    public MyJsonHttpResponseHandler(final MovieListener slidingPaneActivity) {
        this.slidingPaneActivity = slidingPaneActivity;
    }

    @Override
    public synchronized void onSuccess(int statusCode, Header[] headers, JSONObject movie) {

        try {
            Movie newMovie = new Movie();
            String posterURL = movie.getString("Poster");

            newMovie.setTitle(movie.getString("Title"));
            newMovie.setRate(movie.getString("Rated"));
            newMovie.setDirector(movie.getString("Director"));
            newMovie.setGenres(movie.getString("Genre"));
            newMovie.setImbdID(movie.getString("imdbID"));
            newMovie.setDescription(movie.getString("Plot"));
            newMovie.setPoster(MovieUtil.getBitmap(posterURL));

            slidingPaneActivity.addMovieToContainer(newMovie);
            MovieController.addMovie(newMovie);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}