package com.shuqi.wechat.translater.dao.impl;

import com.shuqi.wechat.translater.bean.User;
import com.shuqi.wechat.translater.dao.UserDao;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository("userDao")
public class UserDaoImpl implements UserDao {
    @Resource(name="mongoTemplate")
    private MongoOperations mongoOper;

    @Override
    public void addUser(User user) {
        mongoOper.insert(user);
    }

    @Override
    public User findUserById(String userId) {
        Criteria criteria = Criteria.where("_id").is(new ObjectId(userId));
        Query query = new Query(criteria);
        return mongoOper.findOne(query, User.class);
    }

    @Override
    public User findUserByOpenId(String openid) {
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
