package com.genxhire.opt.handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.genxhire.opt.handler.databinding.ActivityHomePageBinding;
import com.genxhire.opt.handler.recycler.AdapterHome;
import com.genxhire.opt.handler.recycler.RecyclerViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomePage extends AppCompatActivity {

    Button btnStart, btnStop ;
    LinearLayout linearLayout;
    TextView textView;
    SwitchCompat btn;
    List<RecyclerViewModel> list;
    RecyclerViewModel recyclerViewModel;
    RecyclerView recyclerView;
    // Using ArrayList to store images data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        AdapterHome adapter = new AdapterHome(this,list);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomePage.this);
        recyclerView.setLayoutManager(linearLayoutManager);


        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        btn = findViewById(R.id.toggleBtn);

        RecyclerViewModel recyclerViewModel = new RecyclerViewModel();



        btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    startService(new Intent(HomePage.this, SmsListener.class));
                    Toast.makeText(HomePage.this, "start", Toast.LENGTH_SHORT).show();
                    startService();
//                    logger.setMessage("1st log statement");

                        RecyclerViewModel recyclerViewModel1 = new RecyclerViewModel();
                        recyclerViewModel1.setLogText("one");
                        list.add(recyclerViewModel1);
                        adapter.notifyDataSetChanged();



                } else {
                    stopService(new Intent(HomePage.this, SmsListener.class));
                    Toast.makeText(HomePage.this, "stop", Toast.LENGTH_SHORT).show();
                    stopService();
                }
            }
        });

    }

//    public void addTextbox(String Text){
//        final TextView[] myTextViews = new TextView[20]; // create an empty array;
//        final TextView rowTextView = new TextView(this);
//        rowTextView.setText(Text);
//        linearLayout.addView(rowTextView);
//    }
    public void startService() {
        Intent serviceIntent = new Intent(this, notificationService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, notificationService.class);
        stopService(serviceIntent);
    }

    private List<RecyclerViewModel> getData()
    {
        List<RecyclerViewModel> list = new ArrayList<>();
        list.add(new RecyclerViewModel("First Exam"));
        list.add(new RecyclerViewModel("Second Exam"));
        list.add(new RecyclerViewModel("My Test "));

        return list;
    }

    }