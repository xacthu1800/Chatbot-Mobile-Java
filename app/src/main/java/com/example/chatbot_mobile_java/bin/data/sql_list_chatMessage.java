package com.example.chatbot_mobile_java.bin.data;

import java.util.ArrayList;
import java.util.List;

public class sql_list_chatMessage {

    static List<sql_chatMessage> intent_listMessage = new ArrayList<>();
    static int intent_conversationId = 0;
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

    public int get_list_size(){
        return (int) this.listMessage.size();
    }

    public String get_last_textMessage(){
        return this.listMessage.get(listMessage.size()-1).getContent();
    }

    public static List<sql_chatMessage> getIntent_listMessage() {
        return intent_listMessage;
    }

    public static void setIntent_listMessage(List<sql_chatMessage> intent_listMessage) {
        sql_list_chatMessage.intent_listMessage = intent_listMessage;
    }

    public static int getIntent_conversationId() {
        return intent_conversationId;
    }

    public static void setIntent_conversationId(int intent_conversationId) {
        sql_list_chatMessage.intent_conversationId = intent_conversationId;
    }


}
