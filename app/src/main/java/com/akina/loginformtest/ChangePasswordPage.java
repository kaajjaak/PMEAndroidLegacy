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
import java.util.List;

public class ChangePasswordPage extends AppCompatActivity {

    Button changeButton;
    Button cancelButton;
    TextView currpassword;
    TextView newpass1;
    TextView newpass2;
    String url = "https://databasegip.herokuapp.com";
    private static final String TAG = "MyActivity";
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);

        getSupportActionBar().hide();

        changeButton = (Button) findViewById(R.id.btnChangePass);
        cancelButton = (Button) findViewById(R.id.btnCancelPass);


        currpassword = findViewById(R.id.currPassword);
        newpass1 = findViewById(R.id.newPass1);
        newpass2 = findViewById(R.id.newPass2);
        sp = getSharedPreferences("token", Context.MODE_PRIVATE);

        RequestQueue queue = Volley.newRequestQueue(this);

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!ChangePass(currpassword.getText().toString(), newpass1.getText().toString(), newpass2.getText().toString()))
                        return;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChangePasswordPage.this, SettingsPage.class));
            }
        });
    }

    Boolean ChangePass(String currPass, String Newpass1, String Newpass2) throws JSONException {
        Boolean succeed = false;
        if (!Newpass1.equals(Newpass2)) {
            Toast.makeText(ChangePasswordPage.this, "Passwords are not equal!", Toast.LENGTH_SHORT).show();
            return false;
        }
        List<String> Tokens = new ArrayList<String>();
        JSONObject jsonObj = new JSONObject();
        Log.w(TAG, sp.getString("token", "ddd"));
        jsonObj.put("current_password", currPass);
        jsonObj.put("new_password", Newpass1);
        jsonObj.put("token", sp.getString("token" , ""));
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.POST,
                url + "/accounts/changepassword",
                jsonObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("status").equals("success")) {
                        Toast.makeText(ChangePasswordPage.this, "Success!", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(ChangePasswordPage.this, MainActivity.class));
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
        return true;
    }
}