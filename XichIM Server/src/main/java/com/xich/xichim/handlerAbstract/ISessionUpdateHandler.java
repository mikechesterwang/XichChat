package com.xich.xichim.handlerAbstract;

public interface ISessionUpdateHandler {
    void updateSession(String username, int roomId, long idLastMessage);
}
