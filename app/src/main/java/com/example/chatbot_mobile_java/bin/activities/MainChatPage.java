package com.example.chatbot_mobile_java.bin.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatbot_mobile_java.R;
import com.example.chatbot_mobile_java.bin.adapters.chat_adapter;
import com.example.chatbot_mobile_java.bin.data.chatMessage;

import java.util.ArrayList;
import java.util.List;


public class MainChatPage extends AppCompatActivity {
    private LinearLayout layoutOptions, layoutExpandOption;
    private ImageButton btnOptions;
    private boolean optionsVisible = false;

    RecyclerView rvMessages;
    RecyclerView.Adapter chatAdapter;

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
        } else {
            layoutExpandOption.setVisibility(View.GONE);
        }
        optionsVisible = !optionsVisible;
    }
}
