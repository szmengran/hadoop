package com.szmengran.hbase.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Package com.szmengran.hbase.config
 * @Description: TODO
 * @date 2018年9月21日 下午4:01:47
 * @author <a href="mailto:android_li@sina.cn">Joe</a>
 */
@ConfigurationProperties(prefix = "hbase")
public class HBaseProperties {

    private Map<String, String> config;
    
    public Map<String, String> getConfig() {
        return config;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }
}
