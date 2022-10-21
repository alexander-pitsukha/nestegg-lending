package by.nestegg.lending.controller.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
public class PhoneRequest {

    @NotBlank(message = "{error.message.auth.phone.number.empty}")
    @Pattern(regexp = "^\\+(?:\\d ?){6,14}\\d$", message = "{error.message.auth.phone.number.wrong}")
    private String phoneNumber;

    @NotBlank(message = "{error.message.validation.auth.device.token.empty}")
    private String deviceToken;

    private String verifyCode;

}
