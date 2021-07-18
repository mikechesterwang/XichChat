package com.xich.xichim.handler;

import com.xich.xichim.handlerAbstract.IGetRoomListHandler;
import com.xich.xichim.model.entity.Room;
import com.xich.xichim.model.mapper.RoomMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetRoomListHandler implements IGetRoomListHandler {

    private final RoomMapper roomMapper;

    public GetRoomListHandler(RoomMapper roomMapper){
        this.roomMapper = roomMapper;
    }

    @Override
    public List<Room> getRoomList(String username) {
        return roomMapper.getRoomFullList(username);
    }
}
