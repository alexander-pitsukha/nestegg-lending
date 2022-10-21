package by.nestegg.lending.service;

import by.nestegg.lending.AbstractTests;
import by.nestegg.lending.configuration.MessageSourceConfiguration;
import by.nestegg.lending.controller.request.AuthRequest;
import by.nestegg.lending.controller.request.PhoneRequest;
import by.nestegg.lending.controller.view.LikeCountView;
import by.nestegg.lending.controller.view.UserLikeResultView;
import by.nestegg.lending.dto.UserCommentDto;
import by.nestegg.lending.dto.UserDataDto;
import by.nestegg.lending.dto.UserDto;
import by.nestegg.lending.dto.enums.UserType;
import by.nestegg.lending.entity.User;
import by.nestegg.lending.entity.UserComment;
import by.nestegg.lending.exception.ServiceException;
import by.nestegg.lending.exception.SocialException;
import by.nestegg.lending.exception.UserNotFoundException;
import by.nestegg.lending.mapper.UserMapper;
import by.nestegg.lending.mapper.UserSocialDataMapper;
import by.nestegg.lending.repository.BankCardRepository;
import by.nestegg.lending.repository.BorrowingRequestRepository;
import by.nestegg.lending.repository.LendingRequestRepository;
import by.nestegg.lending.repository.UserCommentRepository;
import by.nestegg.lending.repository.UserLikeRepository;
import by.nestegg.lending.repository.UserRepository;
import by.nestegg.lending.repository.UserSocialDataRepository;
import by.nestegg.lending.service.impl.UserServiceImpl;
import by.nestegg.lending.util.Constants;
import by.nestegg.lending.util.MessageCodeUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@MockBean({FacebookService.class, LinkedInService.class, InstagramService.class, VkontakteService.class,
        UserRepository.class, UserSocialDataRepository.class, BankCardRepository.class, UserLikeRepository.class,
        UserCommentRepository.class, BorrowingRequestRepository.class, LendingRequestRepository.class, UserMapper.class,
        UserSocialDataMapper.class})
@Import(MessageSourceConfiguration.class)
class UserServiceImplTests extends AbstractTests {

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
    void testCreateOrUpdateUserSocialData_SocialException() throws Exception {
        AuthRequest authRequest = getObjectFromJson("user/request/phone_request_1.json", AuthRequest.class);
        authRequest.setSocial(UUID.randomUUID().toString());

        SocialException exception = assertThrows(SocialException.class, () -> {
            userService.createOrUpdateUserSocialData(authRequest);
        });
        assertEquals(messageCodeUtil.getFullErrorMessageByBundleCode("error.message.auth.social.wrong"),
                exception.getMessage());
    }

