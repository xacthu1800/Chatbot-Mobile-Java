package com.example.chatbot_mobile_java.bin.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatbot_mobile_java.R;
import com.example.chatbot_mobile_java.bin.adapters.history_chat_adapter;
import com.example.chatbot_mobile_java.bin.data.chat;
import com.example.chatbot_mobile_java.bin.data.listChat;

import java.util.ArrayList;

public class ChatHistory extends AppCompatActivity  {
    RecyclerView rvHistoryChat;
    RecyclerView.Adapter historyChatAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_history);
        getSupportActionBar().hide();

        listChat chatList = new listChat(new ArrayList<>());
        chatList.addItemToListChat(new chat("skibidi toilet"));
        chatList.addItemToListChat(new chat("bin skibidi"));
        chatList.addItemToListChat(new chat("phúc skibidi"));
        chatList.addItemToListChat(new chat("thái skibidi"));
        chatList.addItemToListChat(new chat("quân skibidi"));
        Log.d("listChat", chatList.getItematIndexInListChat(0).getChatText().toString());

        rvHistoryChat = findViewById(R.id.rvHistoryChat);
        rvHistoryChat.setLayoutManager(new LinearLayoutManager(this));
        historyChatAdapter = new history_chat_adapter(chatList);
        rvHistoryChat.setAdapter(historyChatAdapter);

    }
}
