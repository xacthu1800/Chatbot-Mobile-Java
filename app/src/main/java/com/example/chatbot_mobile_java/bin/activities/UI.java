package com.example.chatbot_mobile_java.bin.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.chatbot_mobile_java.R;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Switch;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Context;

public class UI extends AppCompatActivity {
    private ToggleButton btnDark, btnLight;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean nightMODE, lightMODE;
    private Switch switcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_theme_box);
        //getSupportActionBar().hide();
        btnDark = findViewById(R.id.btn_dark);
        btnLight = findViewById(R.id.btn_light);
        sharedPreferences = peekAvailableContext().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMODE = sharedPreferences.getBoolean("night", false);
        if(nightMODE){
            btnDark.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        btnDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nightMODE){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", true);
                    editor.apply();
                }
            }
        });
        btnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(nightMODE) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);
                    editor.apply();
                }
            }
        });
    }
}