package com.wj.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.wj.parameters.Parameter;

/**
 * Created by wlf on 10/26/14.
 */
public class RadarView extends View {

    private Paint radarBackground,textPaint,radarGreen,radarYellow,radarRed,linePaint,cursorPaint;
    private RectF radarOval;
    private int textHeight,lineSize;


    private float cursorAngle, nextCursorAngle;


    public RadarView(Context context) {
        super(context);
        setWillNotDraw(false);
        initRadarView();
    }

    public RadarView(Context context, AttributeSet attrs)
    {

        super(context, attrs);
        setWillNotDraw(false);
        initRadarView();
    }

    public RadarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
        initRadarView();
    }

    private void initRadarView(){


        cursorAngle=nextCursorAngle=0;

        radarBackground = new Paint();
        radarBackground.setColor(Color.BLACK);
        radarBackground.setAntiAlias(true);


        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(30);
        textHeight=(int)textPaint.measureText("yY");


        lineSize = 30;
        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(4);
        linePaint.setAntiAlias(true);


        radarOval = new RectF(0,-500,getMeasuredWidth(),getMeasuredHeight());

        int strokeWidth = 20;

        radarGreen = new Paint();
        radarGreen.setStyle(Paint.Style.STROKE);
        radarGreen.setColor(Color.GREEN);
        radarGreen.setStrokeWidth(strokeWidth);
        radarGreen.setAntiAlias(true);

        radarYellow = new Paint();
        radarYellow.setStyle(Paint.Style.STROKE);
        radarYellow.setColor(Color.YELLOW);
        radarYellow.setStrokeWidth(strokeWidth);
        radarYellow.setAntiAlias(true);

        radarRed = new Paint();
        radarRed.setStyle(Paint.Style.STROKE);
        radarRed.setColor(Color.RED);
        radarRed.setStrokeWidth(strokeWidth);
        radarRed.setAntiAlias(true);

        cursorPaint = new Paint();
        cursorPaint.setStyle(Paint.Style.STROKE);
        cursorPaint.setStrokeWidth(3);
        cursorPaint.setColor(Color.RED);
        cursorPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int radius = Math.min((int) (1.414*getMeasuredWidth()/2),(int) (1.414*getMeasuredHeight()/2))-50;

        radarOval.set((int)((getMeasuredWidth()-2*radius)/2), (getMeasuredHeight()-radius)/2, 2*radius+(int)((getMeasuredWidth()-2*radius)/2), (getMeasuredHeight()-radius)/2+2*radius);

        canvas.drawArc(radarOval, -135, 90, true, radarBackground);


        canvas.drawArc(radarOval, -45-(float)((-40-Parameter.getSignalGood())*3)/2, (float)((-40-Parameter.getSignalGood())*3)/2, false, radarGreen);
        canvas.drawArc(radarOval, -45-(float)((-40-Parameter.getSignalFair())*3)/2,  (float)((Parameter.getSignalGood()-Parameter.getSignalFair())*3)/2, false, radarYellow);
        canvas.drawArc(radarOval, -135, (float)((Parameter.getSignalFair()+100)*3)/2, false, radarRed);


        canvas.save();
        canvas.translate(radarOval.left, radarOval.top);
        canvas.rotate(-45,radius,(radarOval.bottom-radarOval.top)/2);

        for(int i=-100;i<=-40;++i){
            if(i%10==0){

                canvas.drawLine(radius, 10,(radarOval.right-radarOval.left)/2, 2*lineSize,linePaint);
                canvas.save();
                canvas.translate(0, lineSize*2+textHeight);
                canvas.drawText(""+i, (radarOval.right-radarOval.left)/2-textPaint.measureText(""+i)/2, 0, textPaint);
                canvas.restore();
            }
            else{
                canvas.drawLine(radius, 10,(radarOval.right-radarOval.left)/2, lineSize,linePaint);
            }
            canvas.rotate((float)1.5,radius,(radarOval.bottom-radarOval.top)/2);
        }


        canvas.restore();
        canvas.save();
        canvas.translate(radarOval.left, radarOval.top);
        canvas.rotate(cursorAngle,radius,(radarOval.bottom-radarOval.top)/2);        // range [-45, 45]
        canvas.drawLine(radius, 0,(radarOval.right-radarOval.left)/2, radius,cursorPaint);
        canvas.restore();


    }
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if(!hasFocus()){

        }
        super.onWindowFocusChanged(hasWindowFocus);
    }


    public void setStrength(int strength){
        if(strength>-40){
            strength = -40;
        }
        if(strength<-100){
            strength=-100;
        }
        nextCursorAngle=(strength+100)*3/2-45;
    }

    public void update(){
        if(cursorAngle==nextCursorAngle)return;
        cursorAngle+=(nextCursorAngle-cursorAngle)/2.0f;
        invalidate();
    }
}
