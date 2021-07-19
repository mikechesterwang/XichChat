package com.xich.xichim.handlerAbstract;

public interface IPersistentSendHandler {
    long send(String usernameSender, long timestamp, String content, int roomId) throws Exception;
}
