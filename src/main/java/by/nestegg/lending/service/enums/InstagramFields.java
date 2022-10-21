package by.nestegg.lending.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InstagramFields {

    ID("id"),
    USERNAME("username"),
    EMAIL("email");

    private String field;

}
