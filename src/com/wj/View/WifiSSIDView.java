package com.wj.View;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.TextView;

/**
 * Created by wlf on 10/26/14.
 */
public class WifiSSIDView extends TextView{

    public WifiSSIDView(Context context) {
        super(context);
        //    setWillNotDraw(false);
        super.setTextSize(20
        );
        super.setTextColor(Color.GREEN);
        super.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public WifiSSIDView(Context context, AttributeSet attrs)
    {

        super(context, attrs);
        //    setWillNotDraw(false);
        super.setTextSize(20);
        super.setTextColor(Color.GREEN);
        super.setGravity(Gravity.CENTER_HORIZONTAL);
    }

    public WifiSSIDView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //    setWillNotDraw(false);
        super.setTextSize(20);
        super.setTextColor(Color.GREEN);
        super.setGravity(Gravity.CENTER_HORIZONTAL);
    }


    public void setWifiSSID(CharSequence text){
        super.setText(text);
    }
}
