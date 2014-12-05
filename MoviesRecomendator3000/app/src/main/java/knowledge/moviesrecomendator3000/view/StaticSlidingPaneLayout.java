package knowledge.moviesrecomendator3000.view;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;

public class StaticSlidingPaneLayout extends SlidingPaneLayout {
    public StaticSlidingPaneLayout(Context context, AttributeSet attributes) {
        super(context, attributes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChild(upperPane, widthMeasureSpec, heightMeasureSpec);
        measureChild(lowerPane, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);

        upperPaneTop = getTop();
        lowerPaneTop = upperPane.getMeasuredHeight() - paneOffset;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected int getUpperPaneBottom() {
        return upperPaneTop + upperPane.getMeasuredHeight() - paneOffset;
    }

    @Override
    protected int getUpperPaneTop() {
        return getTop();
    }

    @Override
    protected boolean getInterceptConditions(float diffY, Point touchEnd) {
        boolean intercept = false;

        if(collapsed && distance(touchBegin, touchEnd) > touchSlop
                && lowerPaneTop > collapsedHeight) {
            // Just intercept if it is a valid drag and the lower pane is not fully expanded
            intercept = true;
        } else if(collapsed && diffY<0 && lowerPane.getScrollY() == 0) {
            intercept = true;
        }

        return intercept;
    }

    @Override
    protected float checkSuperiorBound(float diffY) {
        return diffY;
    }

    @Override
    protected float checkInferiorBound(float diffY) {
        return super.checkInferiorBound(diffY);
    }
}
