package by.nestegg.lending.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LinkedinFields {

    ID("id"),
    FIRST_NAME("first_name"),
    LAST_NAME("last_name"),
    EMAIL("email"),
    BIRTHDATE("birthDate"),
    POSITIONS("positions"),
    EDUCATIONS("educations"),
    GEOLOCATION("geoLocation"),
    PROFILE_PICTURE("profilePicture");

    private String field;

}
