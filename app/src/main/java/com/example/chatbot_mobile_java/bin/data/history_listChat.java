package com.example.chatbot_mobile_java.bin.data;

import java.util.ArrayList;
import java.util.List;

public class history_listChat {
    private List<history_chat> listHistoryChat;

    public history_listChat(List<history_chat> listHistoryChat) {
        this.listHistoryChat = new ArrayList<>();
    }

    public List<history_chat> getListChat() {
        return listHistoryChat;
    }

    public void setListChat(List<history_chat> listHistoryChat) {
        this.listHistoryChat = listHistoryChat;
    }

    public void addItemToListChat(history_chat newHistoryChat){
        this.listHistoryChat.add(newHistoryChat);
    }

    public history_chat getItematIndexInListChat(int index){
        return this.listHistoryChat.get(index);
    }
}
