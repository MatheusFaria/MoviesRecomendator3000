package knowledge.moviesrecomendator3000.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import knowledge.moviesrecomendator3000.R;
import knowledge.moviesrecomendator3000.controller.Controller;
import knowledge.moviesrecomendator3000.model.Movie;


public class HomeActivity extends Activity {

    private static final String ONTOLOGY_PATH = "movies_recomendation.owl";
    private Spinner moodSpinner, companionSpinner;
    InputStream ontologyFileIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.moodSpinner = (Spinner) findViewById(R.id.spinner_mood);
        this.companionSpinner = (Spinner) findViewById(R.id.spinner_companion);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void recommendButtonClick(View view) {
        String mood = this.moodSpinner.getSelectedItem().toString();
        String companion = this.companionSpinner.getSelectedItem().toString();

        String movieTitles = "";

        try {
            ontologyFileIS = getAssets().open(ONTOLOGY_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Ontology creation", "error");
        }

        /*ArrayList<Movie> recommendedMovies = Controller.recommend(mood, companion, ontologyFileIS);
        for(Movie recommendedMovie : recommendedMovies) {
            movieTitles += recommendedMovie.getTitle()+"\n";
            Log.i("Recommendation", "I recommend to you: " + recommendedMovie.getTitle());
        }*/

        Toast.makeText(getApplicationContext(), movieTitles, Toast.LENGTH_LONG).show();
    }
}
