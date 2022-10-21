package by.nestegg.lending.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class AuthRequest {

    @NotBlank(message = "{error.message.validation.auth.device.token.empty}")
    private String deviceToken;

    @NotBlank(message = "{error.message.validation.auth.social.empty}")
    private String social;

    @NotBlank(message = "{error.message.validation.auth.social.token.empty}")
    private String socialToken;

}
