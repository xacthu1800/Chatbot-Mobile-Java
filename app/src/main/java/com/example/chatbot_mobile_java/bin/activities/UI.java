package com.example.chatbot_mobile_java.bin.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.UiModeManager;
import android.content.res.Configuration;
import android.os.Bundle;
import com.example.chatbot_mobile_java.R;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Context;

public class UI extends AppCompatActivity {
    private ToggleButton btnDark, btnLight, btn_System;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean nightMODE, lightMODE;
    private Switch switcher;
    private EditText etApiKey;
    private ImageButton btnSubmitApiKey;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_theme_box);
        //getSupportActionBar().hide();
        btn_System = findViewById(R.id.btn_system);
        etApiKey = findViewById(R.id.etApiKey);
        btnSubmitApiKey = findViewById(R.id.btnSubmitApiKey);
        btnDark = findViewById(R.id.btn_dark);
        btnLight = findViewById(R.id.btn_light);

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
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

        btnSubmitApiKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newApiKey = etApiKey.getText().toString();
                if(newApiKey == ""){
                    return;
                }
                liveVoice.setApiKey(newApiKey);
                Toast.makeText(view.getContext(), "Đã đổi API key", Toast.LENGTH_SHORT).show();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(etApiKey.getWindowToken(), 0);
                }
                etApiKey.clearFocus();
            }
        });

        btn_System.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();



//                switch (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK ) {
//                    case Configuration.UI_MODE_NIGHT_YES:
//                        Toast.makeText(view.getContext(), "dark theme", Toast.LENGTH_SHORT).show();
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                        break;
//                    case Configuration.UI_MODE_NIGHT_NO:
//                        Toast.makeText(view.getContext(), "light theme", Toast.LENGTH_SHORT).show();
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                        break;
//                }
//
//                editor.apply();


                UiModeManager uiModeManager = (UiModeManager) getSystemService(UI_MODE_SERVICE);
                boolean isLightTheme = (uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_NO);

                if (isLightTheme) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", false);
                    editor.apply();
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night", true);
                    editor.apply();
                }

            }
        });

    }
}