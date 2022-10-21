package by.nestegg.lending;

import by.nestegg.lending.controller.UserController;
import by.nestegg.lending.repository.UserRepository;
import by.nestegg.lending.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = "wiremock.server.port=8080")
class NesteggLendingApplicationTests extends BasicTests {

    @Autowired
    private UserController userController;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void contextLoads() {
        assertNotNull(userController);
        assertNotNull(userService);
        assertNotNull(userRepository);
    }

}
