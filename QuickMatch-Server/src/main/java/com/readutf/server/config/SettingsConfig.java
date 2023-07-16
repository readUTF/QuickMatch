package com.readutf.server.config;

import com.github.jsixface.YamlConfig;
import redis.clients.jedis.JedisPool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class SettingsConfig {

//    private final YamlConfig yamlConfig;
    private JedisPool jedisPool;

    public SettingsConfig() {

    }

    public JedisPool getJedisPool() {
        if(jedisPool != null) return jedisPool;
        return jedisPool = new JedisPool();
    }

}
