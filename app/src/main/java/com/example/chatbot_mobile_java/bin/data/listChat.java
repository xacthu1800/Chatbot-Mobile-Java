package com.example.chatbot_mobile_java.bin.data;

import java.util.ArrayList;
import java.util.List;

public class listChat {
    private List<chat> listChat;

    public listChat(List<chat> listChat) {
        this.listChat = new ArrayList<>();
    }

    public List<chat> getListChat() {
        return listChat;
    }

    public void setListChat(List<chat> listChat) {
        this.listChat = listChat;
    }

    public void addItemToListChat(chat newChat){
        this.listChat.add(newChat);
    }

    public chat getItematIndexInListChat(int index){
        return this.listChat.get(index);
    }
}
