package by.nestegg.lending.authencation;

public interface TokenService {

    TokenResponse createTokenResponse(Long userId);

    TokenResponse refreshTokens(String token);

}
