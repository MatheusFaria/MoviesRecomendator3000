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

import org.coode.owlapi.obo.renderer.OBOExceptionHandler;

import knowledge.moviesrecomendator3000.R;

public class SlidingPane extends Activity {

    private SlidingPaneLayout slidingPane;
    int screenHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sliding_pane);

        this.slidingPane = (SlidingPaneLayout) findViewById(R.id.sliding_pane);
        this.screenHeight = getHeight();
    }

    public void animate(View view) {
        slidingPane.slideUp();
    }

    private int getHeight() {
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        return size.y;
    }
}
