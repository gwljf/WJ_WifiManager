package com.wj.Thread;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.widget.LinearLayout;
import com.wj.View.RadarView;
import com.wj.View.WifiSSIDView;
import com.wj.WJ_WifiManager.R;
import com.wj.parameters.Parameter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wlf on 10/26/14.
 */
public class WifiRadar_Update_Thread extends Thread {

    private WifiManager wifi;
    private ArrayList<ScanResult> scanResults;
    private RadarView radarView;
    private WifiSSIDView wifiSSID;
    private Activity activity;
    private String BSSID;



    public WifiRadar_Update_Thread(Activity _activity, String _BSSID){
        this.activity = _activity;
        this.BSSID = _BSSID;
        wifi = (WifiManager) this.activity.getSystemService(Context.WIFI_SERVICE);
        LinearLayout linearLayout = (LinearLayout) this.activity.findViewById(R.id.radarView);
        radarView = new RadarView(this.activity);
        wifiSSID = new WifiSSIDView(this.activity);
        linearLayout.addView(wifiSSID);
        linearLayout.addView(radarView);

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
                    try {
                        SSID = sr.get(index).SSID;
                        level = sr.get(index).level;
                    } catch (IndexOutOfBoundsException e) {
                        continue;
                    }

                    if (activity != null) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                radarView.update();
                                if(level > Parameter.getSignalGood()){
                                    wifiSSID.setText("\nAP SSID is:    "+SSID+"\n"+
                                            "Signal Strength is:    "+level+" (Good)");
                                }else if(level>Parameter.getSignalFair() && level<Parameter.getSignalGood()){
                                    wifiSSID.setText("\nAP SSID is:    "+SSID+"\n"+
                                            "Signal Strength is:    "+level+" (Fair)");
                                }else {
                                    wifiSSID.setText("\nAP SSID is:    "+SSID+"\n"+
                                            "Signal Strength is:    "+level+" (Poor)");
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
