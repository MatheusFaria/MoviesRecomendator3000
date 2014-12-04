package knowledge.moviesrecomendator3000.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import knowledge.moviesrecomendator3000.R;
import knowledge.moviesrecomendator3000.controller.Controller;
import knowledge.moviesrecomendator3000.controller.MovieController;
import knowledge.moviesrecomendator3000.model.Movie;
import knowledge.moviesrecomendator3000.util.MovieListener;

public class SlidingPaneActivity extends Activity implements MovieListener {

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
        ArrayList<String> moviesIRIs = Controller.recommend(mood, companion, ontologyFileIS);

        try {
            ArrayList<Movie> movies = MovieController.getMovies(moviesIRIs, this);
            //addMoviesToContainer(movies);

        } catch (Exception e) {
            e.printStackTrace();
        }

        slidingPane.slideUp();
    }

    private void addMoviesToContainer(ArrayList<Movie> movies) {
        View movieEntry = getLayoutInflater().inflate(R.layout.movie_item, null);

        for (Movie movie : movies) {
            TextView movieTitle = (TextView) movieEntry.findViewById(R.id.movie_title);
            movieTitle.setText(movie.getTitle());

            movieContainer.addView(movieEntry);
        }
    }

    public void addMovieToContainer(Movie movie){
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

}
