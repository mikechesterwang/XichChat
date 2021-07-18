package com.xich.xichim.handler;

import com.xich.xichim.handlerAbstract.IDeleteUserHandler;
import com.xich.xichim.model.entity.User;
import com.xich.xichim.model.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteUserHandler implements IDeleteUserHandler {

    private final UserMapper userMapper;

    public DeleteUserHandler(UserMapper userMapper){
        this.userMapper = userMapper;
    }
    public void delete(String username) throws Exception{
        userMapper.delete(username);
    }
}
