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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegisterPage extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    TextView regUsername;
    TextView regPassword;
    TextView regPassword2;
    Button regRegister;
    Button regCancel;
    SharedPreferences sp;
    private String token = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        regUsername = findViewById(R.id.tvUsername);
        regPassword = findViewById(R.id.tvPass1);
        regPassword2 = findViewById(R.id.tvPass2);
        regRegister = findViewById(R.id.btRegister);
        sp = getSharedPreferences("token", Context.MODE_PRIVATE);
        regCancel = findViewById(R.id.btCancel);

        regCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterPage.this, MainActivity.class));
            }
        });
        // instantiate the RequestsQueue
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://databasegip.herokuapp.com";


        regRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (regPassword.getText().toString().equals(regPassword2.getText().toString())) {
                        Register(regUsername.getText().toString(), regPassword.getText().toString(), url);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void Register(String Username, String Password, String url) throws JSONException {
        List<String> Tokens = new ArrayList<String>();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("username", Username);
        jsonObj.put("password", Password);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.POST,
                url + "/accounts",
                jsonObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (regUsername.getText().toString().equals(response.getString("username"))) {
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("token", response.getString("token"));
                        editor.putString("username", response.getString("username"));
                        editor.putBoolean("initialized", true);
                        //setContentView(R.layout.homepage);
                        editor.apply();
                        startActivity(new Intent(RegisterPage.this, HomePage.class));
                    }
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
