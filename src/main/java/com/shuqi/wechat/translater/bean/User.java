package com.shuqi.wechat.translater.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class User {
    @Id
    private String _id;
    @Field("openid")
    private String openid;
    @Field("nick_name")
    private String nickName;
    @Field("dict_id")
    private String dictId;
    @Field("session_key")
    private String sessionKey;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getDictId() {
        return dictId;
    }

    public void setDictId(String dictId) {
        this.dictId = dictId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "User{" +
                "_id='" + _id + '\'' +
                ", openid='" + openid + '\'' +
                ", nickName='" + nickName + '\'' +
                ", dictId='" + dictId + '\'' +
                ", sessionKey='" + sessionKey + '\'' +
                '}';
    }
}
