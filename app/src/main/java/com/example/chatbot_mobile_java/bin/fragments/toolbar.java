package com.example.chatbot_mobile_java.bin.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.chatbot_mobile_java.R;
import com.example.chatbot_mobile_java.bin.activities.ChatHistory;
import com.example.chatbot_mobile_java.bin.activities.MainChatPage;


public class toolbar extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_toolbar, container, false);
        View view = inflater.inflate(R.layout.fragment_toolbar, container, false);


        ImageButton btnOpenChatHistory = view.findViewById(R.id.imgBtnToolbar);

        btnOpenChatHistory.setOnClickListener(v -> {
            Context context = getContext();
            if (context != null) {
                Intent intent = new Intent(context, ChatHistory.class);
                startActivity(intent);
            }
        });



        return view;

    }
}