package com.xich.xichim.handlerAbstract;

import java.util.List;

public interface ILoginHandler {
    void login(String username, String password) throws Exception;

    List<Integer> getRoomList(String username) throws Exception;
}
