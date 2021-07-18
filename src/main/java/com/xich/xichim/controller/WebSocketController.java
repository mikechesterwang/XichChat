package com.xich.xichim.controller;

import com.alibaba.fastjson.JSON;
import com.xich.xichim.broker.SimpleStateKeeper;
import com.xich.xichim.controller.json.*;
import com.xich.xichim.handler.CreateDirectMessageHandler;
import com.xich.xichim.handlerAbstract.*;
import com.xich.xichim.model.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;

public class WebSocketController extends TextWebSocketHandler {

    public WebSocketController(){
        super();
        System.out.println("Socket Handler is initialized.");
    }

    @Autowired
    private SimpleStateKeeper stateKeeper;
    @Autowired
    private IRegisterHandler registerHandler;
    @Autowired
    private ILoginHandler loginHandler;
    @Autowired
    private IPersistentSendHandler persistentSendHandler;
    @Autowired
    private ILoadHistoryHandler loadHistoryHandler;
    @Autowired
    private IDeleteUserHandler deleteUserHandler;
    @Autowired
    private ICreateDirectMessageHandler createDirectMessageHandler;
    @Autowired
    private IGetRoomListHandler getRoomListHandler;

    private static void send(WebSocketSession session, String message) throws IOException {
        session.sendMessage(new TextMessage(message));
    }

    private static void sendError(String id, WebSocketSession session, String message) throws IOException{
        send(session, JSON.toJSONString(new Response(id, "status", new CommandStatusResponse(false, message))));
    }

    private static void sendSuccess(String id, WebSocketSession session) throws IOException{
        send(session, JSON.toJSONString(new Response(id, "status", new CommandStatusResponse(true, ""))));
    }

    private static void sendMessageError(String id, WebSocketSession session, String message) throws IOException{
        send(session, JSON.toJSONString(new Response(id, "message", new CommandStatusResponse(false, message))));
    }

    interface CommandActivator{
        void run() throws Exception;
    }

