package com.shuqi.wechat.translater.bean;

import java.util.Map;

public class Word {
    private String userId;
    private String word;
    private Map translate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Map getTranslate() {
        return translate;
    }

    public void setTranslate(Map translate) {
        this.translate = translate;
    }

    public Word() {
    }
}
