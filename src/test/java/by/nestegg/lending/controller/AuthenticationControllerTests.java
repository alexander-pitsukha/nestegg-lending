package by.nestegg.lending.controller;

import by.nestegg.lending.authencation.TokenResponse;
import by.nestegg.lending.authencation.TokenService;
import by.nestegg.lending.controller.request.AuthRequest;
import by.nestegg.lending.controller.request.PhoneRequest;
import by.nestegg.lending.dto.UserDto;
import by.nestegg.lending.exception.TokenValidationException;
import by.nestegg.lending.service.UserService;
import by.nestegg.lending.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@WithMockUser
class AuthenticationControllerTests extends BasicControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserCache userCache;

    @BeforeEach
    void setUp() throws Exception {
        super.setUp();
    }

    @Test
    void testLoginSocial() throws Exception {
        AuthRequest authRequest = getObjectFromJson("user/request/auth_facebook_request.json",
                AuthRequest.class);
        UserDto userDto = getObjectFromJson("user/user_profile.json", UserDto.class);
        TokenResponse tokenResponse = getObjectFromJson("token/token_response.json", TokenResponse.class);

        given(userService.createOrUpdateUserSocialData(any(AuthRequest.class))).willReturn(userDto);
        willDoNothing().given(userCache).removeUserFromCache(isA(String.class));
        given(tokenService.createTokenResponse(anyLong())).willReturn(tokenResponse);

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .content(asJsonString(authRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token_type", is(tokenResponse.getTokenType())))
                .andExpect(jsonPath("$.access_token", is(tokenResponse.getAccessToken())))
                .andExpect(jsonPath("$.expires_in").value(tokenResponse.getExpiresIn()))
                .andExpect(jsonPath("$.refresh_token", is(tokenResponse.getRefreshToken())));

        verify(userService).createOrUpdateUserSocialData(any(AuthRequest.class));
        verify(userCache).removeUserFromCache(anyString());
        verify(tokenService).createTokenResponse(anyLong());
    }

    @Test
    void testLoginPhone() throws Exception {
        PhoneRequest phoneRequest = getObjectFromJson("user/request/phone_request_1.json", PhoneRequest.class);
        String response = "1234";

        given(userService.createOrUpdateUserByPhoneNumber(anyString(), anyString())).willReturn(response);

        mockMvc.perform(post("/auth/login/phone")
                        .with(csrf())
                        .content(asJsonString(phoneRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(response));

        verify(userService).createOrUpdateUserByPhoneNumber(anyString(), anyString());
    }

    @Test
    void testVerifyPhoneCode() throws Exception {
        PhoneRequest phoneRequest = getObjectFromJson("user/request/phone_request_1.json", PhoneRequest.class);
        UserDto userDto = getObjectFromJson("user/user_profile.json", UserDto.class);
        TokenResponse tokenResponse = getObjectFromJson("token/token_response.json", TokenResponse.class);

        given(userService.updateUserByPhoneVerifyCode(any(PhoneRequest.class))).willReturn(userDto);
        given(tokenService.createTokenResponse(anyLong())).willReturn(tokenResponse);

        mockMvc.perform(put("/auth/login/phone/code")
                        .with(csrf())
                        .content(asJsonString(phoneRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token_type", is(tokenResponse.getTokenType())))
                .andExpect(jsonPath("$.access_token", is(tokenResponse.getAccessToken())))
                .andExpect(jsonPath("$.expires_in").value(tokenResponse.getExpiresIn()))
                .andExpect(jsonPath("$.refresh_token", is(tokenResponse.getRefreshToken())));

        verify(userService).updateUserByPhoneVerifyCode(any(PhoneRequest.class));
        verify(tokenService).createTokenResponse(anyLong());
    }

    @Test
    void testRefreshTokens() throws Exception {
        TokenResponse tokenResponse = getObjectFromJson("token/token_response.json", TokenResponse.class);

        given(tokenService.refreshTokens(anyString())).willReturn(tokenResponse);

        mockMvc.perform(get("/auth/" + Constants.REFRESH_TOKEN)
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(request -> {
                            request.setAttribute(Constants.TOKEN, UUID.randomUUID().toString());
                            return request;
                        }))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token_type", is(tokenResponse.getTokenType())))
                .andExpect(jsonPath("$.access_token", is(tokenResponse.getAccessToken())))
                .andExpect(jsonPath("$.expires_in").value(tokenResponse.getExpiresIn()))
                .andExpect(jsonPath("$.refresh_token", is(tokenResponse.getRefreshToken())));

        verify(tokenService).refreshTokens(anyString());
    }

    @Test
    void testHandleRuntimeExceptions() throws Exception {
        given(tokenService.refreshTokens(anyString())).willThrow(new BadCredentialsException(""),
                new TokenValidationException(""));

        mockMvc.perform(get("/auth/" + Constants.REFRESH_TOKEN)
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(request -> {
                            request.setAttribute(Constants.TOKEN, UUID.randomUUID().toString());
                            return request;
                        }))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        verify(tokenService).refreshTokens(anyString());
    }

}
