package by.nestegg.lending.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageCodeUtil {

    private final Locale locale = Locale.getDefault();

    private final MessageSource messageSource;

    public String getFullErrorMessageByBundleCode(String bundleCode, Object[] objects) {
        try {
            return messageSource.getMessage(bundleCode, objects, locale);
        } catch (NoSuchMessageException e) {
            log.info("Unknown bundle code {}", bundleCode);
        }
        return messageSource.getMessage(Constants.ERROR_MESSAGE, objects, locale);
    }

    public String getFullErrorMessageByBundleCode(String bundleCode) {
        return getFullErrorMessageByBundleCode(bundleCode, null);
    }

}
