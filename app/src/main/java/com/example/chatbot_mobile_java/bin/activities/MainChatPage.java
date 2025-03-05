package com.example.chatbot_mobile_java.bin.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chatbot_mobile_java.R;



public class MainChatPage extends AppCompatActivity {
    private LinearLayout layoutOptions, layoutExpandOption;
    private ImageButton btnOptions;
    private boolean optionsVisible = false;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat_page);
        getSupportActionBar().hide();


       btnOptions = findViewById(R.id.ibtnMoreOption);
       layoutExpandOption = findViewById(R.id.layoutExpandedOptions);

       btnOptions.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               toggleOptionsVisibility();
           }
       });
    }

    private void toggleOptionsVisibility() {
        if (optionsVisible == false) {
            layoutExpandOption.setVisibility(View.VISIBLE);
        } else {
            layoutExpandOption.setVisibility(View.GONE);
        }
        optionsVisible = !optionsVisible;
    }
}
