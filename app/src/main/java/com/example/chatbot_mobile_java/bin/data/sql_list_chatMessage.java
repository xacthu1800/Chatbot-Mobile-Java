package com.example.chatbot_mobile_java.bin.data;

import java.util.ArrayList;
import java.util.List;

public class sql_list_chatMessage {
    private List<sql_chatMessage> listMessage;
    public sql_list_chatMessage(){
        this.listMessage = new ArrayList<>();
    }

    public List<sql_chatMessage> getList(){
        return this.listMessage;
    }

    public void add(sql_chatMessage message){
        this.listMessage.add(message);
    }

    public sql_chatMessage get(int index){
        return this.listMessage.get(index);
    }

    public List<sql_chatMessage> getListMessage() {
        return listMessage;
    }

    public void setListMessage(List<sql_chatMessage> listMessage) {
        this.listMessage = listMessage;
    }
}
