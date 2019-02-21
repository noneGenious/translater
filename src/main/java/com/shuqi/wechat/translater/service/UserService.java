package com.shuqi.wechat.translater.service;

import com.shuqi.wechat.translater.bean.User;
import com.shuqi.wechat.translater.dao.UserDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("userService")
public class UserService {

    @Resource(name = "userDao")
    private UserDao userDao;

    public void addUser(User user){
        userDao.addUser(user);
    }

    public User findUserById(String userId){
        return userDao.findUserById(userId);
    }

    public User findUserByOpenId(String openId){return userDao.findUserByOpenId(openId);}

    public void updateUserInfo(User user){
        userDao.updateUserInfo(user);
    }
}

