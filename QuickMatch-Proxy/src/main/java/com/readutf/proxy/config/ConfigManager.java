package com.readutf.proxy.config;

import lombok.SneakyThrows;

import java.io.File;

public class ConfigManager {

//    private @Getter final YamlConfig yamlConfig;

    @SneakyThrows
    public ConfigManager(File dataFolder) {

//        File configFile = new File(dataFolder, "config.yml");
//        if (!configFile.exists()) {
//            InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("config.yml");
//
//            yamlConfig = YamlConfig.load(resourceAsStream);
//
//            Files.copy(resourceAsStream, configFile.toPath());
//        } else {
//            yamlConfig = YamlConfig.load(new FileInputStream(configFile));
//        }

    }
}