    @Test
    void testUpdateUserByPhoneVerifyCode_UserNotFoundException() throws Exception {
        PhoneRequest phoneRequest = getObjectFromJson("user/request/phone_request_1.json", PhoneRequest.class);

        when(userRepository.findByPhoneNumber(phoneRequest.getPhoneNumber())).thenReturn(null);

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUserByPhoneVerifyCode(phoneRequest);
        });
        assertEquals(messageCodeUtil.getFullErrorMessageByBundleCode(
                "error.message.auth.user.by.phone.not.found",
                new Object[]{phoneRequest.getPhoneNumber()}), exception.getMessage());

        verify(userRepository).findByPhoneNumber(phoneRequest.getPhoneNumber());
    }

    @Test
    void testUpdateUserByPhoneVerifyCode_ServiceException_1() throws Exception {
        PhoneRequest phoneRequest = getObjectFromJson("user/request/phone_request_1.json", PhoneRequest.class);
        User user = getObjectFromJson("user/user_1.json", User.class);
        phoneRequest.setVerifyCode(UUID.randomUUID().toString());

        when(userRepository.findByPhoneNumber(phoneRequest.getPhoneNumber())).thenReturn(user);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.updateUserByPhoneVerifyCode(phoneRequest);
        });
        assertEquals(messageCodeUtil.getFullErrorMessageByBundleCode(
                "error.message.auth.phone.verified.code.wrong",
                new Object[]{phoneRequest.getVerifyCode()}), exception.getMessage());

        verify(userRepository).findByPhoneNumber(phoneRequest.getPhoneNumber());
    }

    @Test
    void testUpdateUserByPhoneVerifyCode_ServiceException_2() throws Exception {
        PhoneRequest phoneRequest = getObjectFromJson("user/request/phone_request_1.json", PhoneRequest.class);
        User user = getObjectFromJson("user/user_1.json", User.class);
        phoneRequest.setVerifyCode("");

        when(userRepository.findByPhoneNumber(phoneRequest.getPhoneNumber())).thenReturn(user);

        ServiceException exception = assertThrows(ServiceException.class, () -> {
            userService.updateUserByPhoneVerifyCode(phoneRequest);
        });
        assertEquals(messageCodeUtil.getFullErrorMessageByBundleCode(
                "error.message.auth.phone.verified.code.empty",
                new Object[]{phoneRequest.getVerifyCode()}), exception.getMessage());

        verify(userRepository).findByPhoneNumber(phoneRequest.getPhoneNumber());
    }

    @Test
    void testUpdateUserProfile_UserNotFoundException() throws Exception {
        UserDto userDto = getObjectFromJson("user/user_dto.json", UserDto.class);

        when(userRepository.findById(userDto.getId())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.updateUserProfile(userDto);
        });
        assertEquals(messageCodeUtil.getFullErrorMessageByBundleCode(
                Constants.ERROR_MESSAGE_USER_BY_ID_NOT_FOUND, new Object[]{userDto.getId()}), exception.getMessage());

        verify(userRepository).findById(userDto.getId());
    }

    @Test
    void testGetUserProfile() throws Exception {
        User user = getObjectFromJson("user/user_2.json", User.class);
        UserDto userDto = getObjectFromJson("user/user_dto.json", UserDto.class);

        when(userRepository.findByDeviceToken(any(String.class))).thenReturn(user);
        when(userMapper.toDto(any(User.class))).thenReturn(userDto);

        userDto = userService.getUserProfile(UUID.randomUUID().toString());

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getNickname(), userDto.getNickname());
        assertEquals(user.getDeviceToken(), userDto.getDeviceToken());
        assertEquals(user.getPushActive(), userDto.getPushActive());
        assertEquals(user.getAutomaticDebiting(), userDto.getAutomaticDebiting());
        assertEquals(user.getAvatarId(), userDto.getAvatarId());
        assertEquals(user.getBankCards().size(), userDto.getBankCardDtos().size());

        verify(userRepository).findByDeviceToken(any(String.class));
        verify(userMapper).toDto(any(User.class));
    }

    @ParameterizedTest
    @MethodSource("provideUserSocialData")
    void testGetUserData(long userId, long userOwnerId, boolean isBorrowingRequestExist, boolean isLendingRequestExist,
                         int lendingTimes, UserType userType, int likeTimes, int userLikeTimes) throws Exception {
        User user = getObjectFromJson("user/user_2.json", User.class);
        LikeCountView likeCountView = getLikeCountView();
        UserLikeResultView userLikeResultView = getUserLikeResultView();
        List<UserComment> userComments = objectMapper.readValue(new ClassPathResource("user/user_comments.json")
                .getInputStream(), new TypeReference<>() {
        });
        List<UserCommentDto> userCommentDtos = objectMapper.readValue(
                new ClassPathResource("user/user_comment_dtos.json").getInputStream(), new TypeReference<>() {
                });

        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(borrowingRequestRepository.existsByUserId(anyLong()))
                .thenReturn(isBorrowingRequestExist);
        when(lendingRequestRepository.existsByUserId(anyLong()))
                .thenReturn(isLendingRequestExist);
        when(userLikeRepository.getLikeAndDislikeCountByUserId(anyLong())).thenReturn(likeCountView);
        when(userLikeRepository.getLikeResultAndLikeAndDislikeCountByUserId(anyLong(), anyLong()))
                .thenReturn(userLikeResultView);
        when(userCommentRepository.findAllByUserId(anyLong())).thenReturn(userComments);
        when(userMapper.toDto(userComments)).thenReturn(userCommentDtos);

        UserDataDto userDataDto = userService.getUserData(userId, userOwnerId);

        assertEquals(user.getId(), userDataDto.getId());
        assertEquals(user.getNickname(), userDataDto.getNickname());
        assertEquals(user.getAvatarId(), userDataDto.getAvatarId());
        assertEquals(userType, userDataDto.getUserType());
        if (Objects.equals(userId, userOwnerId)) {
            assertEquals(likeCountView.getLikeCount(), userDataDto.getLikeCountView().getLikeCount());
            assertEquals(likeCountView.getDislikeCount(), userDataDto.getLikeCountView().getDislikeCount());
        } else {
            assertEquals(userLikeResultView.getIsLike(), userDataDto.getUserLikeResultView().getIsLike());
            assertEquals(userLikeResultView.getLikeCount(), userDataDto.getUserLikeResultView().getLikeCount());
            assertEquals(userLikeResultView.getDislikeCount(), userDataDto.getUserLikeResultView().getDislikeCount());
        }
        assertEquals(3, userDataDto.getUserCommentDtos().size());

        verify(userRepository).findById(anyLong());
        verify(borrowingRequestRepository).existsByUserId(anyLong());
        verify(lendingRequestRepository, times(lendingTimes)).existsByUserId(anyLong());
        verify(userLikeRepository, times(likeTimes)).getLikeAndDislikeCountByUserId(anyLong());
        verify(userLikeRepository, times(userLikeTimes)).getLikeResultAndLikeAndDislikeCountByUserId(anyLong(),
                anyLong());
        verify(userCommentRepository).findAllByUserId(anyLong());
        verify(userMapper).toDto(anyList());
    }

    @Test
    void testGetUserData() {
        long userId = 1;

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.getUserData(userId, 2L);
        });
        assertEquals(messageCodeUtil.getFullErrorMessageByBundleCode(Constants.ERROR_MESSAGE_USER_BY_ID_NOT_FOUND,
                new Object[]{userId}), exception.getMessage());

        verify(userRepository).findById(anyLong());
    }

    @Test
    void testGetLikeCountView() {
        LikeCountView likeCountView = getLikeCountView();

        when(userLikeRepository.getLikeAndDislikeCountByUserId(anyLong())).thenReturn(likeCountView);

        LikeCountView result = userService.getLikeCountView(1L);

        assertEquals(likeCountView.getLikeCount(), result.getLikeCount());
        assertEquals(likeCountView.getDislikeCount(), result.getDislikeCount());

        verify(userLikeRepository).getLikeAndDislikeCountByUserId(anyLong());
    }

    @Test
    void testGetUserLikeResultView() throws Exception {
        UserLikeResultView userLikeResultView = getUserLikeResultView();

        when(userLikeRepository.getLikeResultAndLikeAndDislikeCountByUserId(anyLong(), anyLong()))
                .thenReturn(userLikeResultView);

        UserLikeResultView result = userService.getUserLikeResultView(1L, 2L);

        assertEquals(userLikeResultView.getIsLike(), result.getIsLike());
        assertEquals(userLikeResultView.getLikeCount(), result.getLikeCount());
        assertEquals(userLikeResultView.getDislikeCount(), result.getDislikeCount());

        verify(userLikeRepository).getLikeResultAndLikeAndDislikeCountByUserId(anyLong(), anyLong());
    }

    @Test
    void testSaveUserComment_UserNotFoundException_1() throws Exception {
        UserCommentDto userCommentDto = getObjectFromJson("user/user_comment_dto.json", UserCommentDto.class);

        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.saveUserComment(userCommentDto);
        });
        assertEquals(messageCodeUtil.getFullErrorMessageByBundleCode(
                        Constants.ERROR_MESSAGE_USER_BY_ID_NOT_FOUND, new Object[]{userCommentDto.getUserId()}),
                exception.getMessage());

        verify(userRepository).findById(anyLong());
    }

    @Test
    void testSaveUserComment_UserNotFoundException_2() throws Exception {
        UserCommentDto userCommentDto = getObjectFromJson("user/user_comment_dto.json", UserCommentDto.class);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()), Optional.empty());

        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            userService.saveUserComment(userCommentDto);
        });
        assertEquals(messageCodeUtil.getFullErrorMessageByBundleCode(
                        Constants.ERROR_MESSAGE_USER_BY_ID_NOT_FOUND, new Object[]{userCommentDto.getUserOwnerId()}),
                exception.getMessage());

        verify(userRepository, times(2)).findById(anyLong());
    }

    private static Stream<Arguments> provideUserSocialData() {
        return Stream.of(
                Arguments.of(1L, 2L, Boolean.TRUE, Boolean.FALSE, 0, UserType.BORROWER, 0, 1),
                Arguments.of(1L, 2L, Boolean.FALSE, Boolean.TRUE, 1, UserType.LENDER, 0, 1),
                Arguments.of(1L, 1L, Boolean.TRUE, Boolean.FALSE, 0, UserType.BORROWER, 1, 0),
                Arguments.of(1L, 1L, Boolean.FALSE, Boolean.TRUE, 1, UserType.LENDER, 1, 0)
        );
    }

    private LikeCountView getLikeCountView() {
        return new LikeCountView() {

            @Override
            public int getLikeCount() {
                return 100;
            }

            @Override
            public int getDislikeCount() {
                return 100;
            }
        };
    }

    private UserLikeResultView getUserLikeResultView() {
        return new UserLikeResultView() {

            @Override
            public Boolean getIsLike() {
                return Boolean.TRUE;
            }

            @Override
            public int getLikeCount() {
                return 100;
            }

            @Override
            public int getDislikeCount() {
                return 100;
            }
        };
    }

}
