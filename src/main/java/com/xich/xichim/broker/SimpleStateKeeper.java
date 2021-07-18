package com.xich.xichim.broker;

import com.xich.xichim.model.mapper.RoomMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimpleStateKeeper {
    private final ConcurrentHashMap<WebSocketSession, String> session2username = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, WebSocketSession> username2session = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, HashSet<WebSocketSession>> roomChannel = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<WebSocketSession, HashSet<Integer>> roomList = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, String> blackList = new ConcurrentHashMap<>();

    public SimpleStateKeeper(){
        System.out.println("State keeper is initiated.");
    }

    @Autowired
    private RoomMapper roomMapper;

    /**
     * ban user temporarily, unban when restarting the server.
     * @param session
     * @param username
     * @param information
     */
    public void banUser(WebSocketSession session, String username, String information){

        System.out.println("Ban user " + username + " for " + information);
        blackList.put(username, information);
        logout(session);
    }

    public boolean isInRoom(WebSocketSession session, int roomId){
        return roomList.get(session).contains(roomId);
    }

    public boolean isLogin(WebSocketSession session){
        return session2username.containsKey(session);
    }

    public String getUsername(WebSocketSession session){
        return session2username.get(session);
    }

    public WebSocketSession getSession(String username){
        return username2session.get(username);
    }

    public void createRoom(int roomId, WebSocketSession session){
        roomList.get(session).add(roomId);
        subscribeToRoom(roomId, session);
    }

    public void unsubscribeRoom(int roomId, WebSocketSession session){
        roomChannel.get(roomId).remove(session);
    }

    public void broadcastRoom(WebSocketMessage<?> message, int roomId) throws IOException {
        for(WebSocketSession session : roomChannel.get(roomId)){
            session.sendMessage(message);
        }
    }

    public void login(String username, WebSocketSession session) throws Exception{

        if(blackList.containsKey(username)){
            throw new Exception("Server is busy.");
        }

        // init information mapper
        session2username.put(session, username);
        username2session.put(username, session);

        // create room list
        // and subscribe all rooms
        roomList.put(session, new HashSet<>());
        for(int roomId : roomMapper.getRoomIdList(username)){
            subscribeToRoom(roomId, session);
            roomList.get(session).add(roomId);
        }
    }

    public void logout(WebSocketSession session){
        // remove from information mapper
        String username = session2username.get(session);
        session2username.remove(session);
        username2session.remove(username);

        // unsubscribe all rooms
        for(int roomId : roomList.get(session)){
            unsubscribeRoom(roomId, session);
        }

        // remove room list
        roomList.remove(session);
    }

    public synchronized void subscribeToRoom(int roomId, WebSocketSession session){
        if(session == null){
            return;
        }
        if( ! roomChannel.containsKey(roomId)){
            roomChannel.put(roomId, new HashSet<>());
        }
        roomChannel.get(roomId).add(session);
    }
}
