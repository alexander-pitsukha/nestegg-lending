package by.nestegg.lending.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDto extends AbstractLongIdentifiableDto {

    @NotBlank(message = "{error.message.user.nickname.not.empty}")
    private String nickname;

    @NotBlank
    private String deviceToken;

    @NotNull
    private Boolean pushActive;

    @NotNull
    private Boolean automaticDebiting;

    @Valid
    @JsonProperty("bankCards")
    private List<BankCardDto> bankCardDtos = new ArrayList<>();

    private String avatarId;

    private String facebookId;

    private String linkedinId;

    private String instagramId;

    private String vkontakteId;

}
