package by.nestegg.lending.dto;

import by.nestegg.lending.controller.view.LikeCountView;
import by.nestegg.lending.controller.view.UserLikeResultView;
import by.nestegg.lending.dto.enums.UserType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDataDto {

    private Long id;

    private String nickname;

    private String avatarId;

    private UserType userType;

    @JsonProperty("likeCount")
    private LikeCountView likeCountView;

    @JsonProperty("userLikes")
    private UserLikeResultView userLikeResultView;

    @JsonProperty("userComments")
    private List<UserCommentDto> userCommentDtos = new ArrayList<>();

}
