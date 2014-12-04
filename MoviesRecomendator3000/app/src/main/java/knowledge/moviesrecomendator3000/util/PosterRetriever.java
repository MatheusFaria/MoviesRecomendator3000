package knowledge.moviesrecomendator3000.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;


public class PosterRetriever extends AsyncTask<URL, Integer, Bitmap> {

    @Override
    protected Bitmap doInBackground(URL... urls) {
        Bitmap poster = null;

        try {
            poster = BitmapFactory.decodeStream(urls[0].openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return poster;
    }

}
