package com.shuqi.wechat.translater.controller;

import com.shuqi.wechat.translater.bean.Dict;
import com.shuqi.wechat.translater.bean.Word;
import com.shuqi.wechat.translater.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    public void setDictService(DictService dictService) {
        this.dictService = dictService;
    }

    @RequestMapping("/query")
    public Dict getDict(String userId){
        Dict dict = dictService.getDict(userId);
        if(dict == null){
            dictService.createDict(userId);
            dict = dictService.getDict(userId);
        }
        return dict;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void add2Dict(String userId, @RequestBody Map data){
        Word word = new Word();
        word.setWord((String) data.get("word"));
        word.setTranslate((Map) data.get("translate"));
        dictService.add2Dict(userId, word);
    }

    @RequestMapping("/del")
    public void deleteFromDict(String userId, String word){
        Word w =  new Word();
        w.setWord(word);
        dictService.deleteFormDict(userId, w);
    }

}
