package com.example.chatbot_mobile_java.bin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatbot_mobile_java.R;
import com.example.chatbot_mobile_java.bin.data.chatMessage;
import com.google.android.material.transition.MaterialElevationScale;

import java.util.List;

public class chat_adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_CLIENT = 1;
    private static final int VIEW_TYPE_APP = 2;

    private List<chatMessage> messages ;
    private Context context;

    public chat_adapter(Context context, List<chatMessage> messages){
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position){
        chatMessage message = messages.get(position);
        if(message.isCient()){
            return VIEW_TYPE_CLIENT ;
        } else return VIEW_TYPE_APP;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_CLIENT){
            View view = LayoutInflater.from(context).inflate(R.layout.item_client_chat_message, parent,false);
            return new ClientViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.item_app_chat_message, parent,false);
            return new AppViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        chatMessage message = messages.get(position);
        if(holder.getItemViewType()==VIEW_TYPE_CLIENT){
            ClientViewHolder clientHolder = (ClientViewHolder) holder;
            clientHolder.tvClientChatMessage.setText(message.getContent());
        }else{
            AppViewHolder AppHolder = (AppViewHolder) holder;
            AppHolder.tvAppChatMessage.setText(message.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ClientViewHolder extends RecyclerView.ViewHolder{
        TextView tvClientChatMessage;
        public ClientViewHolder(View itemView){
            super(itemView);
            tvClientChatMessage = itemView.findViewById(R.id.tvClientChatMessage);
        }
    }

    class AppViewHolder extends RecyclerView.ViewHolder{
        TextView tvAppChatMessage;
        public AppViewHolder(View itemView){
            super(itemView);
            tvAppChatMessage = itemView.findViewById(R.id.tvAppChatMessage);
        }
    }

}
