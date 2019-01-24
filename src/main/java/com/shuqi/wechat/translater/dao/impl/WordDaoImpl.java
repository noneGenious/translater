package com.shuqi.wechat.translater.dao.impl;

import com.shuqi.wechat.translater.bean.Word;
import com.shuqi.wechat.translater.dao.WordsDao;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository("wordsDao")
public class WordDaoImpl implements WordsDao {

    @Resource(name="mongoTemplate")
    private MongoOperations mongoOper;


    @Override
    public void addWord(Word word) {
        mongoOper.insert(word);
    }

    @Override
    public Word findWord(String word) {
        Criteria criteria = Criteria.where("word").is(word);
        Query query = new Query(criteria);
        return mongoOper.findOne(query, Word.class);
    }
}
