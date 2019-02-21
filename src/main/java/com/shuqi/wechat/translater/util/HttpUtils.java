package com.shuqi.wechat.translater.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    private static final int SOCKET_TIMEOUT = 120000;//ͨ��ʱ��
    private static final int CONNECT_TIMEOUT = 120000;//���ӳ�ʱʱ��
    private static final int MAX_PER_ROUTE = 3000;
    private static final int MAX_TOTAL = 4000;

    private static CloseableHttpClient httpClient;

    private static final Logger log = Logger.getLogger(HttpUtils.class);

    /**
     * ��ʼ�����ӳ�
     */
    private static synchronized void initPools() {
        if (httpClient == null) {
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
            cm.setDefaultMaxPerRoute(MAX_PER_ROUTE);
            cm.setMaxTotal(MAX_TOTAL);
            httpClient = HttpClients.custom().setConnectionManager(cm).build();
        }
    }

    public static String doGet(String url, Map params) throws URISyntaxException, IOException {
        initPools();
        URI uri = initURL(url, params);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();
        HttpGet httpGet = new HttpGet(uri);
        httpGet.setConfig(requestConfig);
        httpGet.setHeader("Charset", "UTF-8");
        return handleResponse(httpGet);
    }

    public static String doPost(String url, Map params) throws URISyntaxException, IOException {
        initPools();
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIMEOUT).setConnectTimeout(CONNECT_TIMEOUT).build();
        URI uri = initURL(url, params);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setConfig(requestConfig);
        httpPost.setHeader("Charset", "UTF-8");
        return handleResponse(httpPost);
    }

    public static String doGet(String url) throws IOException, URISyntaxException {
        return doGet(url, new HashMap());
    }

    public static String doPost(String url) throws URISyntaxException, IOException {
        return doPost(url, new HashMap());
    }

    private static URI initURL(String url, Map params) throws URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(url);
        for (Object key : params.keySet()) {
            uriBuilder.addParameter(key.toString(), params.get(key).toString());
        }
        return uriBuilder.build();
    }

    private static String handleResponse(HttpRequestBase request) throws IOException {
        CloseableHttpResponse response = httpClient.execute(request);
        StatusLine statusLine = response.getStatusLine();

        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity, "UTF-8");

    }
}
