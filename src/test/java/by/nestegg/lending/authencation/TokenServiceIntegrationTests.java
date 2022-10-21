package by.nestegg.lending.authencation;

import by.nestegg.lending.BasicTests;
import by.nestegg.lending.NesteggLendingApplication;
import by.nestegg.lending.util.WithCustomUserDetails;
import by.nestegg.lending.authencation.jwt.JwtTokenUtil;
import by.nestegg.lending.entity.RefreshToken;
import by.nestegg.lending.entity.User;
import by.nestegg.lending.repository.RefreshTokenRepository;
import by.nestegg.lending.repository.UserRepository;
import by.nestegg.lending.util.Constants;
import by.nestegg.lending.util.MessageCodeUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.data.domain.AuditorAware;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NesteggLendingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class TokenServiceIntegrationTests extends BasicTests {

    @Autowired
    private TokenServiceImpl tokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private AuditorAware<User> auditorAware;
    @Autowired
    private MessageCodeUtil messageCodeUtil;

    @Test
    void testCreateTokenResponse() {
        TokenResponse tokenResponse = tokenService.createTokenResponse(1L);

        assertNotNull(tokenResponse);
        assertNotNull(tokenResponse.getTokenType());
        assertNotNull(tokenResponse.getAccessToken());
        assertNotNull(tokenResponse.getExpiresIn());
        assertNotNull(tokenResponse.getRefreshToken());
        assertEquals(Constants.BEARER, tokenResponse.getTokenType());
    }

    @Test
    @WithCustomUserDetails
    void testRefreshTokens() throws Exception {
        RefreshToken refreshToken = getObjectFromJson("refresh-token/refresh_token.json", RefreshToken.class);
        refreshToken = refreshTokenRepository.save(refreshToken);

        TokenResponse tokenResponse = tokenService.refreshTokens(refreshToken.getToken());

        assertNotNull(tokenResponse);
        assertNotNull(tokenResponse.getAccessToken());
        assertNotNull(tokenResponse.getExpiresIn());
        assertNotNull(tokenResponse.getRefreshToken());
        assertEquals(Constants.BEARER, tokenResponse.getTokenType());
        assertNotEquals(refreshToken.getToken(), tokenResponse.getRefreshToken());
    }

    @Test
    void testDeleteRefreshTokensByExpireDate() throws Exception {
        RefreshToken refreshToken = getObjectFromJson("refresh-token/refresh_token.json", RefreshToken.class);
        refreshTokenRepository.save(refreshToken);

        tokenService.deleteRefreshTokensByExpireDate();
        Optional<RefreshToken> tokenOptional = refreshTokenRepository.findByToken(refreshToken.getToken());

        assertTrue(tokenOptional.isEmpty());
    }

}
