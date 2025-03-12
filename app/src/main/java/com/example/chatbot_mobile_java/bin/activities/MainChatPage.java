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

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatbot_mobile_java.R;
import com.example.chatbot_mobile_java.bin.adapters.chat_adapter;
import com.example.chatbot_mobile_java.bin.data.chatMessage;

import java.util.ArrayList;
import java.util.List;


public class MainChatPage extends AppCompatActivity {
    private LinearLayout layoutOptions, layoutExpandOption, chooseModel;
    private ImageButton btnOptions, Micro, Enter;
    private boolean optionsVisible = false;
    //private RecyclerView rvMessage;

    RecyclerView rvMessages;
    RecyclerView.Adapter chatAdapter;

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


       // xử lý chat của recycle view

        List<chatMessage> messages = new ArrayList<>();
        messages.add(new chatMessage("hi i'm client", true));
        messages.add(new chatMessage("hi i'm App", false));
        messages.add(new chatMessage("this is second message from client", true));
        messages.add(new chatMessage("this is second message response from app", false));

        chatAdapter = new chat_adapter(this, messages);
        rvMessages = findViewById(R.id.rvMessages);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(chatAdapter);


        // kết thúc


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

