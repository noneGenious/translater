package com.shuqi.wechat.translater.controller;

import com.google.gson.Gson;
import com.shuqi.wechat.translater.bean.Word;
import com.shuqi.wechat.translater.service.WordService;
import com.shuqi.wechat.translater.util.HttpUtils;
import com.shuqi.wechat.translater.util.PropertyUtil;
import com.shuqi.wechat.translater.util.TranslaterUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Controller
@RequestMapping("/t")
public class TranslaterController {

    @Resource(name = "wordsService")
    private WordService wordService;

    private static final String APP_KEY = PropertyUtil.getProperty("youdao.appKey");
    private static final String SCREAT = PropertyUtil.getProperty("youdao.screat");
    private static final String YOUDAO_TEXTT = PropertyUtil.getProperty("youdao.textt");
    private static final String YOUDAO_OCRT = PropertyUtil.getProperty("youdao.ocrt");

    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "unchecked"})
    @RequestMapping("/text")
    @ResponseBody
    public Word translateText(String q, @RequestParam(defaultValue = "auto") String from,
                          @RequestParam(defaultValue = "auto") String to){
        Map params = packageParams(q, from, to);
        try {
            String resp = HttpUtils.doGet(YOUDAO_TEXTT, params);
            Gson gson = new Gson();
            return new Word(q, gson.fromJson(resp, Map.class));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "MismatchedQueryAndUpdateOfCollection"})
    @RequestMapping("/img")
    @ResponseBody
    public String translateImg(HttpServletRequest request,@RequestParam(defaultValue = "auto")String from,
                             @RequestParam(defaultValue = "auto")String to,
                             @RequestParam(defaultValue = "300")Integer height,
                             @RequestParam(defaultValue = "300")Integer width){
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());

        if(multipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multRequest.getFileNames();
            if(iter.hasNext()){
                MultipartFile multFile = multRequest.getFile(iter.next());

                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    Thumbnails.of(multFile.getInputStream()).size(300, 300).toOutputStream(byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    String base64img = new BASE64Encoder().encode(bytes);
                    Map params = packageParams(base64img, from, to);
                    params.put("type", 1);
                    return HttpUtils.doPost(YOUDAO_OCRT, params);
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }

            }

        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping("audio")
    public String translateAudio(HttpServletRequest request, @RequestParam(defaultValue = "auto")String from,
                                 @RequestParam(defaultValue = "EN")String to){
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if(multipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multRequest.getFileNames();
            if(iter.hasNext()){
                MultipartFile multFile = multRequest.getFile(iter.next());
                try {
                    byte[] bytes = multFile.getBytes();
                    String base64audio = new BASE64Encoder().encode(bytes);
                    Map params = packageParams(base64audio, from, to);
                    params.put("format", "wav");
                    params.put("rate", 16000);
                    params.put("channel", 1);
                    params.put("type", 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return "";
    }

    @SuppressWarnings("unchecked")
    private Map packageParams(String q, String from, String to){
        Random random = new Random();
        String salt = String.valueOf(random.nextInt(9999999));
        Map params = new HashMap();
        params.put("q", q);
        params.put("from", from);
        params.put("to", to);
        params.put("appKey", APP_KEY);
        params.put("salt", salt);
        params.put("sign", TranslaterUtil.md5(APP_KEY + q + salt + SCREAT));
        return params;
    }

}
