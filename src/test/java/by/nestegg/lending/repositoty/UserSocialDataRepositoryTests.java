package by.nestegg.lending.repositoty;

import by.nestegg.lending.entity.UserSocialData;
import by.nestegg.lending.repository.UserSocialDataRepository;
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
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserSocialDataRepositoryTests extends AbstractRepositoryTests {

    @Autowired
    private UserSocialDataRepository userSocialDataRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFindByUserId() throws Exception {
        UserSocialData userSocialData = saveUserSocialData();

        UserSocialData entity = userSocialDataRepository.findByUserId(1L);

        assertEntry(userSocialData, entity);
    }

    @Test
    void testFindByFacebookId() throws Exception {
        UserSocialData userSocialData = saveUserSocialData();

        UserSocialData entity = userSocialDataRepository.findByFacebookId(userSocialData.getFacebookId());

        assertEntry(userSocialData, entity);
    }

    @Test
    void testFindByLinkedinId() throws Exception {
        UserSocialData userSocialData = saveUserSocialData();

        UserSocialData entity = userSocialDataRepository.findByLinkedinId(userSocialData.getLinkedinId());

        assertEntry(userSocialData, entity);
    }

    @Test
    void testFindByInstagramId() throws Exception {
        UserSocialData userSocialData = saveUserSocialData();

        UserSocialData entity = userSocialDataRepository.findByInstagramId(userSocialData.getInstagramId());

        assertEntry(userSocialData, entity);
    }

    @Test
    void testFindByVkontakteId() throws Exception {
        UserSocialData userSocialData = saveUserSocialData();

        UserSocialData entity = userSocialDataRepository.findByVkontakteId(userSocialData.getVkontakteId());

        assertEntry(userSocialData, entity);
    }

    @Test
    void testUpdateFacebook() throws Exception {
        UserSocialData userSocialData = saveUserSocialData();
        Long userId = userSocialData.getUser().getId();

        userSocialDataRepository.updateFacebook(userId);

        userSocialData = userSocialDataRepository.findByUserId(userId);

        assertNull(userSocialData.getFacebookId());
        assertNull(userSocialData.getFacebookToken());
        assertNull(userSocialData.getFacebookData());
    }

    @Test
    void testUpdateLinkedIn() throws Exception {
        UserSocialData userSocialData = saveUserSocialData();
        Long userId = userSocialData.getUser().getId();

        userSocialDataRepository.updateLinkedIn(userId);

        userSocialData = userSocialDataRepository.findByUserId(userId);

        assertNull(userSocialData.getLinkedinId());
        assertNull(userSocialData.getLinkedinToken());
        assertNull(userSocialData.getLinkedinData());
    }

    @Test
    void testUpdateInstagram() throws Exception {
        UserSocialData userSocialData = saveUserSocialData();
        Long userId = userSocialData.getUser().getId();

        userSocialDataRepository.updateInstagram(userId);

        userSocialData = userSocialDataRepository.findByUserId(userId);

        assertNull(userSocialData.getInstagramId());
        assertNull(userSocialData.getInstagramToken());
        assertNull(userSocialData.getInstagramData());
    }

    @Test
    void testUpdateVkontakte() throws Exception {
        UserSocialData userSocialData = saveUserSocialData();
        Long userId = userSocialData.getUser().getId();

        userSocialDataRepository.updateVkontakte(userId);

        userSocialData = userSocialDataRepository.findByUserId(userId);

        assertNull(userSocialData.getVkontakteId());
        assertNull(userSocialData.getVkontakteToken());
        assertNull(userSocialData.getVkontakteData());
    }

    private UserSocialData saveUserSocialData() throws IOException {
        return saveTestEntity("user/data/user_social_data.json", UserSocialData.class);
    }

    private void assertEntry(UserSocialData userSocialData, UserSocialData entity) {
        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertEquals(userSocialData.getFacebookId(), entity.getFacebookId());
        assertEquals(userSocialData.getFacebookToken(), entity.getFacebookToken());
        assertEquals(userSocialData.getFacebookData(), entity.getFacebookData());
        assertEquals(userSocialData.getLinkedinId(), entity.getLinkedinId());
        assertEquals(userSocialData.getLinkedinToken(), entity.getLinkedinToken());
        assertEquals(userSocialData.getLinkedinData(), entity.getLinkedinData());
        assertEquals(userSocialData.getInstagramId(), entity.getInstagramId());
        assertEquals(userSocialData.getInstagramData(), entity.getInstagramData());
        assertEquals(userSocialData.getVkontakteId(), entity.getVkontakteId());
        assertEquals(userSocialData.getVkontakteToken(), entity.getVkontakteToken());
        assertEquals(userSocialData.getVkontakteData(), entity.getVkontakteData());
    }

}
