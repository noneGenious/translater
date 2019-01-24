package com.shuqi.wechat.translater.dao;

import com.shuqi.wechat.translater.bean.Word;

public interface WordsDao {
    public void addWord(Word word);
    public Word findWord(String word);

}
