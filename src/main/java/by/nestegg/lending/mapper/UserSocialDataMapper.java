package by.nestegg.lending.mapper;

import by.nestegg.lending.dto.facebook.FacebookDto;
import by.nestegg.lending.dto.instagram.InstagramDto;
import by.nestegg.lending.dto.linkedin.LinkedInDto;
import by.nestegg.lending.dto.vkontakte.VkontakteDto;
import by.nestegg.lending.entity.data.FacebookData;
import by.nestegg.lending.entity.data.InstagramData;
import by.nestegg.lending.entity.data.LinkedInData;
import by.nestegg.lending.entity.data.VkontakteData;
import by.nestegg.lending.util.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = Constants.COMPONENT_MODEL)
public interface UserSocialDataMapper {

    UserSocialDataMapper INSTANCE = Mappers.getMapper(UserSocialDataMapper.class);

    FacebookData toFacebookData(FacebookDto facebookDto);

    LinkedInData toLinkedInData(LinkedInDto facebookDto);

    InstagramData toInstagramData(InstagramDto instagramDto);

    VkontakteData toVkontakteData(VkontakteDto vkontakteDto);

}
