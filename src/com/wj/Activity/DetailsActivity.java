package com.wj.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import com.wj.Thread.WifiDetails_Updater_Thread;
import com.wj.Thread.WifiRadar_Update_Thread;
import com.wj.WJ_WifiManager.R;

/**
 * Created by wlf on 10/26/14.
 */
public class DetailsActivity extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_details);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        String BSSID = getIntent().getStringExtra("wifi_BSSID");
        new WifiDetails_Updater_Thread(this, BSSID).start();

    }
}
