package com.xich.xichim.handler;

import com.xich.xichim.handlerAbstract.ILoadHistoryHandler;
import com.xich.xichim.model.entity.Message;
import com.xich.xichim.model.mapper.MessageMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoadHistoryHandler implements ILoadHistoryHandler {

    private final MessageMapper messageMapper;

    public LoadHistoryHandler(MessageMapper messageMapper){
        this.messageMapper = messageMapper;
    }

    public List<Message> load(int roomId, long timestamp, int count) throws Exception{
        return messageMapper.loadHistory(roomId, timestamp, count);
    }
}
