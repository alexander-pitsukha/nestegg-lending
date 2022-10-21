package by.nestegg.lending.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserCommentDto extends AbstractLongIdentifiableDto {

    @NotBlank
    private String comment;

    private Long parentUserCommentId;

    @NotNull
    private Long userId;

    @NotNull
    private Long userOwnerId;

    @JsonProperty("userComments")
    private List<UserCommentDto> userCommentDtos = new ArrayList<>();

}
