package com.wj.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import com.wj.Thread.WifiRadar_Update_Thread;
import com.wj.WJ_WifiManager.R;

/**
 * Created by wlf on 10/26/14.
 */
public class RadarActivity extends Activity{

    private  WifiRadar_Update_Thread wifiRadar_update_thread;
    String BSSID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_radar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        BSSID = getIntent().getStringExtra("wifi_BSSID");
        System.out.println(BSSID);
        wifiRadar_update_thread = new WifiRadar_Update_Thread(this, BSSID);
        wifiRadar_update_thread.start();
    }


    @Override
    public void onPause() {
        if(wifiRadar_update_thread!=null){
            wifiRadar_update_thread.interrupt();
            wifiRadar_update_thread=null;
        }
        super.onPause();
    }
    @Override
    public void onDestroy() {
        if(wifiRadar_update_thread!=null){
            wifiRadar_update_thread.interrupt();
            wifiRadar_update_thread=null;
        }
        super.onDestroy();
    }
}
