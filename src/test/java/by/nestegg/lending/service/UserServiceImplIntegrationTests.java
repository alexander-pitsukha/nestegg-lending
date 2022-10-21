package by.nestegg.lending.service;

import by.nestegg.lending.BasicTests;
import by.nestegg.lending.NesteggLendingApplication;
import by.nestegg.lending.controller.request.AuthRequest;
import by.nestegg.lending.controller.request.PhoneRequest;
import by.nestegg.lending.controller.view.UserLikeResultView;
import by.nestegg.lending.dto.BankCardDto;
import by.nestegg.lending.dto.UserCommentDto;
import by.nestegg.lending.dto.UserDto;
import by.nestegg.lending.entity.UserComment;
import by.nestegg.lending.entity.UserLike;
import by.nestegg.lending.entity.UserSocialData;
import by.nestegg.lending.mapper.UserMapper;
import by.nestegg.lending.mapper.UserSocialDataMapper;
import by.nestegg.lending.repository.BankCardRepository;
import by.nestegg.lending.repository.BorrowingRequestRepository;
import by.nestegg.lending.repository.LendingRequestRepository;
import by.nestegg.lending.repository.UserCommentRepository;
import by.nestegg.lending.repository.UserLikeRepository;
import by.nestegg.lending.repository.UserRepository;
import by.nestegg.lending.repository.UserSocialDataRepository;
import by.nestegg.lending.service.enums.FacebookFields;
import by.nestegg.lending.service.enums.InstagramFields;
import by.nestegg.lending.service.enums.VkontakteFields;
import by.nestegg.lending.service.impl.UserServiceImpl;
import by.nestegg.lending.util.Constants;
import by.nestegg.lending.util.MessageCodeUtil;
import by.nestegg.lending.util.WithCustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = NesteggLendingApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class UserServiceImplIntegrationTests extends BasicTests {

    @Autowired
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private FacebookService facebookService;
    @Autowired
    private LinkedInService linkedInService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserSocialDataRepository userSocialDataRepository;
    @Autowired
    private BankCardRepository bankCardRepository;
    @Autowired
    private UserLikeRepository userLikeRepository;
    @Autowired
    private UserCommentRepository userCommentRepository;
    @Autowired
    private BorrowingRequestRepository borrowingRequestRepository;
    @Autowired
    private LendingRequestRepository lendingRequestRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserSocialDataMapper userSocialDataMapper;
    @Autowired
    private MessageCodeUtil messageCodeUtil;
    @Value("${vk.api.version}")
    private String vkApiVersion;

    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        @Autowired
        public UserService userService(FacebookService facebookService, LinkedInService linkedInService,
                                       InstagramService instagramService, VkontakteService vkontakteService,
                                       UserRepository userRepository, UserSocialDataRepository userSocialDataRepository,
                                       BankCardRepository bankCardRepository, UserLikeRepository userLikeRepository,
                                       UserCommentRepository userCommentRepository,
                                       BorrowingRequestRepository borrowingRequestRepository,
                                       LendingRequestRepository lendingRequestRepository, UserMapper userMapper,
                                       UserSocialDataMapper userSocialDataMapper,
                                       MessageCodeUtil messageCodeUtil) {
            return new UserServiceImpl(facebookService, linkedInService, instagramService, vkontakteService,
                    userRepository, userSocialDataRepository, bankCardRepository, userLikeRepository,
                    userCommentRepository, borrowingRequestRepository, lendingRequestRepository, userMapper,
                    userSocialDataMapper, messageCodeUtil);
        }
    }

    @Test
    void testCreateOrUpdateUserSocialDataFacebook() throws Exception {
        AuthRequest authRequest = getObjectFromJson("user/request/auth_facebook_request.json",
                AuthRequest.class);
        String uri = UriComponentsBuilder.fromUriString("")
                .pathSegment(Constants.ME)
                .queryParam(Constants.FIELDS, Stream.of(FacebookFields.ID, FacebookFields.FIRST_NAME,
                                FacebookFields.LAST_NAME, FacebookFields.EMAIL).map(FacebookFields::getField)
                        .collect(Collectors.joining(Constants.COMMA)))
                .queryParam(Constants.ACCESS_TOKEN, authRequest.getSocialToken())
                .build().toUriString();
        stubResponse(uri, "user/data/facebook_data.json");

        UserDto userDto = userService.createOrUpdateUserSocialData(authRequest);

        assertNotNull(userDto);
        assertNotNull(userDto.getId());
        assertNotNull(userDto.getFacebookId());
        assertFalse(userDto.getPushActive());
        assertFalse(userDto.getAutomaticDebiting());
        assertEquals(authRequest.getDeviceToken(), userDto.getDeviceToken());
    }

    @Test
    void testCreateOrUpdateUserSocialDataLinkedIn() throws Exception {
        AuthRequest authRequest = getObjectFromJson("user/request/auth_linkedin_request.json",
                AuthRequest.class);
        String uri = UriComponentsBuilder.fromUriString("")
                .pathSegment(Constants.ME)
                .build().toUriString();
        stubResponse(uri, "user/data/linkedin_data.json", authRequest.getSocialToken());

        UserDto userDto = userService.createOrUpdateUserSocialData(authRequest);

        assertNotNull(userDto);
        assertNotNull(userDto.getId());
        assertNotNull(userDto.getLinkedinId());
        assertFalse(userDto.getPushActive());
        assertFalse(userDto.getAutomaticDebiting());
        assertEquals(authRequest.getDeviceToken(), userDto.getDeviceToken());
    }

    @Test
    void testCreateOrUpdateUserSocialDataInstagram() throws Exception {
        AuthRequest authRequest = getObjectFromJson("user/request/auth_instagram_request.json",
                AuthRequest.class);
        String uri = UriComponentsBuilder.fromUriString("")
                .pathSegment(Constants.ME)
                .queryParam(Constants.FIELDS, Stream.of(InstagramFields.ID, InstagramFields.USERNAME,
                                InstagramFields.EMAIL).map(InstagramFields::getField)
                        .collect(Collectors.joining(Constants.COMMA)))
                .queryParam(Constants.ACCESS_TOKEN, authRequest.getSocialToken())
                .build().toUriString();
        stubResponse(uri, "user/data/instagram_data.json");

        UserDto userDto = userService.createOrUpdateUserSocialData(authRequest);

        assertNotNull(userDto);
        assertNotNull(userDto.getId());
        assertNotNull(userDto.getInstagramId());
        assertFalse(userDto.getPushActive());
        assertFalse(userDto.getAutomaticDebiting());
        assertEquals(authRequest.getDeviceToken(), userDto.getDeviceToken());
    }

    @Test
    void testCreateOrUpdateUserSocialDataVkontakte() throws Exception {
        AuthRequest authRequest = getObjectFromJson("user/request/auth_vkontakte_request.json",
                AuthRequest.class);
        String uri = UriComponentsBuilder.fromUriString("")
                .pathSegment("account.getProfileInfo")
                .queryParam(Constants.FIELDS, Stream.of(VkontakteFields.ID, VkontakteFields.FIRST_NAME,
                                VkontakteFields.LAST_NAME, VkontakteFields.EMAIL).map(VkontakteFields::getField)
                        .collect(Collectors.joining(Constants.COMMA)))
                .queryParam(Constants.ACCESS_TOKEN, authRequest.getSocialToken())
                .queryParam("v", vkApiVersion)
                .build().toUriString();
        stubResponse(uri, "user/data/vkontakte_data.json");

        UserDto userDto = userService.createOrUpdateUserSocialData(authRequest);

        assertNotNull(userDto);
        assertNotNull(userDto.getId());
        assertNotNull(userDto.getVkontakteId());
        assertFalse(userDto.getPushActive());
        assertFalse(userDto.getAutomaticDebiting());
        assertEquals(authRequest.getDeviceToken(), userDto.getDeviceToken());
    }

    @ParameterizedTest
    @ValueSource(strings = {"user/request/phone_request_1.json", "user/request/phone_request_2.json",
            "user/request/phone_request_3.json"})
    void testCreateOrUpdateUserByPhoneNumber(String fileSource) throws Exception {
        PhoneRequest phoneRequest = getObjectFromJson(fileSource, PhoneRequest.class);

        String verifyCode = userService.createOrUpdateUserByPhoneNumber(phoneRequest.getPhoneNumber(),
                phoneRequest.getDeviceToken());

        assertNotNull(verifyCode);
        assertEquals(4, verifyCode.length());
    }

    @Test
    void testUpdateUserByPhoneVerifyCode() throws Exception {
        PhoneRequest phoneRequest = getObjectFromJson("user/request/phone_request_verify_code.json",
                PhoneRequest.class);

        UserDto userDto = userService.updateUserByPhoneVerifyCode(phoneRequest);

        assertNotNull(userDto);
        assertNotNull(userDto.getId());
        assertEquals(phoneRequest.getDeviceToken(), userDto.getDeviceToken());
    }

    @Test
    @WithCustomUserDetails
    void testUpdateUserProfile() throws Exception {
        UserDto userDtoFromJson = getObjectFromJson("user/user_profile.json", UserDto.class);
        List<BankCardDto> bankCardDtosFromJson = userDtoFromJson.getBankCardDtos();

        UserDto userDto = userService.updateUserProfile(userDtoFromJson);

        assertNotNull(userDto);
        assertEquals(userDtoFromJson.getId(), userDto.getId());
        assertEquals(userDtoFromJson.getNickname(), userDto.getNickname());
        assertEquals(userDtoFromJson.getPushActive(), userDto.getPushActive());
        assertEquals(userDtoFromJson.getAutomaticDebiting(), userDto.getAutomaticDebiting());
        assertEquals(userDtoFromJson.getAvatarId(), userDto.getAvatarId());
        assertEquals(2, userDto.getBankCardDtos().size());
        int i = 0;
        for (BankCardDto bankCardDto : userDto.getBankCardDtos()) {
            assertNotNull(bankCardDto.getId());
            assertEquals(bankCardDtosFromJson.get(i).getCardNumber(), bankCardDto.getCardNumber());
            assertEquals(bankCardDtosFromJson.get(i).getCvv(), bankCardDto.getCvv());
            assertEquals(bankCardDtosFromJson.get(i).getExpireDate(), bankCardDto.getExpireDate());
            assertEquals(bankCardDtosFromJson.get(i).getCardholderName(), bankCardDto.getCardholderName());
            assertEquals(bankCardDtosFromJson.get(i).getIsDefault() != null
                    ? bankCardDtosFromJson.get(i).getIsDefault() : false, bankCardDto.getIsDefault());
            i++;
        }
    }

    @Test
    void testRemoveFacebook() throws Exception {
        UserSocialData userSocialData = getObjectFromJson("user/data/user_social_data.json",
                UserSocialData.class);
        userSocialData = userSocialDataRepository.save(userSocialData);

        userService.removeFacebook(userSocialData.getUser().getId());

        UserSocialData entity = userSocialDataRepository.findByUserId(userSocialData.getUser().getId());

        assertNull(entity.getFacebookId());
        assertNull(entity.getFacebookToken());
        assertNull(entity.getFacebookData());
    }

    @Test
    void testRemoveLinkedIn() throws Exception {
        UserSocialData userSocialData = getObjectFromJson("user/data/user_social_data.json",
                UserSocialData.class);
        userSocialData = userSocialDataRepository.save(userSocialData);

        userService.removeLinkedIn(userSocialData.getUser().getId());

        UserSocialData entity = userSocialDataRepository.findByUserId(userSocialData.getUser().getId());

        assertNull(entity.getLinkedinId());
        assertNull(entity.getLinkedinToken());
        assertNull(entity.getLinkedinData());
    }

    @Test
    void testRemoveInstagram() throws Exception {
        UserSocialData userSocialData = getObjectFromJson("user/data/user_social_data.json",
                UserSocialData.class);
        userSocialData = userSocialDataRepository.save(userSocialData);

        userService.removeInstagram(userSocialData.getUser().getId());

        UserSocialData entity = userSocialDataRepository.findByUserId(userSocialData.getUser().getId());

        assertNull(entity.getInstagramId());
        assertNull(entity.getInstagramToken());
        assertNull(entity.getInstagramData());
    }

    @Test
    void testRemoveVkontakte() throws Exception {
        UserSocialData userSocialData = getObjectFromJson("user/data/user_social_data.json",
                UserSocialData.class);
        userSocialData = userSocialDataRepository.save(userSocialData);

        userService.removeVkontakte(userSocialData.getUser().getId());

        UserSocialData entity = userSocialDataRepository.findByUserId(userSocialData.getUser().getId());

        assertNull(entity.getVkontakteId());
        assertNull(entity.getVkontakteToken());
        assertNull(entity.getVkontakteData());
    }

    @ParameterizedTest
    @MethodSource("provideSaveUserLike")
    void testSaveUserLike(boolean isNew, Boolean isLike, Long userId, Long userOwnerId) throws Exception {
        if (!isNew) {
            UserLike userLike = getObjectFromJson("user/like.json", UserLike.class);
            userLikeRepository.save(userLike);
        }

        userService.saveUserLike(isLike, userId, userOwnerId);

        UserLikeResultView userLikeResultView = userService.getUserLikeResultView(userId, userOwnerId);

        assertEquals(isLike, userLikeResultView.getIsLike());
    }

    @Test
    void testDeleteUserLike() throws Exception {
        UserLike userLike = getObjectFromJson("user/like.json", UserLike.class);
        userLikeRepository.save(userLike);

        userService.deleteUserLike(1L, 2L);

        boolean result = userLikeRepository.existsByUserIdAndUserOwnerId(1L, 2L);

        assertFalse(result);
    }

    @Test
    void testSaveUserComment() throws Exception {
        UserComment userComment = getObjectFromJson("user/user_comment_1.json", UserComment.class);

        UserCommentDto userCommentDto = userService.saveUserComment(userMapper.toDto(userComment));

        assertNotNull(userCommentDto);
        assertNotNull(userCommentDto.getId());
        assertEquals(userComment.getComment(), userCommentDto.getComment());
    }

    @Test
    void testUpdateUserComment() throws Exception {
        UserComment userComment = getObjectFromJson("user/user_comment_1.json", UserComment.class);
        userComment = userCommentRepository.save(userComment);
        String comment = UUID.randomUUID().toString();

        UserCommentDto userCommentDto = userService.updateUserComment(userComment.getId(), comment);

        assertNotNull(userCommentDto);
        assertNotNull(userCommentDto.getId());
        assertEquals(userComment.getId(), userCommentDto.getId());
        assertEquals(comment, userCommentDto.getComment());
    }

    @Test
    void testDeleteUserComment() throws Exception {
        UserComment userComment = getObjectFromJson("user/user_comment_1.json", UserComment.class);
        userComment = userCommentRepository.save(userComment);

        userService.deleteUserComment(userComment.getId());

        boolean result = userCommentRepository.existsById(userComment.getId());

        assertFalse(result);
    }

    private void stubResponse(String uri, String responseResource) throws IOException {
        String response = FileUtils.readFileToString(new ClassPathResource(responseResource).getFile(),
                StandardCharsets.UTF_8);
        stubFor(get(urlEqualTo(uri))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(response)));
    }

    private void stubResponse(String uri, String responseResource, String token) throws IOException {
        String response = FileUtils.readFileToString(new ClassPathResource(responseResource).getFile(),
                StandardCharsets.UTF_8);
        stubFor(get(urlEqualTo(uri)).withHeader(HttpHeaders.CONTENT_TYPE, equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withHeader(HttpHeaders.AUTHORIZATION, equalTo(Constants.BEARER + Constants.SPACE + token))
                .willReturn(aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(response)));
    }

    private static Stream<Arguments> provideSaveUserLike() {
        return Stream.of(
                Arguments.of(false, Boolean.TRUE, 1L, 2L),
                Arguments.of(true, Boolean.FALSE, 1L, 2L)
        );
    }

}
