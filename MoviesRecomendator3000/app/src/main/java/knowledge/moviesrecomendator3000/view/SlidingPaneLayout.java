package knowledge.moviesrecomendator3000.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.ScrollView;

import knowledge.moviesrecomendator3000.R;

public class SlidingPaneLayout extends ViewGroup {

    private ScrollView upperPane, lowerPane;
    private Point touchBegin, touchEnd;

    private int collapsedHeight;
    private int paneOffset;
    private int upperPaneTop, lowerPaneTop;
    private int touchSlop;
    private boolean collapsed;


    public SlidingPaneLayout(Context context, AttributeSet attributes) {
        super(context, attributes);

        TypedArray typedAttrs = context.getTheme().obtainStyledAttributes(attributes,
                R.styleable.SlidingPaneLayout, 0, 0);
        collapsedHeight = typedAttrs.getInt(R.styleable.SlidingPaneLayout_collapsed_height, 0);
        typedAttrs.recycle();

        paneOffset = 0;

        ViewConfiguration vc = ViewConfiguration.get(context);
        touchSlop = vc.getScaledTouchSlop();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        upperPane = (ScrollView) getChildAt(0);
        lowerPane = (ScrollView) getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChild(upperPane, widthMeasureSpec, heightMeasureSpec);
        measureChild(lowerPane, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

        upperPaneTop = getTop() - paneOffset;
        lowerPaneTop = upperPane.getMeasuredHeight() - paneOffset;

        Log.i("Layout", "Upper Pane Top: "+upperPaneTop);
        Log.i("Layout", "Lower Pane Top: "+lowerPaneTop);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        upperPane.layout(left, upperPaneTop, right, upperPaneTop + upperPane.getMeasuredHeight());
        lowerPane.layout(left, lowerPaneTop, right, lowerPaneTop + lowerPane.getMeasuredHeight());


    }

    public void slideUp() {
        this.paneOffset = this.getMeasuredHeight()-collapsedHeight;

        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(lowerPane, "translationY", paneOffset, 0);
        objectAnimator.start();

        objectAnimator = ObjectAnimator.ofFloat(upperPane, "translationY", paneOffset, 0);
        objectAnimator.start();

        this.collapsed = true;
        this.invalidate();
        this.requestLayout();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        final int eventAction = event.getAction();
        boolean intercept = false;

        switch (eventAction) {
            case MotionEvent.ACTION_DOWN:
                touchBegin = new Point((int) event.getX(), (int) event.getY());
                intercept = false;
            break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                intercept = false;
            break;
            case MotionEvent.ACTION_MOVE:
                touchEnd = new Point((int) event.getX(), (int) event.getY());
                float diffY =  touchBegin.y - touchEnd.y;

                if(collapsed && distance(touchBegin, touchEnd) > touchSlop && lowerPaneTop > collapsedHeight) {
                    // Just intercept if it is a valid drag and the lower pane is not fully expanded
                    intercept = true;
                } else if(collapsed && diffY<0 && lowerPane.getScrollY() == 0) {
                    intercept = true;
                }
            break;
        }

        return intercept;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(event.getAction() == MotionEvent.ACTION_MOVE) {
            float diffY =  touchBegin.y - event.getY();

            if(diffY<0 && diffY<upperPaneTop) {
                // Scrolling down
                diffY = upperPaneTop;
            }

            if(diffY>0 && diffY>(lowerPaneTop-collapsedHeight)) {
                // Scrolling up
                diffY = lowerPaneTop-collapsedHeight;
            }

            // Update the paneOffset and the Point of reference
            touchBegin = new Point((int) event.getX(), (int) event.getY());
            paneOffset += (int) diffY;

            this.invalidate();
            this.requestLayout();
        }

        return super.onTouchEvent(event);
    }

    private double distance(Point p1, Point p2) {
        // Regular Distance between two points.
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }
}