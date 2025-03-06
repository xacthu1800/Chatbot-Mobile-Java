package com.example.chatbot_mobile_java.bin.adapters;

import android.util.Log;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.example.chatbot_mobile_java.R;
import com.example.chatbot_mobile_java.bin.data.chat;
import com.example.chatbot_mobile_java.bin.data.listChat;

import java.util.List;

public class history_chat_adapter extends RecyclerView.Adapter<history_chat_adapter.MyViewHolder> {

    listChat chatList;

    public history_chat_adapter(listChat listChat) {
        this.chatList = listChat;
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_history, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        Log.d("onCreateViewHolder", "run");
        return holder;
    }

    @Override
    public void onBindViewHolder(history_chat_adapter.MyViewHolder holder, int position) {

        holder.ItemChatHistory.setText(chatList.getItematIndexInListChat(position).getChatText()) ;
        Log.d("onbindView holder", "run");
    }

    @Override
    public int getItemCount() {
        Log.d("getItemCound", "run");
        return this.chatList.getListChat().size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView ItemChatHistory;

        public MyViewHolder(View itemView){
            super(itemView);
            ItemChatHistory = itemView.findViewById(R.id.tvItemChatHistory);
            Log.d("MyviewHolder", "run");
        }
    }
}

//public class history_chat_adapter extends RecyclerView.Adapter<history_chat_adapter.MyViewHolder> {
//
//    @NonNull
//    @Override
//    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        // Implement this method
//        return null;
//    }
//
//    // You'll also need to implement the other required methods:
//    @Override
//    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        // Implement this method
//    }
//
//    @Override
//    public int getItemCount() {
//        // Implement this method
//        return 0;
//    }
//
//    // Define your ViewHolder as an inner class
//    public static class MyViewHolder extends RecyclerView.ViewHolder {
//        // Define your view references here
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//            // Initialize your views here
//        }
//    }
//}
