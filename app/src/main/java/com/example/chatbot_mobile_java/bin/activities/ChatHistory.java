package com.example.chatbot_mobile_java.bin.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatbot_mobile_java.R;
import com.example.chatbot_mobile_java.bin.adapters.history_chat_adapter;
import com.example.chatbot_mobile_java.bin.data.history_chat;
import com.example.chatbot_mobile_java.bin.data.history_listChat;

import java.util.ArrayList;

public class ChatHistory extends AppCompatActivity  {
    RecyclerView rvHistoryChat;
    RecyclerView.Adapter historyChatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);
        getSupportActionBar().hide();

        history_listChat chatList = new history_listChat(new ArrayList<>());
        chatList.addItemToListChat(new history_chat("skibidi toilet"));
        chatList.addItemToListChat(new history_chat("bin skibidi"));
        chatList.addItemToListChat(new history_chat("phúc skibidi"));
        chatList.addItemToListChat(new history_chat("thái skibidi"));
        chatList.addItemToListChat(new history_chat("quân skibidi"));
        Log.d("listChat", chatList.getItematIndexInListChat(0).getChatText().toString());

        rvHistoryChat = findViewById(R.id.rvHistoryChat);
        rvHistoryChat.setLayoutManager(new LinearLayoutManager(this));
        historyChatAdapter = new history_chat_adapter(chatList);
        rvHistoryChat.setAdapter(historyChatAdapter);

    }
}
