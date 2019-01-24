package com.shuqi.wechat.translater.dao;

import com.shuqi.wechat.translater.bean.User;

public interface UserDao {
    public void addUser(User user);

    public User findUser(String userId);

    public void updateUserInfo(User user);
}
