package com.xich.xichim.handlerAbstract;

import com.xich.xichim.model.entity.Room;

import java.util.List;

public interface IGetRoomListHandler {
    List<Room> getRoomList(String username);
}
