package com.akina.loginformtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class SettingsPage extends AppCompatActivity {

    private DrawerLayout dl;
    private ActionBarDrawerToggle adbt;
    Button changepass;
    Button logout;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        dl = (DrawerLayout)findViewById(R.id.settings_dl);
        adbt = new ActionBarDrawerToggle(this,dl,R.string.Open,R.string.Close);
        adbt.setDrawerIndicatorEnabled(true);
        changepass = findViewById(R.id.pswChange);
        logout = findViewById(R.id.btnLogout);
        dl.addDrawerListener(adbt);
        adbt.syncState();
        sp = getSharedPreferences("token", Context.MODE_PRIVATE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(SettingsPage.this, MainActivity.class));
            }
        });
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsPage.this, ChangePasswordPage.class));
            }
        });

        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.myprofile:
                        Toast.makeText(SettingsPage.this, "MyProfile", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.settings:
                        //startActivity(new Intent(SettingsPage.this, HomePage.class));
                        break;
                    case R.id.applications:
                        Toast.makeText(SettingsPage.this, "Edit Profile", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return adbt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

}