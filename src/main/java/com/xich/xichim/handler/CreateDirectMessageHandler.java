package com.xich.xichim.handler;

import com.xich.xichim.handlerAbstract.ICreateDirectMessageHandler;
import com.xich.xichim.model.entity.Room;
import com.xich.xichim.model.mapper.RoomMapper;
import com.xich.xichim.utils.HashGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class CreateDirectMessageHandler implements ICreateDirectMessageHandler {
    private final PlatformTransactionManager transactionManager;
    private final RoomMapper roomMapper;

    public CreateDirectMessageHandler(PlatformTransactionManager transactionManager, RoomMapper roomMapper){
        this.transactionManager = transactionManager;
        this.roomMapper = roomMapper;
    }

    public int createDirectMessage(String usernameSender, String usernameReceiver) throws Exception{
        TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        Integer rtn = transactionTemplate.execute(status -> {
            Room room = new Room(HashGenerator.directMessageRoomNameHash(usernameSender, usernameReceiver));
            roomMapper.createRoom(room);
            int roomId = room.getId();
            roomMapper.joinRoom(usernameSender, roomId);
            roomMapper.joinRoom(usernameReceiver, roomId);
            return roomId;
        });
        if(rtn == null){
            throw new Exception("Failed to create room with " + usernameSender + " " + usernameReceiver);
        }
        return rtn;
    }
}
