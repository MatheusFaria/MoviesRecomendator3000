package knowledge.moviesrecomendator3000.view;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.coode.owlapi.obo.renderer.OBOExceptionHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import knowledge.moviesrecomendator3000.R;
import knowledge.moviesrecomendator3000.controller.Controller;
import knowledge.moviesrecomendator3000.model.Movie;

public class SlidingPaneActivity extends Activity {

    private static final String ONTOLOGY_PATH = "movies_recomendation.owl";

    private SlidingPaneLayout slidingPane;
    private LinearLayout movieContainer;
    private Spinner moodSpinner, companionSpinner;
    InputStream ontologyFileIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_pane);

        initViews();
    }

    public void recommendButtonClick(View view) {
        String mood = this.moodSpinner.getSelectedItem().toString();
        String companion = this.companionSpinner.getSelectedItem().toString();

        try {
            ontologyFileIS = getAssets().open(ONTOLOGY_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Ontology creation", "error");
        }

        movieContainer.removeAllViews();
        ArrayList<String> recommendedMovies = Controller.recommend(mood, companion, ontologyFileIS);
        for(String movieIRI: recommendedMovies) {
           addMovie(movieIRI);
        }

        slidingPane.slideUp();
    }

    private void addMovieToContainer(Movie movie) {
        View movieEntry = getLayoutInflater().inflate(R.layout.movie_item, null);

        TextView movieTitle = (TextView) movieEntry.findViewById(R.id.movie_title);
        movieTitle.setText(movie.getTitle());

        movieContainer.addView(movieEntry);
    }

    private void addMovie(String movieIRI) {
        String URL = "";

        try {
            URL = "http://www.omdbapi.com/?t="+getMovieTitle(movieIRI)+"&r=json";
        } catch (Exception e) {
            e.printStackTrace();
        }

        boolean waiting = true;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URL, new JsonHttpResponseHandler() {
            @Override
            public synchronized void onSuccess(int statusCode, Header[] headers, JSONObject movie) {

                try {
                    Movie newMovie = new Movie();

                    String movieTitle = movie.getString("Title");
                    String posterURL = movie.getString("Poster");

                    newMovie.setTitle(movieTitle);
                    newMovie.setPosterURL(posterURL);

                    addMovieToContainer(newMovie);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initViews() {
        this.slidingPane = (SlidingPaneLayout) findViewById(R.id.sliding_pane);
        this.moodSpinner = (Spinner) findViewById(R.id.spinner_mood);
        this.companionSpinner = (Spinner) findViewById(R.id.spinner_companion);
        this.movieContainer = (LinearLayout) findViewById(R.id.lower_pane_container);
    }

    private String getMovieTitle(String movieIRI) throws Exception {
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
}
