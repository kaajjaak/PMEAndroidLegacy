package com.akina.loginformtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button loginPage;
    Button registerPage;
    SharedPreferences sp;
    private static final String TAG = "MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().hide();

        loginPage = (Button) findViewById(R.id.btnLogin);
        registerPage = (Button) findViewById(R.id.btnRegister);

        sp = getSharedPreferences("token", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor;
        if(sp.contains("initialized")) {
            try {
                Login();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginPage.class));
            }
        });
        registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterPage.class));
            }
        });
    }
    void Login() throws JSONException {
        String url ="https://databasegip.herokuapp.com";
        List<String> Tokens = new ArrayList<String>();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("token", sp.getString("token", ""));
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.POST,
                url + "/pages/homepage",
                jsonObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    //SharedPreferences.Editor editor = sp.edit();
                    //setContentView(R.layout.homepage);
                    Log.w(TAG, "response username: " + response.getString("username") + " stored username:" + sp.getString("username" , ""));
                    if(response.getString("username").equals(sp.getString("username", "")))
                        startActivity(new Intent(MainActivity.this, HomePage.class));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                Log.d(TAG, arg0.toString());
            }
        });
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(jsObjRequest);
    }
}