package com.example.chatbot_mobile_java.bin.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.example.chatbot_mobile_java.R;
import com.example.chatbot_mobile_java.bin.activities.MainChatPage;
import com.example.chatbot_mobile_java.bin.data.clientMessage;
import com.example.chatbot_mobile_java.bin.data.history_listChat;
import com.example.chatbot_mobile_java.bin.data.sql_list_chatMessage;

import java.util.List;
import java.util.Map;

public class history_chat_adapter extends RecyclerView.Adapter<history_chat_adapter.MyViewHolder> {

    List<Integer> conversationIdList;
    Map<Integer, sql_list_chatMessage> groupedMessageList;
    history_listChat chatList;

    public history_chat_adapter(
            history_listChat history_listChat,
            List<Integer> conversationIdList,
            Map<Integer, sql_list_chatMessage> groupedMessageList
    ) {
        this.chatList = history_listChat;
        this.conversationIdList = conversationIdList;
        this.groupedMessageList = groupedMessageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_history, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(history_chat_adapter.MyViewHolder holder, int position) {
        sql_list_chatMessage list;
        list = this.groupedMessageList.get(this.conversationIdList.get(position));
        if (list == null) {
            holder.ItemChatHistory.setText("No message");
            Log.d("errrr", "null");
        } else {
            Log.d("errrr", "not null");
        }

        holder.ItemChatHistory.setText(list.get_last_textMessage());
        holder.itemView.setOnClickListener(v -> {
            Context context = holder.itemView.getContext();
            Intent intent = new Intent(context, MainChatPage.class);

            sql_list_chatMessage.setIntent_conversationId(this.conversationIdList.get(position));
            sql_list_chatMessage.setIntent_listMessage(this.groupedMessageList.get(this.conversationIdList.get(position)).getListMessage());

            MainChatPage.setFirstChat(false);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return this.conversationIdList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ItemChatHistory;

        public MyViewHolder(View itemView) {
            super(itemView);
            ItemChatHistory = itemView.findViewById(R.id.tvItemChatHistory);
        }
    }
}
