package com.shuqi.wechat.translater.service;

import com.shuqi.wechat.translater.bean.Dict;
import com.shuqi.wechat.translater.bean.Word;
import com.shuqi.wechat.translater.dao.DictDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictService {
    @Autowired
    private DictDao dictDao;

    public void setDictDao(DictDao dictDao) {
        this.dictDao = dictDao;
    }

    public void add2Dict(String userId, Word word){
        dictDao.add2Dict(userId, word);
    }

    public void deleteFormDict(String userId, Word word){
        dictDao.deleteFormDict(userId, word);
    }

    public Dict getDict(String userId){
        return dictDao.getDict(userId);
    }

    public  void createDict(String userId){
     dictDao.createDict(userId);
    }

}
