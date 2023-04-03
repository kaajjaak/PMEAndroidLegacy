package com.akina.loginformtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.akina.loginformtest.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginPage extends AppCompatActivity {
    SharedPreferences sp;
    private String token = " ";
    private static final String TAG = "MyActivity";
    TextView username;
    TextView password;
    Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.usernameTv);
        password = findViewById(R.id.passwordTv);
        login = findViewById(R.id.loginBtn);
        sp = getSharedPreferences("token", Context.MODE_PRIVATE);

        // instantiate the RequestsQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://databasegip.herokuapp.com";


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Login(username.getText().toString(), password.getText().toString(), url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });}

    void Login(String Username, String Password, String url) throws JSONException {
        List<String> Tokens = new ArrayList<String>();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("username", Username);
        jsonObj.put("password", Password);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.POST,
                url + "/accounts/login",
                jsonObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    SharedPreferences.Editor editor = sp.edit();

                    editor.putString("token", response.getString("token"));
                    editor.putString("username", response.getString("username"));
                    editor.putBoolean("initialized", true);
                    //setContentView(R.layout.homepage);
                    editor.apply();

                    startActivity(new Intent(LoginPage.this, HomePage.class));

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