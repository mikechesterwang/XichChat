package com.xich.xichim.handler;

import com.xich.xichim.handlerAbstract.ILoginHandler;
import com.xich.xichim.model.mapper.RoomMapper;
import com.xich.xichim.model.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginHandler implements ILoginHandler {
    private final UserMapper userMapper;
    private final RoomMapper roomMapper;

    public LoginHandler(UserMapper userMapper, RoomMapper roomMapper){
        this.userMapper = userMapper;
        this.roomMapper = roomMapper;
    }

    public void login(String username, String password) throws Exception{
        // verify password
        if( userMapper.verifyUsernameAndPassword(username, password) == 0){
            throw new Exception("Incorrect username or password. Please try again.");
        }

        // subscribe channel
        List<Integer> roomList = roomMapper.getRoomIdList(username);

    }

    @Override
    public List<Integer> getRoomList(String username) throws Exception {
        return null;
    }
}
