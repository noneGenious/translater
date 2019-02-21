package com.shuqi.wechat.translater.service;

import com.shuqi.wechat.translater.bean.Word;
import com.shuqi.wechat.translater.dao.WordsDao;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("wordsService")
public class WordsService {

    @Resource(name = "wordsDao")
    private WordsDao wordsDao;

    public void addWord(Word word){
        wordsDao.addWord(word);
    }

    public Word findWord(String word){
        return wordsDao.findWord(word);
    }
}
