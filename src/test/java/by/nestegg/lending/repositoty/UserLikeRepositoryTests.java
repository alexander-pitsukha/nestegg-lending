package by.nestegg.lending.repositoty;

import by.nestegg.lending.controller.view.LikeCountView;
import by.nestegg.lending.controller.view.UserLikeResultView;
import by.nestegg.lending.entity.User;
import by.nestegg.lending.entity.UserLike;
import by.nestegg.lending.repository.UserLikeRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserLikeRepositoryTests extends AbstractRepositoryTests {

    @Autowired
    private UserLikeRepository userLikeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        User user = saveUser();
        List<UserLike> userLikes = objectMapper.readValue(new ClassPathResource("user/likes.json").getInputStream(),
                new TypeReference<>() {
                });
        userLikes.forEach(userLike -> {
            if (userLike.getUserOwner() == null) {
                userLike.setUserOwner(user);
            }
            saveTestEntity(userLike, UserLike.class);
        });
    }

    @Test
    void testGetLikeAndDislikeCountByUserId() {
        LikeCountView likeCountView = userLikeRepository.getLikeAndDislikeCountByUserId(1L);

        assertEquals(1, likeCountView.getLikeCount());
        assertEquals(1, likeCountView.getDislikeCount());
    }

    @Test
    void testGetLikeResultAndLikeAndDislikeCountByUserId() {
        UserLikeResultView userLikeResultView = userLikeRepository
                .getLikeResultAndLikeAndDislikeCountByUserId(1L, 2L);

        assertNotNull(userLikeResultView);
        assertFalse(userLikeResultView.getIsLike());
        assertEquals(1, userLikeResultView.getLikeCount());
        assertEquals(1, userLikeResultView.getDislikeCount());
    }

    @Test
    void testFindByUserIdAndUserOwnerId() {
        UserLike userLike = userLikeRepository.findByUserIdAndUserOwnerId(1L, 2L);

        assertNotNull(userLike);
        assertFalse(userLike.getIsLike());
    }

    @Test
    void testExistsByUserIdAndUserOwnerId() {
        boolean isExist = userLikeRepository.existsByUserIdAndUserOwnerId(1L, 2L);

        assertTrue(isExist);
    }

    @Test
    void testUpdateUserLike() {
        userLikeRepository.updateUserLike(true, 1L, 2L);
        UserLike userLike = userLikeRepository.findByUserIdAndUserOwnerId(1L, 2L);

        assertNotNull(userLike);
        assertTrue(userLike.getIsLike());
    }

    @Test
    void testDelete() {
        userLikeRepository.delete(1L, 2L);
        UserLike userLike = userLikeRepository.findByUserIdAndUserOwnerId(1L, 2L);

        assertNull(userLike);
    }

    private User saveUser() throws IOException {
        return saveTestEntity("user/user_1.json", User.class);
    }

}
