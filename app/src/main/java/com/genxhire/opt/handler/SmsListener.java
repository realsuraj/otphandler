package com.genxhire.opt.handler;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SmsListener extends Service
{

    private final String TAG = this.getClass().getSimpleName();
    private SMSReceiver smsReceiver;
    private IntentFilter mIntentFilter;
    private ArrayList<PortalData> portalDataModel = new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        getPortal();
        HomePage homePage= new HomePage();
        Log.i(TAG, "Communicator started");
        smsReceiver = new SMSReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        mIntentFilter.setPriority(2147483647);
        registerReceiver(smsReceiver, mIntentFilter);
        Intent intent = new Intent("android.provider.Telephony.SMS_RECEIVED");
        List<ResolveInfo> infos = getPackageManager().queryBroadcastReceivers(intent, 0);
        for (ResolveInfo info : infos) {
            Log.i(TAG, "Receiver name:" + info.activityInfo.name + "; priority=" + info.priority);
        }
    }
    @Override
    public void onDestroy() {
        registerReceiver(smsReceiver, mIntentFilter);
        // Unregister the SMS receiver
        super.onDestroy();
        unregisterReceiver(smsReceiver);
    }
    @Override

    public IBinder onBind(Intent arg0) {
        return null;
    }
    private class SMSReceiver extends BroadcastReceiver {
        private final String TAG = this.getClass().getSimpleName();

        @Override
        public void onReceive(Context context, Intent intent)
        {
            Bundle extras = intent.getExtras();

            String strMessage = "";

            if ( extras != null )
            {
                Object[] smsextras = (Object[]) extras.get( "pdus" );

                for ( int i = 0; i < smsextras.length; i++ )
                {
                    SmsMessage smsmsg = SmsMessage.createFromPdu((byte[])smsextras[i]);

                    String strMsgBody = smsmsg.getMessageBody().toString();
                    String strMsgSrc = smsmsg.getOriginatingAddress();
                    strMessage += "SMS from " + strMsgSrc + " : " + strMsgBody;
                    if(strMsgSrc.equalsIgnoreCase("SHINEM")){
                        Toast.makeText(context, "Shine " + strMessage, Toast.LENGTH_SHORT).show();
                        forwardShineOTP(strMsgBody);
                    }
                    else if(strMsgSrc.equalsIgnoreCase("NAUKRI")){
                        Toast.makeText(context, "Naukri " + strMessage, Toast.LENGTH_SHORT).show();
                        forwardNaukriOTP(strMsgBody);
                    }
                    else{
                        Toast.makeText(context, "Others" + strMessage, Toast.LENGTH_SHORT).show();
                        forwardNaukriOTP(strMsgBody);

                    }
                }
            }
        }
    }
    public void getAccountId(String otp, String accountID){
        String uniqueId = null;
        for(int i = 0;i< portalDataModel.size() ; i++){
           if((portalDataModel.get(i).getPortalAccountId()).equalsIgnoreCase(accountID) ){
               Log.d(TAG, "getAccountId: Good" + accountID + " " + portalDataModel.get(i).getPortalId());
               uniqueId =  portalDataModel.get(i).getPortalId();
               if(otp.equalsIgnoreCase(portalDataModel.get(i).getOtp())){
                   Log.d(TAG, "Otp: Duplicated");
                   break;
               }
               else{

                   sendData(otp, uniqueId);
                   break;
               }

           }
        }
    }

    public void sendData(String Otp, String UniqueId) {
        Log.d(TAG, "sendData " + Otp + UniqueId);
        String postUrl = "update_otp";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        HashMap<String, Object> params = new HashMap<>();
        params.put("portalId", UniqueId);
        params.put("otp", Otp);
//        try {
//            postData.put("portalId", UniqueId);
//            postData.put("otp", Otp);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        Log.d(TAG, "postData: "+ postData);
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.PUT, postUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String name = response.toString();
                Log.d(TAG, "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("TAG", "Error: " + error.getMessage());
            }
        }){
            public byte[] getBody() {
                return new JSONObject(params).toString().getBytes();
            }
            public String getBodyContentType() {
                return "application/json";
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public void getPortal() {
        String postUrl = "portal";
        Log.d(TAG, "getPortal: fetching portal " );
        RequestQueue queue = Volley.newRequestQueue(SmsListener.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, postUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, "onResponse: "+ response);
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject responseObj = response.getJSONObject(i);
                        String id = responseObj.getString("id");
                        String accountId = responseObj.getString("portalAccountId");
                        String otp = responseObj.getString("otp");
                        portalDataModel.add(new PortalData(id, accountId, otp));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
            }
        });
        queue.add(jsonArrayRequest);

    }

    public void forwardNaukriOTP(String message){
        String otp = message.substring(4,11);
        String accountId = message.substring(31,47);
        getAccountId(otp, accountId);
    }

    public void forwardShineOTP(String message){
        String otp = message.substring(7,13);
        String accountId = message.substring(7,13);
      getAccountId(otp, accountId);
    }

}