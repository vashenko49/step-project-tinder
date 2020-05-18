package com.tinder.start;


import com.tinder.exception.ConfigFileException;
import org.apache.commons.dbcp2.BasicDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class ConfigFile {

    private static volatile ConfigFile CONFIG_FILE;
    private static Object mutex = new Object();
    private final Map<String, String> configMap;


    public static ConfigFile getInstance() throws ConfigFileException {
        if (CONFIG_FILE == null) {
            synchronized (mutex) {
                CONFIG_FILE = new ConfigFile();
            }
        }

        return CONFIG_FILE;
    }

    private ConfigFile() throws ConfigFileException {
        try (InputStream input = BasicDataSource.class.getClassLoader().getResourceAsStream("config.properties")) {
            configMap = new HashMap<>();
            Properties prop = new Properties();
            prop.load(input);

            prop.forEach((key, value) -> {
                configMap.put(key.toString(), value.toString());
            });

        } catch (IOException ex) {
           throw new ConfigFileException("Error load config file");
        }
    }

    public String getValueByKey(String key) {
        return configMap.get(key);
    }
}
