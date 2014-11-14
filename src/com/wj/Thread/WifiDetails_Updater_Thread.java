package com.wj.Thread;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.LinearLayout;
import com.wj.View.DetailsView;
import com.wj.View.RadarView;
import com.wj.View.WifiSSIDView;
import com.wj.WJ_WifiManager.R;
import com.wj.parameters.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wlf on 10/26/14.
 */
public class WifiDetails_Updater_Thread extends Thread{

    private WifiManager wifi;
    private ArrayList<ScanResult> scanResults;
    private RadarView radarView;
    private DetailsView detailsView;
    private Activity activity;
    private String BSSID;



    public WifiDetails_Updater_Thread(Activity _activity, String _BSSID){
        this.activity = _activity;
        this.BSSID = _BSSID;
        wifi = (WifiManager) this.activity.getSystemService(Context.WIFI_SERVICE);
        LinearLayout linearLayout = (LinearLayout) this.activity.findViewById(R.id.wifi_details);
        radarView = new RadarView(this.activity);
        detailsView = new DetailsView(this.activity);
        linearLayout.addView(detailsView);

    }
    @Override
    public void run(){

        try {
            while(true){
                if(wifi.isWifiEnabled()) {
                    wifi.startScan();
                    final List<ScanResult> sr = wifi.getScanResults();
                    int index = 0;
                    for(ScanResult r : sr){
                        if(r.BSSID.equals(this.BSSID)){
                            radarView.setStrength(r.level);
                            break;
                        }
                        ++index;
                    }
                    final String SSID;
                    final Integer level;
                    final Integer frequency;

                    try {
                        SSID = sr.get(index).SSID;
                        level = sr.get(index).level;
                        frequency = sr.get(index).frequency;

                    } catch (IndexOutOfBoundsException e) {
                        continue;
                    }

                    if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                radarView.update();
                                if(level > Parameter.getSignalGood()){
                                    detailsView.setText("\nAP SSID is:    "+SSID+"\n"+
                                            "Frequency is:   "+frequency+"\n"+
                                            "Signal dBm is:   "+level+"\n"+
                                            "Strength Level is:     Good"+"\n");
                                }else if(level>Parameter.getSignalFair() && level<Parameter.getSignalGood()){
                                    detailsView.setText("\nAP SSID is:    "+SSID+"\n"+
                                            "Frequency is:   "+frequency+"\n"+
                                            "Signal dBm is:   "+level+"\n"+
                                            "Strength Level is:     Fair"+"\n");
                                }else {
                                    detailsView.setText("\nAP SSID is:    "+SSID+"\n"+
                                            "Frequency is:   "+frequency+"\n"+
                                            "Signal dBm is:   "+level+"\n"+
                                            "Strength Level is:     Poor"+"\n");
                                }
                            }
                        });
                    }
                    sleep(100);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
