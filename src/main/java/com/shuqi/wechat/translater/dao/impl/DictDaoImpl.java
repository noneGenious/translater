package com.shuqi.wechat.translater.dao.impl;

import com.alibaba.fastjson.JSON;
import com.shuqi.wechat.translater.bean.Dict;
import com.shuqi.wechat.translater.bean.Word;
import com.shuqi.wechat.translater.dao.DictDao;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;

@Repository
public class DictDaoImpl implements DictDao {

    @Resource(name="mongoTemplate")
    private MongoOperations mongoOper;

    @Override
    public void add2Dict(String userId, Word word) {
        Criteria criteria = Criteria.where("user_id").is(userId);
        Query query = new Query(criteria);
        Update update = new Update();
        update.addToSet("dict", word);
        mongoOper.updateFirst(query, update, Dict.class);
    }

    @Override
    public void deleteFormDict(String userId, Word word) {
        Criteria criteria = Criteria.where("user_id").is(userId);
        Query query = new Query(criteria);
        Update update = new Update();
        Document doc = new Document().append("word", word.getWord());
        update.pull("dict", doc);
        mongoOper.updateFirst(query, update, Dict.class);
    }

    @Override
    public Dict getDict(String userId) {
        Criteria criteria = Criteria.where("user_id").is(userId);
        Query query = new Query(criteria);
        return mongoOper.findOne(query, Dict.class);
    }

    @Override
    public void createDict(String userId) {
        Dict dict = new Dict();
        dict.setUserId(userId);
        dict.setDict(new ArrayList());
        mongoOper.insert(dict);
    }
}
