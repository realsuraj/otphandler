package com.genxhire.opt.handler;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoginPage extends AppCompatActivity {
    Button loginBtn;
    EditText inputEmail,inputPassword;
    String email, password, token;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);



        loginBtn = findViewById(R.id.btnSigin);
        inputPassword =  (EditText) findViewById(R.id.password_input);
        inputEmail = findViewById(R.id.email_input);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               checkLoginCredential();
//               Intent intent = new Intent(LoginPage.this, HomePage.class);
//               startActivity(intent);


            }
        });

    }

    private void checkLoginCredential() {

        email = String.valueOf(inputEmail.getText());
        password = String.valueOf(inputPassword.getText());

        String postUrl = "";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject postData = new JSONObject();
        try {
            postData.put("userID", email);
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("TA", "checkLoginCredential: " + postData.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, postUrl, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("TAG", "onResponse: " + response);
                    token = response.getString("token");

                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString("token", token);
                    myEdit.commit();
                    Toast.makeText(LoginPage.this, "success", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginPage.this, HomePage.class);
                    startActivity(intent);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int statusCode = error.networkResponse.statusCode;
                errorHandling(statusCode);
                Log.d("TAG", "Error: " + error.networkResponse.statusCode);
            }
        });
        requestQueue.add(jsonObjectRequest);





    }

    public void saveToken(){

    }


    public void errorHandling(int statusCode){
        switch(statusCode) {
            case 400:
                Toast.makeText(this, "Enter Username and password", Toast.LENGTH_SHORT).show();
                break;
            case 403:
                Toast.makeText(this, "Wrong Username or password", Toast.LENGTH_SHORT).show();
                break;
            case 401:
                Toast.makeText(this, "Wrong Username , password", Toast.LENGTH_SHORT).show();
                break;
            case 404:
                Toast.makeText(this, "No Data found", Toast.LENGTH_SHORT).show();
                break;
            case 409:
                Toast.makeText(this, " could not be processed because of conflict in the request", Toast.LENGTH_SHORT).show();
                break;
            default:
                Toast.makeText(this, "Unknown Error", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}