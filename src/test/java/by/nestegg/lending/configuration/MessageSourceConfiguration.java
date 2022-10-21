package by.nestegg.lending.configuration;

import by.nestegg.lending.util.MessageCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

@TestConfiguration
public class MessageSourceConfiguration {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/message_ru");
        return messageSource;
    }

    @Bean
    @Autowired
    public MessageCodeUtil messageCodeUtil(MessageSource messageSource) {
        return new MessageCodeUtil(messageSource);
    }

}
