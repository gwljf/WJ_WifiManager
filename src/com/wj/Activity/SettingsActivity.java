package com.wj.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.wj.WJ_WifiManager.R;
import com.wj.parameters.Parameter;

/**
 * Created by wlf on 10/27/14.
 */
public class SettingsActivity extends Activity{


    SettingsActivity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_pramater);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        final EditText editText1 = (EditText) findViewById(R.id.editText);
        final EditText editText2 = (EditText) findViewById(R.id.editText2);

        Button sendButton = (Button) findViewById(R.id.button1);
        Button resetButton = (Button) findViewById(R.id.button2);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int parameter1;
                int parameter2;
                try {
                    parameter1 = Integer.valueOf(editText1.getText().toString());
                    parameter2 = Integer.valueOf(editText2.getText().toString());

                    System.out.println(parameter1+"  "+parameter2);

                    if(-100<=parameter1 && parameter1<=-40 && -100<=parameter1 && parameter1<=-40 && parameter1 > parameter2){
                        Parameter.setSignalGood(parameter1);
                        Parameter.setSignalFair(parameter2);
                        Toast.makeText(activity, "Sucessful", Toast.LENGTH_SHORT).show();
                        activity.onBackPressed();
                    } else {
                        Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e){
                    Toast.makeText(activity, "Exception Failed", Toast.LENGTH_SHORT).show();
                }



            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Parameter.setSignalGood(-60);
                Parameter.setSignalFair(-80);
                Toast.makeText(activity, "Reset finished.", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
