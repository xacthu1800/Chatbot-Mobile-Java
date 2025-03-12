package com.example.chatbot_mobile_java.bin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.chatbot_mobile_java.R;
import com.example.chatbot_mobile_java.bin.activities.MainChatPage;

public class chat_history_toolbar extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_history_toolbar, container, false);
        ImageButton btnCloseChatHistory = view.findViewById(R.id.imgBtnBack);
        btnCloseChatHistory.setOnClickListener(v -> {
            Context context = getContext();
            if (context != null) {
                Intent intent = new Intent(context, MainChatPage.class);
                startActivity(intent);
            }
        });
        return view;
    }
}