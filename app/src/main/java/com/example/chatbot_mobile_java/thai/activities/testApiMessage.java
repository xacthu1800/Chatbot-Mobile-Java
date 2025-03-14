package com.example.chatbot_mobile_java.thai.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.chatbot_mobile_java.R;
import com.example.chatbot_mobile_java.thai.adapters.RecyclerViewAdapter;
import com.example.chatbot_mobile_java.thai.data.Api;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class testApiMessage extends AppCompatActivity {

    List<Api> apiList = new ArrayList<Api>();

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api);

        fillApiList();

        recyclerView = findViewById(R.id.lv_apiList);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new RecyclerViewAdapter(apiList, this);
        recyclerView.setAdapter(mAdapter);

    }

    private void fillApiList() {
        Api a0 = new Api(0, "Gemini 2.0 Pro Experimental","Nổi bật với khả năng xử lý thông tin phức tạp, cửa sổ ngữ cảnh rộng, đặc biệt tối ưu cho lập trình và nghiên cứu.", "https://play-lh.googleusercontent.com/Pkwn0AbykyjSuCdSYCbq0dvOqHP-YXcbBLTZ8AOUZhvnRuhUnZ2aJrw_YCf6kVMcZ4PM=w480-h960-rw");
        Api a1 = new Api(1, "Gemini 2.0 Flash Thinking Experimental","Tập trung vào tốc độ xử lý và khả năng suy luận nhanh, tối ưu hóa chi phí.", "https://play-lh.googleusercontent.com/Pkwn0AbykyjSuCdSYCbq0dvOqHP-YXcbBLTZ8AOUZhvnRuhUnZ2aJrw_YCf6kVMcZ4PM=w480-h960-rw");
        Api a2 = new Api(2, "OpenAI GPT-4o-mini", "Là một phiên bản nhẹ hơn của GPT-4o, có tốc độ nhanh, tối ưu hóa cho hiệu suất cao với tài nguyên thấp.", "https://play-lh.googleusercontent.com/lmG9HlI0awHie0cyBieWXeNjpyXvHPwDBb8MNOVIyp0P8VEh95AiBHtUZSDVR3HLe3A=w480-h960-rw");
        Api a3 = new Api(3, "xAI Grok-2", "Grok-2 mạnh mẽ hơn với khả năng suy luận tốt, tạo hình ảnh ấn tượng và được cung cấp miễn phí.", "https://img.icons8.com/?size=512&id=W864KQKLKmWj&format=png");

        apiList.addAll(Arrays.asList(new Api[] {a0, a1, a2, a3}));


    }
}
