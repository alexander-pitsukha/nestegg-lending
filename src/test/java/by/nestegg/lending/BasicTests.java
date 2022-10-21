package by.nestegg.lending;

import by.nestegg.lending.configuration.TestAppConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Import({TestAppConfiguration.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(initializers = {BasicTests.Initializer.class})
@Sql("classpath:user/sql/insert_users.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public abstract class BasicTests extends AbstractTests {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of("spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                            "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                            "spring.datasource.password=" + postgreSQLContainer.getPassword())
                    .applyTo(configurableApplicationContext.getEnvironment());
        }
    }

}
