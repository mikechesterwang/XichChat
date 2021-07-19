package com.xich.xichim.handlerAbstract;

import com.xich.xichim.model.entity.Message;

import java.util.List;

public interface ILoadHistoryHandler {
    List<Message> load(int roomId, long timestamp, int count) throws Exception;
}
