package mosaos.servlet_sample.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Get message resources ( from messages.properties / messages_xx.properties )
 */
public class MessageUtil {

    public static final String BASE_NAME = "i18n/messages"; // base name for properties file.

    public static String getMessage(String key) {
        return getMessage(key, Locale.getDefault());
    }

    public static String getMessage(String key, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, locale);
        return bundle.getString(key);
    }

    public static String getMessage(String key, Locale locale, Object... arguments) {
        ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME, locale);
        String message = bundle.getString(key);
        return String.format(message, arguments);
    }

    public static String getMessage(String key, Object... arguments) {
        return getMessage(key, Locale.getDefault(), arguments);
    }
}