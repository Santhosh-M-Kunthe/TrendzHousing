package in.siteurl.www.trendzcrm;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by siteurl on 27/3/18.
 */


public class DynamicRectangularLayout extends RelativeLayout {

    public DynamicRectangularLayout(Context context) {
        super(context);
    }


    public DynamicRectangularLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public DynamicRectangularLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    // Here note that the height is width/2

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, (heightMeasureSpec / 4));
        int size = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), (size / 4));
    }


}


