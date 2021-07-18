package com.xich.xichim.handler;

import com.xich.xichim.handlerAbstract.IPersistentSendHandler;
import com.xich.xichim.model.entity.Message;
import com.xich.xichim.model.mapper.MessageMapper;
import org.springframework.stereotype.Service;

@Service
public class PersistentSendHandler implements IPersistentSendHandler {

    private final MessageMapper messageMapper;

    public PersistentSendHandler(MessageMapper messageMapper){
        this.messageMapper = messageMapper;
    }

    public long send(String usernameSender, long timestamp, String content, int roomId) throws Exception{
        Message message = new Message(content, usernameSender, roomId, timestamp);
        messageMapper.insertNewMessage(message);
        return message.getId();
    }
}
