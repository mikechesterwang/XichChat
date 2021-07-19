package com.xich.xichim.controller.json;

import com.xich.xichim.model.entity.Message;

import java.util.List;

public class HistoryMessage {
    private String usernameSender;
    private String usernameReceiver;
    private List<Message> history;

    public HistoryMessage(String usernameSender, String usernameReceiver, List<Message> history) {
        this.usernameSender = usernameSender;
        this.usernameReceiver = usernameReceiver;
        this.history = history;
    }

    public String getUsernameSender() {
        return usernameSender;
    }

    public void setUsernameSender(String usernameSender) {
        this.usernameSender = usernameSender;
    }

    public String getUsernameReceiver() {
        return usernameReceiver;
    }

    public void setUsernameReceiver(String usernameReceiver) {
        this.usernameReceiver = usernameReceiver;
    }

    public List<Message> getHistory() {
        return history;
    }

    public void setHistory(List<Message> history) {
        this.history = history;
    }
}
