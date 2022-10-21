package by.nestegg.lending.controller;

import by.nestegg.lending.controller.request.UserLikeRequest;
import by.nestegg.lending.controller.view.LikeCountView;
import by.nestegg.lending.controller.view.UserLikeResultView;
import by.nestegg.lending.dto.UserCommentDto;
import by.nestegg.lending.dto.UserDataDto;
import by.nestegg.lending.dto.UserDto;
import by.nestegg.lending.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@WithMockUser
class UserControllerTests extends BasicControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @BeforeEach
    void setUp() throws Exception {
        super.setUp();
    }

    @Test
    void testUpdateUserProfile() throws Exception {
        UserDto userDto = getObjectFromJson("user/user_profile.json", UserDto.class);

        given(userService.updateUserProfile(any(UserDto.class))).willReturn(userDto);

        mockMvc.perform(put("/users/profile/{userId}", userDto.getId())
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.nickname", is(userDto.getNickname())))
                .andExpect(jsonPath("$.deviceToken", is(userDto.getDeviceToken())))
                .andExpect(jsonPath("$.pushActive", is(userDto.getPushActive())))
                .andExpect(jsonPath("$.bankCards", hasSize(userDto.getBankCardDtos().size())));

        verify(userService).updateUserProfile(any(UserDto.class));
    }

    @Test
    void testUpdateUserProfile_BadRequest() throws Exception {
        UserDto userDto = getObjectFromJson("user/user_profile.json", UserDto.class);

        given(userService.updateUserProfile(any(UserDto.class))).willReturn(userDto);

        mockMvc.perform(put("/users/profile/{userId}", Long.MAX_VALUE)
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetUserProfile() throws Exception {
        UserDto userDto = getObjectFromJson("user/user_profile.json", UserDto.class);

        given(userService.getUserProfile(anyString())).willReturn(userDto);

        mockMvc.perform(get("/users/profile")
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDto.getId()))
                .andExpect(jsonPath("$.nickname", is(userDto.getNickname())))
                .andExpect(jsonPath("$.deviceToken", is(userDto.getDeviceToken())))
                .andExpect(jsonPath("$.pushActive", is(userDto.getPushActive())))
                .andExpect(jsonPath("$.bankCards", hasSize(userDto.getBankCardDtos().size())));

        verify(userService).getUserProfile(anyString());
    }

    @Test
    void testRemoveFacebook() throws Exception {
        willDoNothing().given(userService).removeFacebook(isA(Long.class));

        mockMvc.perform(put("/users/{userId}/facebook", 1)
                        .with(csrf())
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(userService).removeFacebook(anyLong());
    }

    @Test
    void testRemoveLinkedIn() throws Exception {
        willDoNothing().given(userService).removeLinkedIn(isA(Long.class));

        mockMvc.perform(put("/users/{userId}/linkedin", 1)
                        .with(csrf())
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(userService).removeLinkedIn(anyLong());
    }

    @Test
    void testRemoveInstagram() throws Exception {
        willDoNothing().given(userService).removeInstagram(isA(Long.class));

        mockMvc.perform(put("/users/{userId}/instagram", 1)
                        .with(csrf())
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(userService).removeInstagram(anyLong());
    }

    @Test
    void testRemoveVkontakte() throws Exception {
        willDoNothing().given(userService).removeVkontakte(isA(Long.class));

        mockMvc.perform(put("/users/{userId}/vkontakte", 1)
                        .with(csrf())
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(userService).removeVkontakte(anyLong());
    }

    @Test
    void testGetUserData() throws Exception {
        UserDataDto userDataDto = getObjectFromJson("user/user_data_dto.json", UserDataDto.class);

        given(userService.getUserData(anyLong(), anyLong())).willReturn(userDataDto);

        mockMvc.perform(get("/users/{userOwnerId}/data/{userId}", 2, 1)
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userDataDto.getId()))
                .andExpect(jsonPath("$.nickname", is(userDataDto.getNickname())))
                .andExpect(jsonPath("$.avatarId", is(userDataDto.getAvatarId())))
                .andExpect(jsonPath("$.userType", is(userDataDto.getUserType().name())))
                .andExpect(jsonPath("$.userComments", hasSize(userDataDto.getUserCommentDtos()
                        .size())));

        verify(userService).getUserData(anyLong(), anyLong());
    }

    @Test
    void testGetUserLikesDislikes() throws Exception {
        UserLikeResultView userLikeResultView = getUserLikeResultView();

        given(userService.getUserLikeResultView(anyLong(), anyLong())).willReturn(userLikeResultView);

        mockMvc.perform(get("/users/{userOwnerId}/like/{userId}", 2, 1)
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isLike", is(userLikeResultView.getIsLike())))
                .andExpect(jsonPath("$.likeCount", is(userLikeResultView.getLikeCount())))
                .andExpect(jsonPath("$.dislikeCount", is(userLikeResultView.getDislikeCount())));

        verify(userService).getUserLikeResultView(anyLong(), anyLong());
    }

    @Test
    void testSaveLike() throws Exception {
        UserLikeRequest userLikeRequest = getObjectFromJson("user/request/user_like_request.json",
                UserLikeRequest.class);
        LikeCountView likeCountView = getLikeCountView();

        willDoNothing().given(userService).saveUserLike(isA(Boolean.class), isA(Long.class), isA(Long.class));
        given(userService.getLikeCountView(anyLong())).willReturn(likeCountView);

        mockMvc.perform(put("/users/like")
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(userLikeRequest))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeCount", is(likeCountView.getLikeCount())))
                .andExpect(jsonPath("$.dislikeCount", is(likeCountView.getDislikeCount())));

        verify(userService).saveUserLike(anyBoolean(), anyLong(), anyLong());
        verify(userService).getLikeCountView(anyLong());
    }

    @Test
    void testDeleteLike() throws Exception {
        LikeCountView likeCountView = getLikeCountView();

        willDoNothing().given(userService).deleteUserLike(isA(Long.class), isA(Long.class));
        given(userService.getLikeCountView(anyLong())).willReturn(likeCountView);

        mockMvc.perform(delete("/users/{userOwnerId}/like/{userId}", 2, 1)
                        .with(csrf())
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.likeCount", is(likeCountView.getLikeCount())))
                .andExpect(jsonPath("$.dislikeCount", is(likeCountView.getDislikeCount())));

        verify(userService).deleteUserLike(anyLong(), anyLong());
        verify(userService).getLikeCountView(anyLong());
    }

    @Test
    void testSaveComment() throws Exception {
        UserCommentDto userCommentDto = getObjectFromJson("user/user_comment_dto.json", UserCommentDto.class);

        given(userService.saveUserComment(any(UserCommentDto.class))).willReturn(userCommentDto);

        mockMvc.perform(post("/users/comment")
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(userCommentDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userCommentDto.getId()))
                .andExpect(jsonPath("$.comment", is(userCommentDto.getComment())))
                .andExpect(jsonPath("$.userId").value(userCommentDto.getUserId()))
                .andExpect(jsonPath("$.userOwnerId").value(userCommentDto.getUserOwnerId()));

        verify(userService).saveUserComment(any(UserCommentDto.class));
    }

    @Test
    void testUpdateComment() throws Exception {
        UserCommentDto userCommentDto = getObjectFromJson("user/user_comment_dto.json", UserCommentDto.class);

        given(userService.updateUserComment(anyLong(), anyString())).willReturn(userCommentDto);

        mockMvc.perform(put("/users/comment/{id}", 1)
                        .with(csrf())
                        .headers(httpHeaders())
                        .content(asJsonString(userCommentDto.getComment()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userCommentDto.getId()))
                .andExpect(jsonPath("$.comment", is(userCommentDto.getComment())))
                .andExpect(jsonPath("$.userId").value(userCommentDto.getUserId()))
                .andExpect(jsonPath("$.userOwnerId").value(userCommentDto.getUserOwnerId()));

        verify(userService).updateUserComment(anyLong(), anyString());
    }

    @Test
    void testDeleteComment() throws Exception {
        willDoNothing().given(userService).deleteUserComment(isA(Long.class));

        mockMvc.perform(delete("/users/comment/{id}", 1)
                        .with(csrf())
                        .headers(httpHeaders())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(userService).deleteUserComment(anyLong());
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
