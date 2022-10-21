package by.nestegg.lending.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FacebookFields {

    ID("id"),
    FIRST_NAME("first_name"),
    LAST_NAME("last_name"),
    EMAIL("email"),
    BIRTHDAY("birthday"),
    GENDER("gender"),
    HOMETOWN("hometown"),
    LOCATION("location"),
    FRIENDS("friends"),
    LIKES("likes"),
    PHOTOS("photos"),
    POSTS("posts"),
    VIDEOS("videos");

    private String field;

}
