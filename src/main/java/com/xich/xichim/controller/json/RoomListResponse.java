package com.xich.xichim.controller.json;

import com.xich.xichim.model.entity.Room;

import java.util.List;

public class RoomListResponse {
    private List<Room> list;

    public RoomListResponse(List<Room> list){
        this.list = list;
    }

    public List<Room> getList() {
        return list;
    }

    public void setList(List<Room> list) {
        this.list = list;
    }
}
