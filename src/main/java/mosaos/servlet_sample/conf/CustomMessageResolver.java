package mosaos.servlet_sample.conf;

import java.util.Locale;
import java.util.ResourceBundle;

import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.messageresolver.AbstractMessageResolver;

import lombok.extern.slf4j.Slf4j;
import mosaos.servlet_sample.util.MessageUtil;

/**
 * MessageResolver for thymeleaf.
 * 
 * By using this class,
 * Resources can now be referenced directly from templates using the
 * `th:text="#{property name}"` format.
 * 
 * @see MessageUtil
 * @see src/main/resources/messages_xx.properties
 */
@Slf4j
public class CustomMessageResolver extends AbstractMessageResolver {

    public CustomMessageResolver() {
        super();
        Locale.setDefault(AppConfig.getDefaultLocale());
        log.debug("Set default locale to '{}'", Locale.getDefault());
    }
    
    @Override
    public String resolveMessage(ITemplateContext context, Class<?> origin, String key, Object[] messageParameters) {
        try {
            log.debug("Locale: {}", context.getLocale());
            ResourceBundle bundle = ResourceBundle.getBundle(MessageUtil.BASE_NAME, context.getLocale());
            String message = bundle.getString(key);
            return message;
        } catch (Exception e) {
            return "key:`" + key + "` could not resolved.";
        }
    }

    @Override
    public String createAbsentMessageRepresentation(ITemplateContext context, Class<?> origin, String key,
            Object[] messageParameters) {
        return "key:`" + key + "` is not defined.";
    }
}
