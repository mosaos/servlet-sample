package mosaos.servlet_sample.conf;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import mosaos.servlet_sample.exception.ApplicationException;

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
        return getEnvOrProperty(key);
    }
    public static String getJdbcDriver() {
        return getEnvOrProperty("jdbc.driver");
    }
    public static String getJdbcUrl() {
        String value = getEnvOrProperty("jdbc.url");
        log.info("🌟JDBC_URL  : " + value);
        return value;
    }
    public static String getJdbcUser() {
        String value = getEnvOrProperty("jdbc.user");
        log.info("🌟JDBC_USER : " + value);
        return getEnvOrProperty("jdbc.user");
    }
    public static String getJdbcPassword() {
        return getEnvOrProperty("jdbc.password");
    }
    public static Locale getDefaultLocale() {
        String value = getEnvOrProperty("locale.default");
        return Locale.forLanguageTag(value);
    }
    public static boolean isThymeleafCache() {
        String value = getEnvOrProperty("thymeleaf.cache");
        return Boolean.getBoolean(value);
    }
    public static int getTomcatPort() {
        String value = getEnvOrProperty("tomcat.port");
        return Integer.getInteger(value);
    }
    private static String getEnvOrProperty(final String propKey) {
        String envValue = System.getenv(getEnvKey(propKey));
        return (envValue != null) ? envValue : props.getProperty(propKey);
    }
    private static String getEnvKey(final String key) {
        return "APP_" + key.replaceAll("\\.", "_").toUpperCase();
    }
}
