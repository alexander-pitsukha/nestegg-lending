package by.nestegg.lending.mapper;

import by.nestegg.lending.dto.BankCardDto;
import by.nestegg.lending.dto.UserCommentDto;
import by.nestegg.lending.dto.UserDto;
import by.nestegg.lending.entity.BankCard;
import by.nestegg.lending.entity.User;
import by.nestegg.lending.entity.UserComment;
import by.nestegg.lending.util.Constants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

@Mapper(componentModel = Constants.COMPONENT_MODEL)
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "bankCardDtos", source = "bankCards")
    @Mapping(target = "facebookId", source = "userSocialData.facebookId")
    @Mapping(target = "linkedinId", source = "userSocialData.linkedinId")
    @Mapping(target = "instagramId", source = "userSocialData.instagramId")
    @Mapping(target = "vkontakteId", source = "userSocialData.vkontakteId")
    UserDto toDto(User user);

    @Mapping(target = "cardNumber", qualifiedByName = "decodeBase64")
    @Mapping(target = "cvv", qualifiedByName = "decodeBase64")
    BankCardDto toDto(BankCard bankCard);

    List<BankCard> toEntity(List<BankCardDto> bankCardDtos);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cardNumber", qualifiedByName = "encodeBase64")
    @Mapping(target = "cvv", qualifiedByName = "encodeBase64")
    @Mapping(target = "isDefault", defaultValue = "false")
    BankCard toEntity(BankCardDto bankCardDto);

    @Mapping(target = "userCommentDtos", source = "userComment.userComments")
    @Mapping(target = "parentUserCommentId", source = "userComment.parentUserComment.id")
    @Mapping(target = "userId", source = "userComment.user.id")
    @Mapping(target = "userOwnerId", source = "userComment.userOwner.id")
    UserCommentDto toDto(UserComment userComment);

    List<UserCommentDto> toDto(List<UserComment> userComments);

    @Named("encodeBase64")
    default String encodeBase64(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    @Named("decodeBase64")
    default String decodeBase64(String str) {
        return new String(Base64.getDecoder().decode(str), StandardCharsets.UTF_8);
    }

}
