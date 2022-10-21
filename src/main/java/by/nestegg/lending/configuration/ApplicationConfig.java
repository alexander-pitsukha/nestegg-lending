package by.nestegg.lending.configuration;

import by.nestegg.lending.entity.User;
import by.nestegg.lending.security.AuditorAwareImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.TimeZone;

@Configuration
@EnableAspectJAutoProxy
@EnableJpaAuditing
@EnableScheduling
public class ApplicationConfig implements WebMvcConfigurer {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public AuditorAware<User> auditorAware() {
        return new AuditorAwareImpl();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/message_ru");
        return messageSource;
    }

    @Bean
    @Autowired
    public LocalValidatorFactoryBean localValidatorFactoryBean(MessageSource messageSource) {
        var localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.setValidationMessageSource(messageSource);
        return localValidatorFactoryBean;
    }

    @Autowired
    public void setTimeZone(ObjectMapper objectMapper) {
        objectMapper.setTimeZone(TimeZone.getDefault());
    }

}
