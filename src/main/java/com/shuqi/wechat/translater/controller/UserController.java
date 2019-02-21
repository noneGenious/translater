package com.shuqi.wechat.translater.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shuqi.wechat.translater.bean.User;
import com.shuqi.wechat.translater.service.DictService;
import com.shuqi.wechat.translater.service.UserService;
import com.shuqi.wechat.translater.util.HttpUtils;
import com.shuqi.wechat.translater.util.PropertyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final String APPID = PropertyUtil.getProperty("wechat.appid");
    private static final String SECRET = PropertyUtil.getProperty("wechat.secret");
    private static final String WECHAT_API = PropertyUtil.getProperty("wechat.api");

    @Resource(name = "userService")
    private UserService userService;
    @Autowired
    private DictService dictService;

    public void setDictService(DictService dictService) {
        this.dictService = dictService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/login")
    public JSONObject login(String token, String _id) {

        User user;
        if (StringUtils.isBlank(_id)) {
            user = null;
        } else {
            user = userService.findUserById(_id);
        }
        //判断数据库中是否有该用户_id/客户端是否已有id
        JSONObject out = new JSONObject();
        if (user == null) {
            JSONObject jsonObject = register(token);
            if (StringUtils.isBlank(jsonObject.getString("openid"))) {
                return jsonObject;
            }
            String openid = jsonObject.getString("openid");
            String sessionKey = jsonObject.getString("session_key");
            //确认用户是第一次登陆
            user = userService.findUserByOpenId(openid);
            if (user == null) {
                user = new User();
                user.setOpenid(openid);
                user.setSessionKey(sessionKey);
                userService.addUser(user);
                user = userService.findUserByOpenId(openid);
                dictService.createDict(user.get_id());
            } else {
                userService.updateUserInfo(user);
            }
        }
        out.put("_id", user.get_id());
        out.fluentPut("status", "success");
        return out;
    }

    private JSONObject register(String token) {
        Map params = new HashMap<>();
        params.put("appid", APPID);
        params.put("secret", SECRET);
        params.put("js_code", token);
        params.put("grant_type", "authorization_code");
        try {
            String result = HttpUtils.doGet(WECHAT_API, params);
            JSONObject jsonObject = JSON.parseObject(result);
            String openid = jsonObject.getString("openid");
            return jsonObject;
        } catch (URISyntaxException | IOException e) {
            return new JSONObject();
        }
    }


}
