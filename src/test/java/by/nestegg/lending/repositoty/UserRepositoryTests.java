package by.nestegg.lending.repositoty;

import by.nestegg.lending.entity.User;
import by.nestegg.lending.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTests extends AbstractRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFindByNickname() throws Exception {
        User user = saveUserBySocial();

        User entity = userRepository.findByNickname(user.getNickname());

        assertEntry(user, entity);
    }

    @Test
    void testFindByDeviceToken() throws Exception {
        User user = saveUserBySocial();

        User entity = userRepository.findByDeviceToken(user.getDeviceToken());

        assertEntry(user, entity);
    }

    @Test
    void testFindByPhoneNumber() throws Exception {
        User user = saveTestEntity("user/user_phone_number.json", User.class);

        User entity = userRepository.findByPhoneNumber(user.getPhoneNumber());

        assertNotNull(entity.getId());
        assertEquals(user.getPhoneNumber(), entity.getPhoneNumber());
        assertEquals(user.getVerifiedPhoneCode(), entity.getVerifiedPhoneCode());
        assertEquals(user.getDisabled(), entity.getDisabled());
        assertEquals(user.getPushActive(), entity.getPushActive());
        assertEquals(user.getRoles().get(0), entity.getRoles().get(0));
    }

    private User saveUserBySocial() throws IOException {
        return saveTestEntity("user/user_1.json", User.class);
    }

    private void assertEntry(User user, User entity) {
        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertEquals(user.getNickname(), entity.getNickname());
        assertEquals(user.getDeviceToken(), entity.getDeviceToken());
        assertEquals(user.getDisabled(), entity.getDisabled());
        assertEquals(user.getPushActive(), entity.getPushActive());
        assertEquals(user.getRoles().get(0), entity.getRoles().get(0));
    }

}
