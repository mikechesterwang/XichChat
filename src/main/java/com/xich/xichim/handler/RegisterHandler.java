package com.xich.xichim.handler;

import com.xich.xichim.handlerAbstract.IRegisterHandler;
import com.xich.xichim.model.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
public class RegisterHandler implements IRegisterHandler {

    private final UserMapper userMapper;

    public RegisterHandler(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    public void register(String username, String password) throws Exception{
        if(userMapper.checkExistence(username) != 0){
            throw new Exception("Username " + username + " already exists.");
        }
        userMapper.register(username, password);
    }
}
