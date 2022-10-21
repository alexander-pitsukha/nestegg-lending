package by.nestegg.lending.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserLikeRequest {

    @NotNull
    private Boolean isLike;

    @NotNull
    private Long userId;

    @NotNull
    private Long userOwnerId;

}
