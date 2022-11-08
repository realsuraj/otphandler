package com.genxhire.opt.handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.genxhire.opt.handler.recycler.AdapterHome;
import com.genxhire.opt.handler.recycler.RecyclerViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomePage extends AppCompatActivity {

    TextView isOtpActive;
    SwitchCompat btn;
    List<RecyclerViewModel> list;
    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);



        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        isOtpActive = findViewById(R.id.otp_handler_below_text);
        AdapterHome adapter = new AdapterHome(this,list);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomePage.this);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.smoothScrollToPosition(adapter.getItemCount());

        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        btn = findViewById(R.id.toggleBtn);

        boolean checkBtn = sharedPreferences.getBoolean("switch",false);

        if(checkBtn){
            btn.setChecked(true);
            textColorChangeOnStart();

        }
        else{
            btn.setChecked(false);
            textColorChangeOnStop();

        }
        RecyclerViewModel recyclerViewModel = new RecyclerViewModel();


         BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Get extra data included in the Intent
                String message = intent.getStringExtra("key");
                int color = intent.getIntExtra("color",0);
                Log.d("Home page", "onReceive: " + message);

                    list.add(new RecyclerViewModel(message,color));
                    adapter.notifyDataSetChanged();
                recyclerView.smoothScrollToPosition(adapter.getItemCount());

            }
        };

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMessageReceiver, new IntentFilter("intentKey"));

        btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    startService(new Intent(HomePage.this, SmsListener.class));
                    startService();
                    textColorChangeOnStart();

//                    logger.setMessage("1st log statement");



                } else {
                    stopService(new Intent(HomePage.this, SmsListener.class));
                    stopService();
                    textColorChangeOnStop();

                }
            }
        });



    }


    public void startService() {
        Intent serviceIntent = new Intent(this, notificationService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putBoolean("switch", true);
        myEdit.commit();


    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, notificationService.class);
        stopService(serviceIntent);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putBoolean("switch", false);
        myEdit.commit();
    }

    public void textColorChangeOnStart(){
        isOtpActive.setText("OTP Handler is Active ");
        isOtpActive.setTextColor(Color.GREEN);
    }

    public void textColorChangeOnStop(){
        isOtpActive.setText("OTP Handler is Inactive ");
        isOtpActive.setTextColor(Color.RED);
    }

    }