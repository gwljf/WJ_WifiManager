package com.wj.Thread;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYStepMode;
import com.wj.View.SignalStrengthView;
import com.wj.WJ_WifiManager.R;
import com.wj.utils.ColorPool;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wlf on 10/27/14.
 */
public class StrengthGraph_Updater extends Thread{

    HashMap<String,SignalStrengthView> hash;
    XYPlot plot;
    WifiManager wifi;
    Activity activity;
    public StrengthGraph_Updater(Activity activity){
        this.activity = activity;
        wifi=(WifiManager)activity.getSystemService(Context.WIFI_SERVICE);


        plot=(XYPlot)activity.findViewById(R.id.graph);
        hash = new HashMap<String,SignalStrengthView>();

        plot.setTitle("");
        plot.setTicksPerRangeLabel(1);

        plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.TRANSPARENT);
        plot.getGraphWidget().getGridDomainLinePaint().setColor(Color.TRANSPARENT);
        plot.getGraphWidget().getDomainLabelPaint().setTextSize(15);
        plot.getGraphWidget().getRangeLabelPaint().setTextSize(15);

        plot.setDomainStep(XYStepMode.INCREMENT_BY_VAL, 2);

        plot.setRangeStep(XYStepMode.INCREMENT_BY_VAL, 20);


        plot.setDomainBoundaries(-1, 15, BoundaryMode.FIXED);



        plot.setDomainLabel("Channel");


        DecimalFormat df = new DecimalFormat("0");
        df.setParseIntegerOnly(true);
        plot.setDomainValueFormat(df);
        plot.setRangeBoundaries(-100, -30, BoundaryMode.FIXED);
        plot.setRangeLabel("Strength");
        plot.getLegendWidget().setVisible(false);


    }



    @Override
    public void run() {


        try {
            while(true){
                if(wifi.isWifiEnabled()){
                    WifiInfo wifiINFO  = wifi.getConnectionInfo();
                    wifi.startScan();
                    List<ScanResult> sr = wifi.getScanResults();

                    for(SignalStrengthView s : hash.values()){
                        plot.removeSeries(s);
                    }
                    plot.removeMarkers();

                    for(ScanResult r : sr){
                        SignalStrengthView series = hash.get(r.BSSID);
                        if(series == null){
                            series = new SignalStrengthView(
                                    r.SSID,
                                    (r.frequency-2407)/5,
                                    r.level,
                                    ColorPool.getColor(hash.size()),
                                    ColorPool.getTransparentColor(hash.size()));
                            hash.put(r.BSSID, series);
                        }
                        else{
                            series.setChannel((r.frequency-2407)/5);
                            series.setStrength(r.level);
                            series.update();
                        }

                        plot.addSeries(series, series.getFormatter());
                        plot.addMarker(series.getMarker(activity.getResources().getConfiguration().orientation ));
                    }

                    plot.redraw();
                }
                Thread.sleep(100);
            }

        } catch (InterruptedException e) {

            for(SignalStrengthView s : hash.values()){
                plot.removeSeries(s);
            }

        }


    }
}
