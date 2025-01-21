package mosaos.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import mosaos.exception.ApplicationException;

/**
 * Application Config.
 */
@Slf4j
public class AppConfig {

    private static final String CONFIG_FILE_PATH = "application.properties";
    private static Properties props;

    static {
        try (InputStream is = AppConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE_PATH)) {
            props = new Properties();
            props.load(is);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new ApplicationException("Failed to load application configuration", e);
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }
    public static String getJdbcDriver() {
        return props.getProperty("jdbc.driver");
    }
    public static String getJdbcUrl() {
        return props.getProperty("jdbc.url");
    }
    public static String getJdbcUser() {
        return props.getProperty("jdbc.user");
    }
    public static String getJdbcPassword() {
        return props.getProperty("jdbc.password");
    }
    public static Locale getDefaultLocale() {
        String value = props.getProperty("locale.default");
        return Locale.forLanguageTag(value);
    }
    public static boolean isThymeleafCache() {
        String value = props.getProperty("thymeleaf.cache");
        return Boolean.getBoolean(value);
    }
}
