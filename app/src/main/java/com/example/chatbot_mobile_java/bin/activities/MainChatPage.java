package com.example.chatbot_mobile_java.bin.activities;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.sax.EndElementListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.transition.Transition;
import android.transition.TransitionManager;
//import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatbot_mobile_java.R;



public class MainChatPage extends AppCompatActivity {
    private LinearLayout layoutOptions, layoutExpandOption, chooseModel;
    private ImageButton btnOptions, Micro, Enter;
    private boolean optionsVisible = false;
    //private RecyclerView rvMessage;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat_page);
        getSupportActionBar().hide();


       btnOptions = findViewById(R.id.ibtnMoreOption);
       //rvMessage = findViewById(R.id.chat_toolBar);
       layoutExpandOption = findViewById(R.id.layoutExpandedOptions);
       chooseModel = findViewById(R.id.chooseModel);
       Micro = findViewById(R.id.Micro);
       Enter = findViewById(R.id.Enter);
       //View messageInputLayout = findViewById(R.id.layoutExpandedOptions);
       ConstraintLayout rootLayout = findViewById(R.id.chat_toolBar);

       rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    optionVisible();
                    return true;
                }
                return false;
            }
        });


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
            chooseModel.setVisibility(View.GONE);
            Micro.setVisibility(View.GONE);
            Enter.setVisibility(View.GONE);
        } else {
            layoutExpandOption.setVisibility(View.GONE);
            chooseModel.setVisibility(View.VISIBLE);
            Micro.setVisibility(View.VISIBLE);
            Enter.setVisibility(View.VISIBLE);
        }
        optionsVisible = !optionsVisible;
    }
    private void optionVisible(){
        layoutExpandOption.setVisibility(View.GONE);
        chooseModel.setVisibility(View.VISIBLE);
        Micro.setVisibility(View.VISIBLE);
        Enter.setVisibility(View.VISIBLE);
    }

}

