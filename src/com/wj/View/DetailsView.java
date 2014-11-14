package com.wj.View;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by wlf on 10/26/14.
 */
public class DetailsView extends TextView{

        public DetailsView(Context context) {
        super(context);
        //    setWillNotDraw(false);
        super.setTextSize(20
        );
        super.setTextColor(Color.GREEN);
//        super.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public DetailsView(Context context, AttributeSet attrs)
    {

        super(context, attrs);
        //    setWillNotDraw(false);
        super.setTextSize(20);
        super.setTextColor(Color.GREEN);
 //       super.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public DetailsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //    setWillNotDraw(false);
        super.setTextSize(20);
        super.setTextColor(Color.GREEN);
//        super.setGravity(Gravity.CENTER_HORIZONTAL);
    }


    public void printDetails(CharSequence text){
        super.setText(text);
    }

}
