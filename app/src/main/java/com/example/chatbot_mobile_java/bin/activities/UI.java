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
        getSupportActionBar().hide();

        //getSupportActionBar().hide();
        etApiKey = findViewById(R.id.etApiKey);
        btnSubmitApiKey = findViewById(R.id.btnSubmitApiKey);
        btnDark = findViewById(R.id.btn_dark);
        btnLight = findViewById(R.id.btn_light);
        btn_System = findViewById(R.id.btn_system);

//        sharedPreferences = peekAvailableContext().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightMODE = sharedPreferences.getBoolean("night", false);

        // Use a single SharedPreferences for theme mode
        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);

        // Track theme mode with an enum or integer for more precise tracking
        int currentThemeMode = sharedPreferences.getInt("theme_mode", AppCompatDelegate.MODE_NIGHT_NO);

        // Set initial theme based on saved preference
        AppCompatDelegate.setDefaultNightMode(currentThemeMode);

        btnDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Night mode", Toast.LENGTH_SHORT).show();

                // Always set to night mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                // Save the current mode
                editor = sharedPreferences.edit();
                editor.putInt("theme_mode", AppCompatDelegate.MODE_NIGHT_YES);
                editor.apply();
            }
        });

        btnLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Light mode", Toast.LENGTH_SHORT).show();

                // Always set to light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

                // Save the current mode
                editor = sharedPreferences.edit();
                editor.putInt("theme_mode", AppCompatDelegate.MODE_NIGHT_NO);
                editor.apply();
            }
        });

        btn_System.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "System theme", Toast.LENGTH_SHORT).show();

                // Set to follow system mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

                // Save the current mode
                editor = sharedPreferences.edit();
                editor.putInt("theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                editor.apply();
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



    }
}