package com.shuqi.wechat.translater.bean;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "dicts")
public class Dict {
    @Field("dict")
    private List dict;
    @Field("user_id")
    private String userId;
    @Id
    private String objectId;
    public List getDict() {
        return dict;
    }

    public void setDict(List dict) {
        this.dict = dict;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
