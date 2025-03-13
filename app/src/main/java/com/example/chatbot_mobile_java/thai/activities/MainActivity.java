package com.example.chatbot_mobile_java.thai.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.chatbot_mobile_java.R;
import com.example.chatbot_mobile_java.bin.adapters.RecyclerViewAdapter;
import com.example.chatbot_mobile_java.bin.data.Api;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Api> apiList = new ArrayList<Api>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_constraint_layout);

        fillApiList();

        recyclerView = findViewById(R.id.lv_apiList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new RecyclerViewAdapter(apiList, this);
        recyclerView.setAdapter(mAdapter);

    }

    private void fillApiList() {
        Api a0 = new Api(0, "Gemini 2.0 Pro","Best for General", "https://play-lh.googleusercontent.com/Pkwn0AbykyjSuCdSYCbq0dvOqHP-YXcbBLTZ8AOUZhvnRuhUnZ2aJrw_YCf6kVMcZ4PM=w480-h960-rw");
        Api a1 = new Api(1, "Gemini 2.0 Flash","Best for General, Coding", "https://play-lh.googleusercontent.com/Pkwn0AbykyjSuCdSYCbq0dvOqHP-YXcbBLTZ8AOUZhvnRuhUnZ2aJrw_YCf6kVMcZ4PM=w480-h960-rw");

        apiList.addAll(Arrays.asList(new Api[] {a0, a1}));


    }
}