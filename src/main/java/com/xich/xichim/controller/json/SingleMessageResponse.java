package com.xich.xichim.controller.json;

import com.xich.xichim.model.entity.Message;

public class SingleMessageResponse {
    private String type;
    private Message message;

    public SingleMessageResponse(Message message){
        this.type = "message";
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
