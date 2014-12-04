package knowledge.moviesrecomendator3000.view;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import knowledge.moviesrecomendator3000.R;
import knowledge.moviesrecomendator3000.controller.MovieController;
import knowledge.moviesrecomendator3000.model.Movie;

public class MovieActivity extends Activity {

    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        this.movie = MovieController.getMovie( (Integer) this.getIntent().getExtras().get("MovieIndex"));

        this.getActionBar().setTitle(this.movie.getTitle());

        ImageView img = (ImageView) findViewById(R.id.poster);
        img.setImageBitmap(this.movie.getPoster());

        TextView directorTextView = (TextView) findViewById(R.id.txtDirector);
        directorTextView.setText(this.movie.getDirector());

        TextView rateTextView = (TextView) findViewById(R.id.txtRate);
        rateTextView.setText(this.movie.getRate());

        TextView genresTextView = (TextView) findViewById(R.id.txtGenres);
        genresTextView.setText(this.movie.getGenres());

        TextView descriptionTextView = (TextView) findViewById(R.id.txtDescription);
        descriptionTextView.setText(this.movie.getDescription());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
