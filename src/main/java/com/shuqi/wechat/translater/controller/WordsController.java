package com.shuqi.wechat.translater.controller;

import com.shuqi.wechat.translater.service.WordsService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/w")
public class WordsController {

    @Resource(name = "wordsService")
    private WordsService wordsService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public void addToDict(@RequestBody Map data, String userId){
        String w = MapUtils.getString(data, "w");
        Map t = MapUtils.getMap(data, "t");

    }

}
