package by.nestegg.lending.controller;

import by.nestegg.lending.controller.request.AuthRequest;
import by.nestegg.lending.controller.request.PhoneRequest;
import by.nestegg.lending.authencation.TokenResponse;
import by.nestegg.lending.authencation.TokenService;
import by.nestegg.lending.exception.TokenValidationException;
import by.nestegg.lending.service.UserService;
import by.nestegg.lending.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "Authentication Controller")
@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final UserService userService;
    private final TokenService tokenService;
    private final UserCache userCache;

    @Operation(summary = "Login to Application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return access token and refresh token",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Device token cannot be empty<br>" +
                    "Social network token cannot be empty<br>Social network cannot be empty<br>" +
                    "Social network is incorrect", content = @Content)})
    @PostMapping("login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Validated AuthRequest authRequest) {
        var userDto = userService.createOrUpdateUserSocialData(authRequest);
        userCache.removeUserFromCache(userDto.getDeviceToken());
        return ResponseEntity.ok(tokenService.createTokenResponse(userDto.getId()));
    }

    @Operation(summary = "Login by Phone to Application")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return code (temporary)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class))}),
            @ApiResponse(responseCode = "400", description = "The phone number cannot be empty<br>" +
                    "The phone number is not in the correct format<br>Device token cannot be empty",
                    content = @Content)})
    @PostMapping("login/phone")
    public ResponseEntity<String> login(@RequestBody @Validated PhoneRequest phoneRequest) {
        String code = userService.createOrUpdateUserByPhoneNumber(phoneRequest.getPhoneNumber(),
                phoneRequest.getDeviceToken());
        return ResponseEntity.ok(code);
    }

    @Operation(summary = "Verify a phone code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return code (temporary)",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class))}),
            @ApiResponse(responseCode = "400", description = "The phone number cannot be empty<br>" +
                    "The phone number is not in the correct format<br>Device token cannot be empty<br>" +
                    "Phone verified code is empty", content = @Content),
            @ApiResponse(responseCode = "404", description = "User by phone not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Phone verified code is wrong", content = @Content)})
    @PutMapping("login/phone/code")
    public ResponseEntity<TokenResponse> verifyPhoneCode(@RequestBody @Validated PhoneRequest phoneRequest) {
        var userDto = userService.updateUserByPhoneVerifyCode(phoneRequest);
        return ResponseEntity.ok(tokenService.createTokenResponse(userDto.getId()));
    }

    @Operation(summary = "Refresh tokens by refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return access token and refresh token",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TokenResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Token expired", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)})
    @GetMapping(Constants.REFRESH_TOKEN)
    public ResponseEntity<TokenResponse> refreshTokens(HttpServletRequest request) {
        String refreshToken = (String) request.getAttribute(Constants.TOKEN);
        return ResponseEntity.ok(tokenService.refreshTokens(refreshToken));
    }

    @ExceptionHandler({BadCredentialsException.class, TokenValidationException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleRuntimeExceptions(RuntimeException e) {
        log.info(e.getClass().getName() + " handler.");
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

}
