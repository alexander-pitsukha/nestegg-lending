package by.nestegg.lending.service.impl;

import by.nestegg.lending.controller.request.AuthRequest;
import by.nestegg.lending.controller.request.PhoneRequest;
import by.nestegg.lending.controller.view.LikeCountView;
import by.nestegg.lending.controller.view.UserLikeResultView;
import by.nestegg.lending.dto.UserCommentDto;
import by.nestegg.lending.dto.UserDataDto;
import by.nestegg.lending.dto.UserDto;
import by.nestegg.lending.dto.enums.UserType;
import by.nestegg.lending.entity.BankCard;
import by.nestegg.lending.entity.UserComment;
import by.nestegg.lending.entity.UserLike;
import by.nestegg.lending.entity.UserSocialData;
import by.nestegg.lending.entity.User;
import by.nestegg.lending.entity.enums.Role;
import by.nestegg.lending.mapper.UserSocialDataMapper;
import by.nestegg.lending.repository.BankCardRepository;
import by.nestegg.lending.repository.BorrowingRequestRepository;
import by.nestegg.lending.repository.LendingRequestRepository;
import by.nestegg.lending.repository.UserCommentRepository;
import by.nestegg.lending.repository.UserLikeRepository;
import by.nestegg.lending.service.InstagramService;
import by.nestegg.lending.service.VkontakteService;
import by.nestegg.lending.service.enums.SocialNetwork;
import by.nestegg.lending.exception.SocialException;
import by.nestegg.lending.exception.ServiceException;
import by.nestegg.lending.exception.UserNotFoundException;
import by.nestegg.lending.mapper.UserMapper;
import by.nestegg.lending.repository.UserSocialDataRepository;
import by.nestegg.lending.repository.UserRepository;
import by.nestegg.lending.service.FacebookService;
import by.nestegg.lending.service.LinkedInService;
import by.nestegg.lending.service.UserService;
import by.nestegg.lending.util.Constants;
import by.nestegg.lending.util.MessageCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final FacebookService facebookService;
    private final LinkedInService linkedInService;
    private final InstagramService instagramService;
    private final VkontakteService vkontakteService;
    private final UserRepository userRepository;
    private final UserSocialDataRepository userSocialDataRepository;
    private final BankCardRepository bankCardRepository;
    private final UserLikeRepository userLikeRepository;
    private final UserCommentRepository userCommentRepository;
    private final BorrowingRequestRepository borrowingRequestRepository;
    private final LendingRequestRepository lendingRequestRepository;
    private final UserMapper userMapper;
    private final UserSocialDataMapper userSocialDataMapper;
    private final MessageCodeUtil messageCodeUtil;

    @Override
    @Transactional
    public UserDto createOrUpdateUserSocialData(AuthRequest authRequest) {
        var user = userRepository.findByDeviceToken(authRequest.getDeviceToken());
        UserSocialData userSocialData;
        if (Objects.equals(authRequest.getSocial(), SocialNetwork.FACEBOOK.getName())) {
            userSocialData = getFacebookUserSocialData(authRequest, user);
        } else if (Objects.equals(authRequest.getSocial(), SocialNetwork.LINKEDIN.getName())) {
            userSocialData = getLinkedInUserSocialData(authRequest, user);
        } else if (Objects.equals(authRequest.getSocial(), SocialNetwork.INSTAGRAM.getName())) {
            userSocialData = getInstagramUserSocialData(authRequest, user);
        } else if (Objects.equals(authRequest.getSocial(), SocialNetwork.VKONTAKTE.getName())) {
            userSocialData = getVkontakteUserSocialData(authRequest, user);
        } else {
            throw new SocialException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    "error.message.auth.social.wrong"));
        }
        if (user == null) {
            user = Optional.ofNullable(userSocialData.getUser()).orElseGet(() -> createUser(authRequest));
            user.setDeviceToken(authRequest.getDeviceToken());
        }
        user.setUserSocialData(userSocialData);
        userSocialData.setUser(user);
        return userMapper.toDto(userRepository.saveAndFlush(user));
    }

    @Override
    @Transactional
    public String createOrUpdateUserByPhoneNumber(String phoneNumber, String deviceToken) {
        Optional<User> optionalUser = Optional.ofNullable(userRepository.findByPhoneNumber(phoneNumber));
        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            if (user.getDeviceToken() == null || !Objects.equals(user.getDeviceToken(), deviceToken)) {
                user.setDeviceToken(deviceToken);
            }
        } else {
            optionalUser = Optional.ofNullable(userRepository.findByDeviceToken(deviceToken));
            if (optionalUser.isPresent()) {
                user = optionalUser.get();
                if (user.getPhoneNumber() == null || !Objects.equals(user.getPhoneNumber(), phoneNumber)) {
                    user.setPhoneNumber(phoneNumber);
                }
            } else {
                user = createUser(phoneNumber, deviceToken);
            }
        }
        //user.setVerifiedPhoneCode(generatePhoneVerifiedNumber());
        user.setVerifiedPhoneCode(phoneNumber.substring(phoneNumber.length() - 4));
        user = userRepository.saveAndFlush(user);
        return user.getVerifiedPhoneCode();
    }

    @Override
    @Transactional
    public UserDto updateUserByPhoneVerifyCode(PhoneRequest phoneRequest) {
        var user = Optional.ofNullable(userRepository.findByPhoneNumber(phoneRequest.getPhoneNumber()))
                .orElseThrow(() -> new UserNotFoundException(messageCodeUtil.getFullErrorMessageByBundleCode(
                        "error.message.auth.user.by.phone.not.found",
                        new Object[]{phoneRequest.getPhoneNumber()})));
        if (StringUtils.hasLength(phoneRequest.getVerifyCode())) {
            if (Objects.equals(user.getVerifiedPhoneCode(), phoneRequest.getVerifyCode())) {
                user.setVerifiedPhoneCode(null);
            } else {
                throw new ServiceException(messageCodeUtil.getFullErrorMessageByBundleCode(
                        "error.message.auth.phone.verified.code.wrong",
                        new Object[]{phoneRequest.getVerifyCode()}));
            }
        } else {
            throw new ServiceException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    "error.message.auth.phone.verified.code.empty",
                    new Object[]{phoneRequest.getVerifyCode()}));
        }
        return userMapper.toDto(user);
    }

    @Override
    @Transactional
    public UserDto updateUserProfile(UserDto userDto) {
        var user = userRepository.findById(userDto.getId()).orElseThrow(() -> new UserNotFoundException(
                messageCodeUtil.getFullErrorMessageByBundleCode(
                        Constants.ERROR_MESSAGE_USER_BY_ID_NOT_FOUND, new Object[]{userDto.getId()})));
        user.setNickname(userDto.getNickname());
        user.setPushActive(userDto.getPushActive());
        user.setAutomaticDebiting(userDto.getAutomaticDebiting());
        user.setAvatarId(userDto.getAvatarId());
        if (userDto.getBankCardDtos().isEmpty()) {
            user.setBankCards(userMapper.toEntity(userDto.getBankCardDtos()));
        } else {
            List<BankCard> bankCards = user.getBankCards();
            bankCards.clear();
            userDto.getBankCardDtos().forEach(bankCardDto -> {
                BankCard bankCard = bankCardDto.getId() == null ? userMapper.toEntity(bankCardDto)
                        : bankCardRepository.getById(bankCardDto.getId());
                bankCard.setUser(user);
                bankCards.add(bankCard);
            });
        }
        return userMapper.toDto(userRepository.saveAndFlush(user));
    }

    @Override
    public UserDto getUserProfile(String deviceToken) {
        return userMapper.toDto(userRepository.findByDeviceToken(deviceToken));
    }

    @Override
    @Transactional
    public void removeFacebook(Long userId) {
        userSocialDataRepository.updateFacebook(userId);
    }

    @Override
    @Transactional
    public void removeLinkedIn(Long userId) {
        userSocialDataRepository.updateLinkedIn(userId);
    }

    @Override
    @Transactional
    public void removeInstagram(Long userId) {
        userSocialDataRepository.updateInstagram(userId);
    }

    @Override
    @Transactional
    public void removeVkontakte(Long userId) {
        userSocialDataRepository.updateVkontakte(userId);
    }

    @Override
    public UserDataDto getUserData(Long userId, Long userOwnerId) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                messageCodeUtil.getFullErrorMessageByBundleCode(Constants.ERROR_MESSAGE_USER_BY_ID_NOT_FOUND,
                        new Object[]{userId})));
        var userDataDto = new UserDataDto();
        userDataDto.setId(userId);
        userDataDto.setNickname(user.getNickname());
        userDataDto.setAvatarId(user.getAvatarId());
        boolean isRequestExist = borrowingRequestRepository.existsByUserId(userId);
        if (isRequestExist) {
            userDataDto.setUserType(UserType.BORROWER);
        } else {
            isRequestExist = lendingRequestRepository.existsByUserId(userId);
            if (isRequestExist) {
                userDataDto.setUserType(UserType.LENDER);
            }
        }
        if (Objects.equals(userId, userOwnerId)) {
            userDataDto.setLikeCountView(getLikeCountView(userId));
        } else {
            userDataDto.setUserLikeResultView(getUserLikeResultView(userId, userOwnerId));
        }
        userDataDto.setUserCommentDtos(userMapper.toDto(userCommentRepository.findAllByUserId(userId)));
        return userDataDto;
    }

    @Override
    public LikeCountView getLikeCountView(Long userId) {
        return userLikeRepository.getLikeAndDislikeCountByUserId(userId);
    }

    @Override
    public UserLikeResultView getUserLikeResultView(Long userId, Long userOwnerId) {
        return userLikeRepository.getLikeResultAndLikeAndDislikeCountByUserId(userId, userOwnerId);
    }

    @Override
    @Transactional
    public void saveUserLike(Boolean isLike, Long userId, Long userOwnerId) {
        boolean isExistLike = userLikeRepository.existsByUserIdAndUserOwnerId(userId, userOwnerId);
        if (isExistLike) {
            userLikeRepository.updateUserLike(isLike, userId, userOwnerId);
        } else {
            var userLike = new UserLike();
            userLike.setIsLike(isLike);
            userLike.setUser(userRepository.getById(userId));
            userLike.setUserOwner(userRepository.getById(userOwnerId));
            userLikeRepository.save(userLike);
        }
    }

    @Override
    @Transactional
    public void deleteUserLike(Long userId, Long userOwnerId) {
        userLikeRepository.delete(userId, userOwnerId);
    }

    @Override
    @Transactional
    public UserCommentDto saveUserComment(UserCommentDto userCommentDto) {
        var userComment = new UserComment();
        Optional.ofNullable(userCommentDto.getComment()).ifPresent(userComment::setComment);
        Optional.ofNullable(userCommentDto.getParentUserCommentId()).ifPresent(parentId ->
                userComment.setParentUserComment(userCommentRepository.findById(parentId).orElseThrow()));
        Optional.ofNullable(userCommentDto.getUserId()).ifPresent(userId ->
                userComment.setUser(userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(
                        messageCodeUtil.getFullErrorMessageByBundleCode(
                                Constants.ERROR_MESSAGE_USER_BY_ID_NOT_FOUND, new Object[]{userId})))));
        Optional.ofNullable(userCommentDto.getUserOwnerId()).ifPresent(userOwnerId ->
                userComment.setUserOwner(userRepository.findById(userOwnerId).orElseThrow(() ->
                        new UserNotFoundException(
                                messageCodeUtil.getFullErrorMessageByBundleCode(
                                        Constants.ERROR_MESSAGE_USER_BY_ID_NOT_FOUND, new Object[]{userOwnerId})))));
        return userMapper.toDto(userCommentRepository.saveAndFlush(userComment));
    }

    @Override
    @Transactional
    public UserCommentDto updateUserComment(Long id, String comment) {
        userCommentRepository.updateUserCommentById(id, comment);
        return userMapper.toDto(userCommentRepository.findById(id).orElseThrow());
    }

    @Override
    @Transactional
    public void deleteUserComment(Long id) {
        userCommentRepository.deleteById(id);
    }

    private UserSocialData getVkontakteUserSocialData(AuthRequest authRequest, User user) {
        var vkontakteDto = vkontakteService.getUserData(authRequest.getSocialToken());
        UserSocialData userSocialData = Optional.ofNullable(user != null ? userSocialDataRepository.findByUserId(user.getId())
                : userSocialDataRepository.findByVkontakteId(vkontakteDto.getId())).orElseGet(UserSocialData::new);
        userSocialData.setVkontakteId(vkontakteDto.getId());
        userSocialData.setVkontakteToken(authRequest.getSocialToken());
        userSocialData.setVkontakteData(userSocialDataMapper.toVkontakteData(vkontakteDto));
        return userSocialData;
    }

    private UserSocialData getInstagramUserSocialData(AuthRequest authRequest, User user) {
        var instagramDto = instagramService.getUserData(authRequest.getSocialToken());
        UserSocialData userSocialData = Optional.ofNullable(user != null ? userSocialDataRepository.findByUserId(user.getId())
                : userSocialDataRepository.findByInstagramId(instagramDto.getId())).orElseGet(UserSocialData::new);
        userSocialData.setInstagramId(instagramDto.getId());
        userSocialData.setInstagramToken(authRequest.getSocialToken());
        userSocialData.setInstagramData(userSocialDataMapper.toInstagramData(instagramDto));
        return userSocialData;
    }

    private UserSocialData getLinkedInUserSocialData(AuthRequest authRequest, User user) {
        var linkedInDto = linkedInService.getUserData(authRequest.getSocialToken());
        UserSocialData userSocialData = Optional.ofNullable(user != null ? userSocialDataRepository.findByUserId(user.getId())
                : userSocialDataRepository.findByLinkedinId(linkedInDto.getId())).orElseGet(UserSocialData::new);
        userSocialData.setLinkedinId(linkedInDto.getId());
        userSocialData.setLinkedinToken(authRequest.getSocialToken());
        userSocialData.setLinkedinData(userSocialDataMapper.toLinkedInData(linkedInDto));
        return userSocialData;
    }

    private UserSocialData getFacebookUserSocialData(AuthRequest authRequest, User user) {
        var facebookDto = facebookService.getUserData(authRequest.getSocialToken());
        UserSocialData userSocialData = Optional.ofNullable(user != null ? userSocialDataRepository.findByUserId(user.getId())
                : userSocialDataRepository.findByFacebookId(facebookDto.getId())).orElseGet(UserSocialData::new);
        userSocialData.setFacebookId(facebookDto.getId());
        userSocialData.setFacebookToken(authRequest.getSocialToken());
        userSocialData.setFacebookData(userSocialDataMapper.toFacebookData(facebookDto));
        return userSocialData;
    }

    private User createUser(AuthRequest authRequest) {
        var user = new User();
        user.setDeviceToken(authRequest.getDeviceToken());
        user.setRoles(Collections.singletonList(Role.USER));
        user.setDisabled(Boolean.FALSE);
        user.setPushActive(Boolean.FALSE);
        user.setAutomaticDebiting(Boolean.FALSE);
        return user;
    }

    private User createUser(String phoneNumber, String deviceToken) {
        var user = new User();
        user.setPhoneNumber(phoneNumber);
        user.setDeviceToken(deviceToken);
        user.setRoles(Collections.singletonList(Role.USER));
        user.setDisabled(Boolean.FALSE);
        user.setPushActive(Boolean.FALSE);
        user.setAutomaticDebiting(Boolean.FALSE);
        return user;
    }

    private String generatePhoneVerifiedNumber() {
        try {
            Random random = SecureRandom.getInstanceStrong();
            return String.format("%04d", random.nextInt(10000));
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceException(e.getMessage());
        }
    }

}
