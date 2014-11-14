package com.wj.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.*;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.wj.Thread.AlertThread;
import com.wj.WJ_WifiManager.R;
import com.wj.parameters.Parameter;

import java.util.List;

public class Wifi_Lists_Activity extends SherlockActivity {
    /**
     * Called when the activity is first created.
     */
    private SwipeMenuListView mListView;




    private WifiManager wifiManager;
    private List<ScanResult> wifilist;
    private WifiListAdapter wifiListAdapter;
    private Activity activity = this;


    @Override
    protected void onRestart(){
        super.onRestart();
        wifilist = wifiManager.getScanResults();
        wifiListAdapter = new WifiListAdapter();
        mListView.setAdapter(wifiListAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_lists);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//        if (!mWifi.isConnected()) {
//            Toast.makeText(this, "Your wifi is off now.", Toast.LENGTH_LONG).show();
//
//        }
//
//        Toast.makeText(this, mWifi.getDetailedState().toString(), Toast.LENGTH_LONG).show();
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);


        if(!wifiManager.isWifiEnabled()){
            Toast.makeText(this, "Your wifi is off now.", Toast.LENGTH_LONG).show();
        }

        new AlertThread(this).start();

        wifilist = wifiManager.getScanResults();


        mListView = (SwipeMenuListView) findViewById(R.id.listView);
        wifiListAdapter = new WifiListAdapter();
        mListView.setAdapter(wifiListAdapter);


        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem radar = new SwipeMenuItem(getBaseContext());
                radar.setBackground(new ColorDrawable(Color.BLUE));
                radar.setWidth(dp2px(90));
                radar.setTitle("Open");
                radar.setTitleSize(18);
                radar.setTitleColor(Color.WHITE);
                menu.addMenuItem(radar);


                SwipeMenuItem details = new SwipeMenuItem(getBaseContext());
                details.setBackground(new ColorDrawable(Color.DKGRAY));
                details.setWidth(dp2px(90));
                details.setTitle("Details");
                details.setTitleSize(18);
                details.setTitleColor(Color.WHITE);
                menu.addMenuItem(details);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                ScanResult item = wifilist.get(position);
                switch (index) {
                    case 0:
                        gotoWifiRadar(item);
                        break;
                    case 1:
                        details(item);
                        break;
                }
                return false;
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });


    }

    private void details(ScanResult item) {

        Intent intent = new Intent(activity, DetailsActivity.class);
        intent.putExtra("wifi_BSSID",item.BSSID);
        startActivity(intent);
    }

    private void gotoWifiRadar(ScanResult item) {

        Intent intent = new Intent(activity, RadarActivity.class);
        intent.putExtra("wifi_BSSID",item.BSSID);
        startActivity(intent);

    }



    class WifiListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return wifilist.size();
        }

        @Override
        public ScanResult getItem(int position) {
            return wifilist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getBaseContext(), R.layout.item_list_app, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();




            ScanResult item = getItem(position);
            int level = item.level;
            if(level>Parameter.getSignalGood()){
                holder.wifi_icon.setImageResource(R.drawable.wifi_green);
            }else if(level<= Parameter.getSignalGood() && level>Parameter.getSignalFair()){
                holder.wifi_icon.setImageResource(R.drawable.wifi_yellow);
            }else {
                holder.wifi_icon.setImageResource(R.drawable.wifi_red);
            }
            holder.wifi_name.setText(item.SSID + " (" + item.BSSID+")");

            holder.wifi_name.setTextColor(Color.GREEN);

            return convertView;
        }

        class ViewHolder {
            ImageView wifi_icon;
            TextView wifi_name;

            public ViewHolder(View view) {
                wifi_icon = (ImageView) view.findViewById(R.id.wifi_level);
                wifi_name = (TextView) view.findViewById(R.id.wifi_name);
                view.setTag(this);
            }
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // First Menu Button
        menu.add("Chart")
                .setOnMenuItemClickListener(this.ChartButtonClickListener)
                .setIcon(R.drawable.chart_icon) // Set the menu icon
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        // Second Menu Button
        menu.add("Refresh")
                .setOnMenuItemClickListener(this.RefreshButtonClickListener)
                .setIcon(R.drawable.refresh_icon) // Set the menu icon
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        // Third Menu Button
        menu.add("Settings")
                .setOnMenuItemClickListener(this.SettingsButtonClickListener)
                .setIcon(R.drawable.icon_small_settings)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return super.onCreateOptionsMenu(menu);
    }

    MenuItem.OnMenuItemClickListener ChartButtonClickListener = new MenuItem.OnMenuItemClickListener() {

        public boolean onMenuItemClick(MenuItem item) {


            Intent intent = new Intent(activity, StrengthActivity.class);
            startActivity(intent);

            return false;
        }
    };

    // Capture second menu button click
    MenuItem.OnMenuItemClickListener RefreshButtonClickListener = new MenuItem.OnMenuItemClickListener() {

        public boolean onMenuItemClick(MenuItem item) {


            wifilist = wifiManager.getScanResults();
            wifiListAdapter = new WifiListAdapter();
            mListView.setAdapter(wifiListAdapter);
            Toast.makeText(Wifi_Lists_Activity.this, "Refresh finished.", Toast.LENGTH_SHORT).show();
            return false;
        }
    };

    // Capture third menu button click
    MenuItem.OnMenuItemClickListener SettingsButtonClickListener = new MenuItem.OnMenuItemClickListener() {

        public boolean onMenuItemClick(MenuItem item) {

            Intent intent = new Intent(activity, SettingsActivity.class);
            startActivity(intent);
            return false;
        }
    };

}
