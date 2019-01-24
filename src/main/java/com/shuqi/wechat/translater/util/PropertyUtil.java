package com.shuqi.wechat.translater.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
    private static Properties props;

    static {
        loadProps();
    }

    synchronized static private void loadProps() {
        props = new Properties();
        InputStream in = null;
        try {
            in = PropertyUtil.class.getClassLoader().getResourceAsStream("general.properties");
            props.load(in);
        } catch (FileNotFoundException e) {
            logger.error("general.properties�ļ�δ�ҵ�");
        } catch (IOException e) {
            logger.error("����IOException");
        } finally {
            try {
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                logger.error("general.properties�ļ����رճ����쳣");
            }
        }
        logger.info("����properties�ļ��������...........");
    }

    public static String getProperty(String key) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        if (null == props) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }
}