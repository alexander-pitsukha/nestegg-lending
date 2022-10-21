package by.nestegg.lending.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BankCardDto extends AbstractLongIdentifiableDto {

    @NotBlank
    private String cardNumber;

    @NotBlank
    private String cvv;

    @NotBlank
    private String expireDate;

    @NotBlank
    private String cardholderName;

    @NotNull
    private Boolean isDefault;

}
