package com.example.chatbot_mobile_java.bin.activities;


import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
//import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;

import com.example.chatbot_mobile_java.R;
import com.example.chatbot_mobile_java.bin.adapters.ModelChoosing_Adapter;
import com.example.chatbot_mobile_java.bin.adapters.chat_adapter;
import com.example.chatbot_mobile_java.bin.api.ApiResponseCallback;
import com.example.chatbot_mobile_java.bin.api.api_controller;
import com.example.chatbot_mobile_java.bin.data.Api;
import com.example.chatbot_mobile_java.bin.data.chatMessage;
import com.example.chatbot_mobile_java.bin.data.clientMessage;
import com.example.chatbot_mobile_java.bin.data.sql_chatMessage;
import com.example.chatbot_mobile_java.bin.data.sql_list_chatMessage;
import com.example.chatbot_mobile_java.bin.database.myDatabaseHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainChatPage extends AppCompatActivity {
    private LinearLayout layoutOptions, layoutExpandOption,layoutExpandedModel, chooseModel;
    private ImageButton btnOptions, Micro, Enter;
    private Button btnChooseModel;
    private EditText etMessageInput;
    static boolean optionsVisible = false;
    static boolean optionsVisible_Model = false;
    private List<Api> apiList = new ArrayList<Api>();
    private List<chatMessage> messages;
    private RecyclerView listApiModel;
    private RecyclerView.Adapter listApiModel_Adapter;
    private myDatabaseHelper myDB;

    RecyclerView rvMessages;
    chat_adapter chatAdapter;

    private int conversationId;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat_page);
        getSupportActionBar().hide();
        conversationId = (int) (System.currentTimeMillis() / 1000);

        // khai báo giá trị các View
        etMessageInput = findViewById(R.id.etMessageInput);
        btnChooseModel = findViewById(R.id.btnChooseModel);
        btnOptions = findViewById(R.id.ibtnMoreOption);
        layoutExpandOption = findViewById(R.id.layoutExpandedOptions);
        layoutExpandedModel = findViewById(R.id.layoutExpandedModel);
        chooseModel = findViewById(R.id.chooseModel);
        Micro = findViewById(R.id.Micro);
        Enter = findViewById(R.id.Enter);
        ConstraintLayout rootLayout = findViewById(R.id.chat_toolBar);
        myDB =new myDatabaseHelper(MainChatPage.this);

        // tiền xử lý để hiện lịch sử chat hoặc empty chat
        if( sql_list_chatMessage.getIntent_conversationId() == 0 || sql_list_chatMessage.getIntent_listMessage().isEmpty() ){
            Log.d("get_conversationid", "conversation static not null");
            messages = new ArrayList<>();
        }else  {
            conversationId = sql_list_chatMessage.getIntent_conversationId();
            messages = new ArrayList<>();
            List<sql_chatMessage> sqlListChatMessage = sql_list_chatMessage.getIntent_listMessage();
            for (sql_chatMessage sqlMsg : sqlListChatMessage) {
                chatMessage chatMsg = new chatMessage(sqlMsg.getContent(), sqlMsg.isClient());
                messages.add(chatMsg);
            }
        }

        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    toggleOptionsVisibility();
                    return true;
                }
                return false;
            }
        });
        // kết thúc khai báo giá trị các View

        // thiết lập chức năng các nút
       btnOptions.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               toggleOptionsVisibility();
           }
       });

       btnChooseModel.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) { toggleModelsVisibility(); }
       });
        Enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { sendMessage();}
        });
       // kết thục thiết lập chức năng các nút


       // xử lý chat của recycle view
        chatAdapter = new chat_adapter(this, messages);
        rvMessages = findViewById(R.id.rvMessages);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        layoutManager.setReverseLayout(true); // Đảo ngược danh sách
        rvMessages.setLayoutManager(layoutManager);
        rvMessages.setAdapter(chatAdapter);
        // kết thúc xử lý chat của recycle view

        // Thái - chọn model
        fillApiList();
        listApiModel = findViewById(R.id.lv_apiList);
        listApiModel.setLayoutManager(new LinearLayoutManager(this));
        listApiModel_Adapter = new ModelChoosing_Adapter(apiList, this);
        listApiModel.setAdapter(listApiModel_Adapter);
        // end thái - chọn model

        rvMessages.scrollToPosition(messages.size() - 1);
    }

    // toggle more option
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
    // toggle choose model
    private void toggleModelsVisibility() {
        if (optionsVisible_Model == false) {
            layoutExpandedModel.setVisibility(View.VISIBLE);
            etMessageInput.setVisibility(View.GONE);
            btnOptions.setVisibility(View.GONE);
            Micro.setVisibility(View.GONE);
            Enter.setVisibility(View.GONE);
        } else {
           // chooseModel.setVisibility(View.GONE);
            layoutExpandedModel.setVisibility(View.GONE);
            etMessageInput.setVisibility(View.VISIBLE);
            btnOptions.setVisibility(View.VISIBLE);
            Micro.setVisibility(View.VISIBLE);
            Enter.setVisibility(View.VISIBLE);
        }
        optionsVisible_Model = !optionsVisible_Model;
    }
    // hàm đổ dữ liệu vào List-model
    private void fillApiList() {
        Api a0 = new Api(0, "Gemini 2.0 Pro Experimental","Nổi bật với khả năng xử lý thông tin phức tạp, cửa sổ ngữ cảnh rộng, đặc biệt tối ưu cho lập trình và nghiên cứu.", "https://play-lh.googleusercontent.com/Pkwn0AbykyjSuCdSYCbq0dvOqHP-YXcbBLTZ8AOUZhvnRuhUnZ2aJrw_YCf6kVMcZ4PM=w480-h960-rw");
        Api a1 = new Api(1, "Gemini 2.0 Flash Thinking Experimental","Tập trung vào tốc độ xử lý và khả năng suy luận nhanh, tối ưu hóa chi phí.", "https://play-lh.googleusercontent.com/Pkwn0AbykyjSuCdSYCbq0dvOqHP-YXcbBLTZ8AOUZhvnRuhUnZ2aJrw_YCf6kVMcZ4PM=w480-h960-rw");
        Api a2 = new Api(2, "OpenAI GPT-4o-mini", "Là một phiên bản nhẹ hơn của GPT-4o, có tốc độ nhanh, tối ưu hóa cho hiệu suất cao với tài nguyên thấp.", "https://play-lh.googleusercontent.com/lmG9HlI0awHie0cyBieWXeNjpyXvHPwDBb8MNOVIyp0P8VEh95AiBHtUZSDVR3HLe3A=w480-h960-rw");
        Api a3 = new Api(3, "Claude 3.7", "Claude là AI chatbot của Anthropic, nổi bật với khả năng suy luận tốt, tạo nội dung sáng tạo và đảm bảo an toàn trong phản hồi.", "https://logowik.com/content/uploads/images/claude4477.logowik.com.webp");

        apiList.addAll(Arrays.asList(new Api[] {a0, a1, a2, a3}));
    }
    // hàm xử lý gửi tin nhắn
    private void sendMessage(){
        clientMessage.initialize_Text(etMessageInput.getText().toString());

        String clientInput = etMessageInput.getText().toString();
        String modelApi = clientMessage.get_Type();

        if (clientInput.isEmpty() && modelApi.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nội dung và chọn mô hình", Toast.LENGTH_SHORT).show();
            return;
        } else if (clientInput.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nội dung", Toast.LENGTH_SHORT).show();
            return;
        } else if (modelApi.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn mô hình", Toast.LENGTH_SHORT).show();
            return;
        }

        addMessage(new chatMessage(clientInput, true));
        myDB.addChatMessage(conversationId, clientInput, true, get_timestamp());

        api_controller.resolveApiMessage(new clientMessage(), new ApiResponseCallback() {
            @Override
            public void onSuccess(String response) {
                runOnUiThread(()->{
                    addMessage(new chatMessage(response, false));
                    myDatabaseHelper db = new myDatabaseHelper(MainChatPage.this);
                    db.addChatMessage(conversationId, response, false, get_timestamp());
                    Log.d("AI response: ", response);
                });
            }

            @Override
            public void onError(String error) {
                runOnUiThread(()->{
                    addMessage(new chatMessage("Error: " + error, false));
                    myDatabaseHelper db = new myDatabaseHelper(MainChatPage.this);
                    db.addChatMessage(conversationId, "Error: " + error, false, get_timestamp());
                    Log.e("AI error: ", error);
                });
            }
        });

//        Log.d("AI type: ", clientMessage.get_Type());
//        Log.d("send message: ", clientMessage.get_Text());
        etMessageInput.setText("");
        clientMessage.initialize_Text("");
        return ;
    }
    private void addMessage(chatMessage message){
        chatAdapter.addMessage(message);
        rvMessages.scrollToPosition(chatAdapter.getItemCount()-1);
    }

    private int get_timestamp(){
        return (int) (System.currentTimeMillis() / 1000);
    }

    public static void setModelToggleState(){
        optionsVisible_Model = !optionsVisible_Model;
    }

}