    public static void runCommand(String[] parameters, int expectedsNumParams, String id, WebSocketSession session, CommandActivator ca) throws Exception{
        if (parameters.length != expectedsNumParams) {
            sendError(id, session, "The parameters field is wrong. Expected " + expectedsNumParams + " parameters.");
        }
        try{
            ca.run();
            sendSuccess(id, session);
        }catch (Exception e) {
            sendError(id, session, e.getMessage());
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        final ClientRequest request;
        try{
            request = JSON.parseObject(message.getPayload(), ClientRequest.class);
        }catch(Exception e){
            sendError("", session, "Cannot resolve the format of this message: + " + message.getPayload() + " + Please check the content again: " + e.getMessage());
            return;
        }

        String id = request.getId();

        if ("register".equals(request.getCommand())) { // username, password
            runCommand(request.getParameters(), 2, id, session,
                    () -> registerHandler.register(request.getParameters()[0], request.getParameters()[1]));
        } else if ("login".equals(request.getCommand())) { // username, password
            if (request.getParameters().length != 2) {
                sendError(id, session, "The parameters field is wrong. Expected 2 parameters.");
            }
            try {
                String username = request.getParameters()[0];
                String password = request.getParameters()[1];
                // validate the username and password, throw exception if the authentication information is incorrect.
                loginHandler.login(username, password);

                // store in state keeper and get auth token
                stateKeeper.login(username, session);

                sendSuccess(id, session);
            } catch (Exception e) {
                sendError(id, session, e.getMessage());
            }
        } else if ("send".equals(request.getCommand())) { // message, roomId

            // check if the sender is login.
            if (!stateKeeper.isLogin(session)) {
                sendMessageError(id, session, "Not login yet.");
                return;
            }

            long timestamp = System.currentTimeMillis();

            // check parameters
            if (request.getParameters().length != 2) {
                sendMessageError(id, session, "The parameter field is wrong.");
                return;
            }

            // parse parameters
            String messageContent = request.getParameters()[0];
            int roomId;
            try {
                roomId = Integer.parseInt(request.getParameters()[1]);
            } catch (Exception e) {
                throw new Exception("Room id must be an integer.");
            }

            // check if the sender is in this room
            if (!stateKeeper.isInRoom(session, roomId)) {
                sendMessageError(id, session, "非法访问, Unauthorized Access.");
                stateKeeper.banUser(session, stateKeeper.getUsername(session), "sending message to other room.");
                return;
            }

            // store the message to database and broadcast the message to the channel
            try {
                // long send(String usernameSender, long timestamp, String content, int roomId) throws Exception
                persistentSendHandler.send(stateKeeper.getUsername(session), timestamp, messageContent, roomId);

                // broadcast message to the room
                Message chattingMessage = new Message(messageContent, stateKeeper.getUsername(session), roomId, timestamp);
                stateKeeper.broadcastRoom(new TextMessage(JSON.toJSONString(new Response(id, "message", chattingMessage))), roomId);

            } catch (Exception e) {
                System.err.println("Exception occurs during insert message into database: " + e.getMessage());
            }
        } else if ("load".equals(request.getCommand())) {
            if( ! stateKeeper.isLogin(session)){
                sendError(id, session, "Not login yet.");
                return;
            }

            if(request.getParameters().length != 3){
                sendError(id, session, "The parameters field is wrong. Expected 2 parameters.");
                return;
            }

            // parse parameters
            int roomId;
            int count;
            long endTimestamp;
            try {
                roomId = Integer.parseInt(request.getParameters()[0]);
                endTimestamp = Long.parseLong(request.getParameters()[1]);
                count = Integer.parseInt(request.getParameters()[2]);
            } catch (Exception e) {
                sendError(id, session, "The count and timestamp field should be an integer.");
                return;
            }

            // check if the sender is in this room
            if (!stateKeeper.isInRoom(session, roomId)) {
                sendMessageError(id, session, "非法访问, Unauthorized Access.");
                stateKeeper.banUser(session, stateKeeper.getUsername(session), "sending message to other room.");
                return;
            }

            // Load messages from database
            List<Message> messageHistory = loadHistoryHandler.load(roomId, endTimestamp, count);

            // Send to the user
            session.sendMessage(new TextMessage(JSON.toJSONString(
                    new Response(id, "history",
                            new HistoryMessage(
                                    request.getParameters()[0],
                                    request.getParameters()[1],
                                    messageHistory
                            )
                    ))
            ));
        }else if("createDirectMessage".equals(request.getCommand())){
            if( !stateKeeper.isLogin(session)){
                sendError(id, session, "Not login yet.");
                return;
            }

            if(request.getParameters().length != 1){
                sendError(id, session, "The parameters field should be [\"username of sender\"]");
                return;
            }

            // parse parameters
            String usernameReceiver = request.getParameters()[0];

            // store into database
            int roomId;
            try{
                roomId = createDirectMessageHandler.createDirectMessage(stateKeeper.getUsername(session), usernameReceiver);
            }catch (Exception e){
                sendError(id, session, "Failed to create direct message: " + e.getMessage());
                return;
            }


            // update statekeeper
            stateKeeper.createRoom(roomId, session);
            stateKeeper.subscribeToRoom(roomId, stateKeeper.getSession(usernameReceiver));

            send(session, JSON.toJSONString(new Response(id, "createDirectMessage", String.format("{\"roomId\": %s}", roomId))));
        }else if("roomList".equals(request.getCommand())){
            if( !stateKeeper.isLogin(session)){
                sendError(id, session, "Not login yet.");
                return;
            }
            try{
                send(session, JSON.toJSONString(new Response(id, "getRoomList", new RoomListResponse(
                        getRoomListHandler.getRoomList(stateKeeper.getUsername(session))
                ))));
            }catch (Exception e){
                sendError(id, session, "Failed to get room list: " + e.getMessage());
            }

        }
//        else if ("delete".equals(request.getCommand())) {
//            runCommand(request.getParameters(), 1, id, session,
//                    () -> deleteUserHandler.delete(request.getParameters()[0]));
//
//            session.sendMessage(new TextMessage("Unrecognized command: " + request.getCommand()));
//        }
        else {
            session.sendMessage(new TextMessage("Unrecognized command: " + request.getCommand()));
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        System.out.println("[IN] Client" + session.getId() + " is connected.");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        System.out.println("[OUT] Client " + session.getId() + " is closed.");
        stateKeeper.logout(session);
    }
}