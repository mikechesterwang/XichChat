package com.xich.xichim.model.entity;

public class Message {
    private long id;
    private String content;
    private String usernameSender;
    private int roomId;
    private long timestamp;

    public Message(String content, String usernameSender, int roomId, long timestamp) {
        this.id = 0;
        this.content = content;
        this.usernameSender = usernameSender;
        this.roomId = roomId;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsernameSender() {
        return usernameSender;
    }

    public void setUsernameSender(String usernameSender) {
        this.usernameSender = usernameSender;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
