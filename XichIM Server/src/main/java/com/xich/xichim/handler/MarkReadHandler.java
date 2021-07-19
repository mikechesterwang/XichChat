package com.xich.xichim.handler;

import com.xich.xichim.handlerAbstract.IMarkReadHandler;
import com.xich.xichim.model.mapper.RoomMapper;
import org.springframework.stereotype.Service;

@Service
public class MarkReadHandler implements IMarkReadHandler {

    private final RoomMapper roomMapper;

    public MarkReadHandler(RoomMapper roomMapper){
        this.roomMapper = roomMapper;
    }

    @Override
    public void markReadRoom(int timestamp, int roomId, String username) {
        roomMapper.markReadRoom(timestamp, roomId, username);
    }
}
