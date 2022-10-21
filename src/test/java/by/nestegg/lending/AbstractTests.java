package by.nestegg.lending;

import by.nestegg.lending.configuration.TestAppConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;

@Getter
@Import(TestAppConfiguration.class)
public abstract class AbstractTests {

    @Autowired
    private ObjectMapper objectMapper;

    public <T> T getObjectFromJson(String fileSource, Class<T> valueType) throws java.io.IOException {
        return objectMapper.readValue(new ClassPathResource(fileSource).getInputStream(), valueType);
    }

}
