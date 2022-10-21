package by.nestegg.lending.service;

import by.nestegg.lending.controller.request.AuthRequest;
import by.nestegg.lending.controller.request.PhoneRequest;
import by.nestegg.lending.controller.view.LikeCountView;
import by.nestegg.lending.controller.view.UserLikeResultView;
import by.nestegg.lending.dto.UserCommentDto;
import by.nestegg.lending.dto.UserDataDto;
import by.nestegg.lending.dto.UserDto;

public interface UserService {

    UserDto createOrUpdateUserSocialData(AuthRequest authRequest);

    String createOrUpdateUserByPhoneNumber(String phone, String deviceToken);

    UserDto updateUserByPhoneVerifyCode(PhoneRequest phoneRequest);

    UserDto updateUserProfile(UserDto userDto);

    UserDto getUserProfile(String deviceToken);

    void removeFacebook(Long userId);

    void removeLinkedIn(Long userId);

    void removeInstagram(Long userId);

    void removeVkontakte(Long userId);

    UserDataDto getUserData(Long userId, Long userOwnerId);

    LikeCountView getLikeCountView(Long userId);

    UserLikeResultView getUserLikeResultView(Long userId, Long userOwnerId);

    void saveUserLike(Boolean isLike, Long userId, Long userOwnerId);

    void deleteUserLike(Long userId, Long userOwnerId);

    UserCommentDto saveUserComment(UserCommentDto userCommentDto);

    UserCommentDto updateUserComment(Long id, String comment);

    void deleteUserComment(Long id);

}
