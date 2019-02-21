package com.shuqi.wechat.translater.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.shuqi.wechat.translater.bean.Word;
import com.shuqi.wechat.translater.service.WordsService;
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
import java.io.*;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Controller
@RequestMapping("/t")
public class TranslaterController {

    @Resource(name = "wordsService")
    private WordsService wordService;

    private static final String APP_KEY = PropertyUtil.getProperty("youdao.appKey");
    private static final String SCREAT = PropertyUtil.getProperty("youdao.screat");
    private static final String YOUDAO_TEXTT = PropertyUtil.getProperty("youdao.textt");
    private static final String YOUDAO_OCRT = PropertyUtil.getProperty("youdao.ocrt");
    private static final String YOUDAO_AUDIO = PropertyUtil.getProperty("youdao.audio");
    private static final String AUDIO_PATH = PropertyUtil.getProperty("audio.path");
    private static final String FFMPEG_PATH = PropertyUtil.getProperty("ffmpeg.path");

    private enum OriginType {IMG, TEXT, AUDIO}

    private enum EncryptionType {MD5, SHA}

    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "unchecked"})
    @RequestMapping("/text")
    @ResponseBody
    public Word translateText(String q, @RequestParam(defaultValue = "auto") String from,
                              @RequestParam(defaultValue = "auto") String to) {

        try {
            Map params = packageParams(q, from, to, EncryptionType.SHA, OriginType.TEXT);
            String resp = HttpUtils.doGet(YOUDAO_TEXTT, params);
            Word word = new Word();
            word.setTranslate(JSON.parseObject(resp, Map.class));
            word.setWord(q);
            return word;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Word();
    }

    @SuppressWarnings({"unchecked", "MismatchedQueryAndUpdateOfCollection"})
    @RequestMapping("/img")
    @ResponseBody
    public JSONObject translateImg(HttpServletRequest request, @RequestParam(defaultValue = "auto") String from,
                                   @RequestParam(defaultValue = "en") String to,
                                   @RequestParam(defaultValue = "300") Integer height,
                                   @RequestParam(defaultValue = "300") Integer width) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());

        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multRequest.getFileNames();
            if (iter.hasNext()) {
                MultipartFile multFile = multRequest.getFile(iter.next());

                try {
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();;
                    long maxSize = 1 * 1024 * 1024;
                    float quality = 0.7f;
                    InputStream fileInput = multFile.getInputStream();
                    if(fileInput.available() > maxSize){
                        Thumbnails.of(fileInput).scale(1f).outputQuality(quality).toOutputStream(byteArrayOutputStream);
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                        while(byteArrayInputStream.available() > maxSize){
                            quality -= 0.2;
                            byteArrayOutputStream.close();
                            byteArrayOutputStream = new ByteArrayOutputStream();
                            Thumbnails.of(byteArrayInputStream).scale(1f).outputQuality(quality).toOutputStream(byteArrayOutputStream);
                            byteArrayInputStream.close();
                            byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
                        }
                    }else{

                        Thumbnails.of(fileInput).scale(1f).toOutputStream(byteArrayOutputStream);
                    }

//                    Thumbnails.of(multFile.getInputStream()).size(300, 300).toOutputStream(byteArrayOutputStream);
                    byte[] bytes = byteArrayOutputStream.toByteArray();
                    String base64img = new BASE64Encoder().encode(bytes);
                    Map params = packageParams(base64img, from, to, EncryptionType.MD5, OriginType.IMG);
                    params.put("docType", "json");
                    params.put("type", 1);
                    String result = HttpUtils.doPost(YOUDAO_OCRT, params);
                    return JSON.parseObject(result);
                } catch (IOException | URISyntaxException e) {
                    e.printStackTrace();
                }

            }

        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value="/audio")
    public Map translateAudio(HttpServletRequest request, String userId,
                                     @RequestParam(defaultValue = "zh-CHS") String from,
                                     @RequestParam(defaultValue = "en") String to) {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if (multipartResolver.isMultipart(request)) {
            MultipartHttpServletRequest multRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = multRequest.getFileNames();
            if (iter.hasNext()) {
                try {
                    MultipartFile multFile = multRequest.getFile(iter.next());
                    String sourcePath = AUDIO_PATH+"/" + userId+".acc";
                    String targetPath = AUDIO_PATH+"/" + userId+".wav";
                    multFile.transferTo(new File(sourcePath));
                    this.acc2wav(sourcePath, targetPath);
                    File wavFile = new File(targetPath);
                    FileInputStream fileInptu = new FileInputStream(wavFile);
                    byte[] bytes = new byte[fileInptu.available()];
                    fileInptu.read(bytes);
                    String base64audio = new BASE64Encoder().encode(bytes);
                    Map params = packageParams(base64audio, from, to, EncryptionType.MD5, OriginType.AUDIO);
                    params.put("format", "wav");
                    params.put("rate", 8000);
                    params.put("channel", 1);
                    params.put("type", 1);
                    String result = HttpUtils.doPost(YOUDAO_AUDIO, params);
                    return JSON.parseObject(result, Map.class);
                } catch (IOException | URISyntaxException | InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
        return new HashMap();
    }

    @SuppressWarnings("unchecked")
    private Map packageParams(String q, String from, String to, EncryptionType et, OriginType ot) throws UnsupportedEncodingException {
        String salt = String.valueOf(System.currentTimeMillis());
        Map params = new HashMap();
        params.put("q", q.trim());
        params.put("from", from);
        params.put("to", to);
        params.put("appKey", APP_KEY);
        params.put("salt", salt);
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        String signStr;
        if (ot == OriginType.IMG || ot == OriginType.AUDIO) {
            signStr = APP_KEY + q + salt + SCREAT;
        } else {
            signStr = APP_KEY + truncate(URLDecoder.decode(q, "UTF-8")) + salt + curtime + SCREAT;
            params.put("curtime", curtime);
            params.put("signType", "v3");
        }
        String sign;
        if (et == EncryptionType.MD5) {
            sign = TranslaterUtil.md5(signStr);
        } else {
            sign = TranslaterUtil.sha(signStr);
        }
        params.put("sign", sign);
        return params;
    }


    private String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }

    private void acc2wav(String sourcePath, String targetPath) throws IOException, InterruptedException {
        Runtime run = Runtime.getRuntime();
        String command = FFMPEG_PATH+"/ffmpeg -y -i " + sourcePath + " -ar 8000 -ac 1 -acodec pcm_s16le "+ targetPath;
        Process p = run.exec(command);
        p.getOutputStream().close();
        p.getInputStream().close();
        p.getErrorStream().close();
        p.waitFor();
        run.freeMemory();
    }

}
