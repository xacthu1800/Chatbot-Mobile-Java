package com.example.chatbot_mobile_java.bin.activities;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatbot_mobile_java.R;
import com.example.chatbot_mobile_java.bin.adapters.history_chat_adapter;
import com.example.chatbot_mobile_java.bin.data.history_chat;
import com.example.chatbot_mobile_java.bin.data.history_listChat;
import com.example.chatbot_mobile_java.bin.data.sql_chatMessage;
import com.example.chatbot_mobile_java.bin.data.sql_list_chatMessage;
import com.example.chatbot_mobile_java.bin.database.myDatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.http.Body;

public class ChatHistory extends AppCompatActivity  {
    RecyclerView rvHistoryChat;
    RecyclerView.Adapter historyChatAdapter;
    private myDatabaseHelper myDB;
    private sql_list_chatMessage sqlListChat;
    private List<Integer> list_conversationid;

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

        sqlListChat = new sql_list_chatMessage();
        sqlListChat = get_listChatMessage();

        list_conversationid = get_list_conversationId(sqlListChat);
        Log.d("list_conversationId", list_conversationid.toString());

        Map<Integer, sql_list_chatMessage> groupedMessages = groupMessagesByConversation(sqlListChat, list_conversationid);

//        for (Map.Entry<Integer, sql_list_chatMessage> entry : groupedMessages.entrySet()) {
//            Log.d("ketqua","Conversation ID: " + entry.getKey());
//            for (sql_chatMessage msg : entry.getValue().getListMessage()) {
//                Log.d("ketqua","  - " + msg.getContent());
//            }
//        }

        rvHistoryChat = findViewById(R.id.rvHistoryChat);
        rvHistoryChat.setLayoutManager(new LinearLayoutManager(this));
        historyChatAdapter = new history_chat_adapter(chatList, list_conversationid, groupedMessages);
        rvHistoryChat.setAdapter(historyChatAdapter);


    }


    private sql_list_chatMessage get_listChatMessage (){
        sql_list_chatMessage sqlListChatMessage = new sql_list_chatMessage();
        myDB =new myDatabaseHelper(ChatHistory.this);

        Cursor cursor = myDB.readAllData();
        if(cursor != null && cursor.moveToFirst()){
            do{
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("_id"));
                @SuppressLint("Range") int conversationId = cursor.getInt(cursor.getColumnIndex("conversation_id"));
                @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
                @SuppressLint("Range") boolean isClient = cursor.getInt(cursor.getColumnIndex("is_client")) == 1;
                @SuppressLint("Range") int timestamp = cursor.getInt(cursor.getColumnIndex("timestamp"));

                if (content!= null ){
                    sqlListChatMessage.add(new sql_chatMessage(id,conversationId, content, isClient, timestamp));
                }

            } while(cursor.moveToNext());
            cursor.close();
        }
        return sqlListChatMessage;
    }

    private List<Integer> get_list_conversationId(sql_list_chatMessage list){
        List<Integer> listConId = new ArrayList<>();
        for(sql_chatMessage message : list.getList()){
            if(!listConId.contains(message.getConversationId())){
                listConId.add(message.getConversationId());
            }
        }
        return listConId;
    }

    private sql_list_chatMessage get_list_sorted_by_conversationId(sql_list_chatMessage list,int consId){
        sql_list_chatMessage sortedList = new sql_list_chatMessage();

        for(sql_chatMessage message: list.getListMessage()){
            if(message.getConversationId() == consId){
                sortedList.add(message);
            }
        }

        return sortedList;
    }

    private Map<Integer, sql_list_chatMessage> groupMessagesByConversation(
            sql_list_chatMessage list,
            List<Integer> conversationIds
    ){
        Map<Integer, sql_list_chatMessage> result = new HashMap<>();
        for (Integer convId : conversationIds) {
            result.put(convId, new sql_list_chatMessage());
        }
        // Duyệt qua tất cả tin nhắn và thêm vào danh sách tương ứng
        for (sql_chatMessage message : list.getListMessage()) {
            int convId = message.getConversationId();
            if (result.containsKey(convId)) { // Chỉ thêm nếu convId có trong danh sách
                result.get(convId).add(message);
            }
        }
        return result;
    }

}

//    List<sql_chatMessage> conv1Messages = groupedMessages.get(1);
//        for (sql_chatMessage msg : conv1Messages) {
//        System.out.println(msg.getContent());
//        }