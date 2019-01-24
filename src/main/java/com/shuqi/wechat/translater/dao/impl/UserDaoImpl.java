package com.shuqi.wechat.translater.dao.impl;

import com.shuqi.wechat.translater.bean.User;
import com.shuqi.wechat.translater.dao.UserDao;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import javax.annotation.Resource;

public class UserDaoImpl implements UserDao {
    @Resource(name="mongoTemplate")
    private MongoOperations mongoOper;

    @Override
    public void addUser(User user) {
        mongoOper.insert(user);
    }

    @Override
    public User findUser(String openid) {
        Criteria criteria = Criteria.where("openid").is(openid);
        Query query = new Query(criteria);
        return mongoOper.findOne(query, User.class);
    }

    @Override
    public void updateUserInfo(User user) {
        Criteria criteria = Criteria.where("openid").is(user.getOpenid());
        Query query = new Query(criteria);
        Update update = new Update().set("session_key", user.getSessionKey());
        mongoOper.updateFirst(query, update, User.class);
    }
}
