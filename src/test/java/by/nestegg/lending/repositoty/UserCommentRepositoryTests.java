package by.nestegg.lending.repositoty;

import by.nestegg.lending.entity.UserComment;
import by.nestegg.lending.repository.UserCommentRepository;
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

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserCommentRepositoryTests extends AbstractRepositoryTests {

    @Autowired
    private UserCommentRepository userCommentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        List<UserComment> userComments = objectMapper.readValue(new ClassPathResource("user/user_comments.json")
                .getInputStream(), new TypeReference<>() {
        });
        UserComment userComment = saveTestEntity(userComments.get(0), UserComment.class);
        userComments.get(1).setParentUserComment(userComment);
        saveTestEntity(userComments.get(1), UserComment.class);
        saveTestEntity(userComments.get(2), UserComment.class);
    }

    @Test
    void testFindByUserId() {
        List<UserComment> userComments = userCommentRepository.findAllByUserId(1L);

        assertNotNull(userComments);
        assertEquals(2, userComments.size());
    }

    @Test
    void testUpdateUserCommentById() {
        String comment = UUID.randomUUID().toString();
        List<UserComment> userComments = userCommentRepository.findAllByUserId(1L);
        UserComment userComment = userComments.get(0);

        userCommentRepository.updateUserCommentById(userComment.getId(), comment);
        userComment = userCommentRepository.findById(userComment.getId()).orElseThrow();

        assertEquals(comment, userComment.getComment());
    }

}
