package com.wj.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import com.wj.Thread.StrengthGraph_Updater;
import com.wj.WJ_WifiManager.R;

/**
 * Created by wlf on 10/27/14.
 */
public class StrengthActivity extends Activity{

    private StrengthGraph_Updater strengthGraph_updater;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_signalstrength);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        strengthGraph_updater = new StrengthGraph_Updater(this);
        strengthGraph_updater.start();
    }

    @Override
    public void onResume() {
        if(strengthGraph_updater!=null){
            strengthGraph_updater.interrupt();
        }

        strengthGraph_updater = new StrengthGraph_Updater(this);
        strengthGraph_updater.start();
        super.onResume();
    }
    @Override
    public void onPause() {
        if(strengthGraph_updater!=null){
            strengthGraph_updater.interrupt();
            strengthGraph_updater=null;
        }
        super.onPause();
    }
    @Override
    public void onDestroy() {
        if(strengthGraph_updater!=null){
            strengthGraph_updater.interrupt();
            strengthGraph_updater=null;
        }
        super.onDestroy();
    }
}
