package com.wj.Thread;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import com.wj.parameters.Parameter;
import java.util.List;

/**
 * Created by wlf on 10/27/14.
 */
public class AlertThread extends Thread{

    final private Activity activity;
    private List<ScanResult> scanResults;
    private WifiManager wifi;
    private boolean stopcheck = false;
    final private AlertThread alertThread = this;
    private boolean alreadycheck = false;

    public AlertThread(Activity _activity){
        this.activity = _activity;
    }

    @Override
    public void run() {

        while (!stopcheck){
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e){

            }

            wifi = (WifiManager) activity.getSystemService(Context.WIFI_SERVICE);
            scanResults = wifi.getScanResults();
            for (final ScanResult sr : scanResults) {
                if (sr.BSSID.equals(wifi.getConnectionInfo().getBSSID()) && sr.level < Parameter.getSignalFair()) {
                    if(!alreadycheck){
                        alreadycheck = startAlert(sr.SSID, sr.level);
                    }
                }

            }
        }
    }

    public boolean startAlert(final String SSID, final Integer level){
        this.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                alertDialogBuilder.setMessage("Alter: Your connected Wifi signal"+SSID+" now is Poor ("+String.valueOf(level)+"). Do you want to keep checking?");

                alertDialogBuilder.setPositiveButton("Keep checking",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                alreadycheck = false;
                            }
                        });
                alertDialogBuilder.setNegativeButton("Never check again",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                stopcheck = true;
                                alertThread.interrupt();

                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        return true;
    }
}
