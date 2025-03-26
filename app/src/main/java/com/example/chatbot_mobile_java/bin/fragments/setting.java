package com.example.chatbot_mobile_java.bin.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;

import com.example.chatbot_mobile_java.R;
import com.example.chatbot_mobile_java.bin.activities.ChatHistory;
import com.example.chatbot_mobile_java.bin.activities.MainChatPage;


public class setting extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_toolbar, container, false);
        View view = inflater.inflate(R.layout.fragment_setting_toolbar, container, false);

        ImageButton imgBtnBack = view.findViewById(R.id.imgBtnBack);

        imgBtnBack.setOnClickListener(v -> {
            Context context = getContext();
            if (context != null) {
                Intent intent = new Intent(context, MainChatPage.class);
                startActivity(intent);
            }
        });


        return view;

    }
}