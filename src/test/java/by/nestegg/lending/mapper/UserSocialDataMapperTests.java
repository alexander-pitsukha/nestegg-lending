package by.nestegg.lending.mapper;

import by.nestegg.lending.AbstractTests;
import by.nestegg.lending.dto.facebook.FacebookDto;
import by.nestegg.lending.dto.instagram.InstagramDto;
import by.nestegg.lending.dto.linkedin.LinkedInDto;
import by.nestegg.lending.dto.vkontakte.VkontakteDto;
import by.nestegg.lending.entity.data.FacebookData;
import by.nestegg.lending.entity.data.InstagramData;
import by.nestegg.lending.entity.data.LinkedInData;
import by.nestegg.lending.entity.data.VkontakteData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class UserSocialDataMapperTests extends AbstractTests {

    @Autowired
    private UserSocialDataMapper userSocialDataMapper;

    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        public UserSocialDataMapper userMapper() {
            return new UserSocialDataMapperImpl();
        }
    }

    @Test
    void testToFacebookData() throws Exception {
        FacebookDto facebookDto = getObjectFromJson("user/data/facebook_data.json", FacebookDto.class);

        FacebookData facebookData = userSocialDataMapper.toFacebookData(facebookDto);

        assertNotNull(facebookData);
        assertEquals(facebookDto.getId(), facebookData.getId());
        assertEquals(facebookDto.getFirstName(), facebookData.getFirstName());
        assertEquals(facebookDto.getLastName(), facebookData.getLastName());
        assertEquals(facebookDto.getEmail(), facebookData.getEmail());
    }

    @Test
    void testToLinkedInData() throws Exception {
        LinkedInDto linkedInDto = getObjectFromJson("user/data/linkedin_data.json", LinkedInDto.class);

        LinkedInData linkedInData = userSocialDataMapper.toLinkedInData(linkedInDto);

        assertNotNull(linkedInData);
        assertEquals(linkedInDto.getId(), linkedInData.getId());
        assertEquals(linkedInDto.getFirstName(), linkedInData.getFirstName());
        assertEquals(linkedInDto.getLastName(), linkedInData.getLastName());
    }

    @Test
    void testToInstagramData() throws Exception {
        InstagramDto instagramDto = getObjectFromJson("user/data/instagram_data.json", InstagramDto.class);

        InstagramData instagramData = userSocialDataMapper.toInstagramData(instagramDto);

        assertNotNull(instagramData);
        assertEquals(instagramDto.getId(), instagramData.getId());
        assertEquals(instagramDto.getUsername(), instagramData.getUsername());
        assertEquals(instagramDto.getEmail(), instagramData.getEmail());
    }

    @Test
    void testToVkontakteData() throws Exception {
        VkontakteDto vkontakteDto = getObjectFromJson("user/data/vkontakte_data.json", VkontakteDto.class);

        VkontakteData vkontakteData = userSocialDataMapper.toVkontakteData(vkontakteDto);

        assertNotNull(vkontakteData);
        assertEquals(vkontakteDto.getId(), vkontakteData.getId());
        assertEquals(vkontakteDto.getFirstName(), vkontakteData.getFirstName());
        assertEquals(vkontakteDto.getLastName(), vkontakteData.getLastName());
    }

}
