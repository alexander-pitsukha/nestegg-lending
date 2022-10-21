package by.nestegg.lending.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum VkontakteFields {

    ID("id"),
    FIRST_NAME("first_name"),
    LAST_NAME("last_name"),
    EMAIL("email"),
    FRIENDS("friends"),
    PHOTOS("photos"),
    VIDEO("video"),
    STATUS("status"),
    NOTES("notes"),
    GROUPS("groups");

    private String field;

}
