package com.venhancer.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads {@code config.properties} from the classpath once and exposes typed accessors.
 * All configuration keys used by the framework (browser, base URL, timeouts, etc.)
 * are retrieved through this class.
 */
public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    private ConfigReader() {}

    /**
     * Returns the value associated with {@code key}, or {@code null} if not present.
     *
     * @param key the property key
     * @return the property value, or {@code null}
     */
    public static String get(String key) {
        return properties.getProperty(key);
    }

    /**
     * Returns the value associated with {@code key}, falling back to {@code defaultValue}.
     *
     * @param key          the property key
     * @param defaultValue the value to return when the key is absent
     * @return the property value, or {@code defaultValue}
     */
    public static String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
