package by.nestegg.lending.repositoty;

import by.nestegg.lending.entity.RefreshToken;
import by.nestegg.lending.repository.RefreshTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RefreshTokenRepositoryTests extends AbstractRepositoryTests {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFindByToken() throws Exception {
        RefreshToken refreshToken = saveRefreshToken();

        RefreshToken entity = refreshTokenRepository.findByToken(refreshToken.getToken()).orElseThrow();

        assertEntry(refreshToken, entity);
    }

    @Test
    void testFindByUserId() throws Exception {
        RefreshToken refreshToken = saveRefreshToken();

        RefreshToken entity = refreshTokenRepository.findByUserId(1L).orElseThrow();

        assertEntry(refreshToken, entity);
    }

    @Test
    void testDeleteByExpireDate() throws Exception {
        RefreshToken refreshToken = saveRefreshToken();

        refreshTokenRepository.deleteByExpireDate(LocalDateTime.now());
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByToken(refreshToken.getToken());

        assertTrue(optionalRefreshToken.isEmpty());
    }

    private RefreshToken saveRefreshToken() throws IOException {
        return saveTestEntity("refresh-token/refresh_token.json", RefreshToken.class);
    }

    private void assertEntry(RefreshToken refreshToken, RefreshToken entity) {
        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertEquals(refreshToken.getToken(), entity.getToken());
        assertEquals(refreshToken.getExpireDate(), entity.getExpireDate());
        assertEquals(refreshToken.getUser().getId(), entity.getUser().getId());
    }

}
