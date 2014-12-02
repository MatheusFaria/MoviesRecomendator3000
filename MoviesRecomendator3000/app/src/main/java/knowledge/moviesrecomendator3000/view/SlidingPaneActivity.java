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

import org.coode.owlapi.obo.renderer.OBOExceptionHandler;

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
        initOntology();
    }

    public void recommendButtonClick(View view) {
        String mood = this.moodSpinner.getSelectedItem().toString();
        String companion = this.companionSpinner.getSelectedItem().toString();

        ArrayList<Movie> recommendedMovies = Controller.recomend(mood, companion);
        for(Movie movie: recommendedMovies) {
            addMovieToContainer(movie);
        }

        slidingPane.slideUp();
    }

    private void addMovieToContainer(Movie movie) {
        View movieEntry = getLayoutInflater().inflate(R.layout.movie_item, null);

        TextView movieTitle = (TextView) movieEntry.findViewById(R.id.movie_title);
        movieTitle.setText(movie.getTitle());

        movieContainer.addView(movieEntry);
    }

    private void initViews() {
        this.slidingPane = (SlidingPaneLayout) findViewById(R.id.sliding_pane);
        this.moodSpinner = (Spinner) findViewById(R.id.spinner_mood);
        this.companionSpinner = (Spinner) findViewById(R.id.spinner_companion);
        this.movieContainer = (LinearLayout) findViewById(R.id.lower_pane_container);
    }

    private void initOntology() {
        try {
            ontologyFileIS = getAssets().open(ONTOLOGY_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Ontology creation", "error");
        }

        Controller.initializeOntology(ontologyFileIS);
    }
}
