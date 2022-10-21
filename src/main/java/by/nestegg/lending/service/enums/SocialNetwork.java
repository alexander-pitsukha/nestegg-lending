package by.nestegg.lending.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SocialNetwork {

    FACEBOOK("facebook"),
    LINKEDIN("linkedin"),
    INSTAGRAM("instagram"),
    VKONTAKTE("vkontakte");

    private String name;

    public static SocialNetwork valueOfSocial(String social) {
        for (SocialNetwork entry : values()) {
            if (entry.name.equals(social)) {
                return entry;
            }
        }
        return null;
    }

}
