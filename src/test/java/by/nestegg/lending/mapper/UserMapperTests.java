package by.nestegg.lending.mapper;

import by.nestegg.lending.AbstractTests;
import by.nestegg.lending.dto.BankCardDto;
import by.nestegg.lending.dto.UserCommentDto;
import by.nestegg.lending.dto.UserDto;
import by.nestegg.lending.entity.BankCard;
import by.nestegg.lending.entity.User;
import by.nestegg.lending.entity.UserComment;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
class UserMapperTests extends AbstractTests {

    @Autowired
    private UserMapper userMapper;

    @TestConfiguration
    static class TestContextConfiguration {

        @Bean
        public UserMapper userMapper() {
            return new UserMapperImpl();
        }
    }

    @Test
    void testToUserDto() throws Exception {
        User user = getObjectFromJson("user/user_3.json", User.class);

        UserDto userDto = userMapper.toDto(user);

        assertNotNull(userDto);
        assertEquals(user.getId(), userDto.getId());
        assertEquals(user.getNickname(), userDto.getNickname());
        assertEquals(user.getDeviceToken(), userDto.getDeviceToken());
        assertEquals(user.getPushActive(), userDto.getPushActive());
        assertEquals(user.getAutomaticDebiting(), userDto.getAutomaticDebiting());
        assertEquals(user.getBankCards().size(), userDto.getBankCardDtos().size());
        assertEquals(user.getBankCards().get(0).getId(), userDto.getBankCardDtos().get(0).getId());
        assertEquals(userMapper.decodeBase64(user.getBankCards().get(0).getCardNumber()),
                userDto.getBankCardDtos().get(0).getCardNumber());
        assertEquals(userMapper.decodeBase64(user.getBankCards().get(0).getCvv()),
                userDto.getBankCardDtos().get(0).getCvv());
        assertEquals(user.getBankCards().get(0).getExpireDate(), userDto.getBankCardDtos().get(0).getExpireDate());
        assertEquals(user.getBankCards().get(0).getCardholderName(),
                userDto.getBankCardDtos().get(0).getCardholderName());
        assertEquals(user.getBankCards().get(0).getIsDefault(), userDto.getBankCardDtos().get(0).getIsDefault());
        assertEquals(user.getAvatarId(), userDto.getAvatarId());
        assertEquals(user.getUserSocialData().getFacebookId(), userDto.getFacebookId());
        assertEquals(user.getUserSocialData().getLinkedinId(), userDto.getLinkedinId());
        assertEquals(user.getUserSocialData().getInstagramId(), userDto.getInstagramId());
        assertEquals(user.getUserSocialData().getVkontakteId(), userDto.getVkontakteId());
    }

    @Test
    void testToBankCardDto() throws Exception {
        BankCard bankCard = getObjectFromJson("user/bank_card.json", BankCard.class);

        BankCardDto bankCardDto = userMapper.toDto(bankCard);

        assertNotNull(bankCardDto);
        assertEquals(bankCard.getId(), bankCardDto.getId());
        assertEquals(userMapper.decodeBase64(bankCard.getCardNumber()), bankCardDto.getCardNumber());
        assertEquals(userMapper.decodeBase64(bankCard.getCvv()), bankCardDto.getCvv());
        assertEquals(bankCard.getExpireDate(), bankCardDto.getExpireDate());
        assertEquals(bankCard.getCardholderName(), bankCardDto.getCardholderName());
        assertEquals(bankCard.getIsDefault(), bankCardDto.getIsDefault());
    }

    @Test
    void testToBankCardEntities() throws Exception {
        List<BankCardDto> bankCardDtos = Collections.singletonList(getObjectFromJson("user/bank_card_dto.json",
                BankCardDto.class));

        List<BankCard> bankCards = userMapper.toEntity(bankCardDtos);

        assertNotNull(bankCardDtos);
        assertEquals(bankCardDtos.size(), bankCards.size());
        assertEquals(bankCardDtos.get(0).getId(), bankCards.get(0).getId());
        assertEquals(userMapper.encodeBase64(bankCardDtos.get(0).getCardNumber()), bankCards.get(0).getCardNumber());
        assertEquals(userMapper.encodeBase64(bankCardDtos.get(0).getCvv()), bankCards.get(0).getCvv());
        assertEquals(bankCardDtos.get(0).getExpireDate(), bankCards.get(0).getExpireDate());
        assertEquals(bankCardDtos.get(0).getCardholderName(), bankCards.get(0).getCardholderName());
        assertEquals(bankCardDtos.get(0).getIsDefault(), bankCards.get(0).getIsDefault());
    }

    @Test
    void testToBankCardEntity() throws Exception {
        BankCardDto bankCardDto = getObjectFromJson("user/bank_card_dto.json", BankCardDto.class);

        BankCard bankCard = userMapper.toEntity(bankCardDto);

        assertNotNull(bankCard);
        assertEquals(bankCardDto.getId(), bankCard.getId());
        assertEquals(userMapper.encodeBase64(bankCardDto.getCardNumber()), bankCard.getCardNumber());
        assertEquals(userMapper.encodeBase64(bankCardDto.getCvv()), bankCard.getCvv());
        assertEquals(bankCardDto.getExpireDate(), bankCard.getExpireDate());
        assertEquals(bankCardDto.getCardholderName(), bankCard.getCardholderName());
        assertEquals(bankCardDto.getIsDefault(), bankCard.getIsDefault());
    }

    @Test
    void testToUserCommentDto() throws Exception {
        UserComment userComment = getObjectFromJson("user/user_comment_2.json", UserComment.class);

        UserCommentDto userCommentDto = userMapper.toDto(userComment);

        assertNotNull(userCommentDto);
        assertEquals(userComment.getId(), userCommentDto.getId());
        assertEquals(userComment.getComment(), userCommentDto.getComment());
        assertEquals(userComment.getUser().getId(), userCommentDto.getUserId());
        assertEquals(userComment.getUserOwner().getId(), userCommentDto.getUserOwnerId());
        assertEquals(userComment.getUserComments().size(), userCommentDto.getUserCommentDtos().size());
        assertEquals(userComment.getUserComments().get(0).getId(), userCommentDto.getUserCommentDtos().get(0).getId());
        assertEquals(userComment.getUserComments().get(0).getComment(),
                userCommentDto.getUserCommentDtos().get(0).getComment());
        assertEquals(userComment.getUserComments().get(0).getParentUserComment().getId(),
                userCommentDto.getUserCommentDtos().get(0).getParentUserCommentId());
        assertEquals(userComment.getUserComments().get(0).getUser().getId(),
                userCommentDto.getUserCommentDtos().get(0).getUserId());
        assertEquals(userComment.getUserComments().get(0).getUserOwner().getId(),
                userCommentDto.getUserCommentDtos().get(0).getUserOwnerId());
    }

    @Test
    void testToUserCommentDtos() throws Exception {
        List<UserComment> userComments = Collections.singletonList(getObjectFromJson("user/user_comment_2.json",
                UserComment.class));

        List<UserCommentDto> userCommentDtos = userMapper.toDto(userComments);

        assertNotNull(userCommentDtos);
        assertEquals(userComments.get(0).getId(), userCommentDtos.get(0).getId());
        assertEquals(userComments.get(0).getComment(), userCommentDtos.get(0).getComment());
        assertEquals(userComments.get(0).getUser().getId(), userCommentDtos.get(0).getUserId());
        assertEquals(userComments.get(0).getUserOwner().getId(), userCommentDtos.get(0).getUserOwnerId());
        assertEquals(userComments.get(0).getUserComments().size(), userCommentDtos.get(0).getUserCommentDtos().size());
        assertEquals(userComments.get(0).getUserComments().get(0).getId(),
                userCommentDtos.get(0).getUserCommentDtos().get(0).getId());
        assertEquals(userComments.get(0).getUserComments().get(0).getComment(),
                userCommentDtos.get(0).getUserCommentDtos().get(0).getComment());
        assertEquals(userComments.get(0).getUserComments().get(0).getParentUserComment().getId(),
                userCommentDtos.get(0).getUserCommentDtos().get(0).getParentUserCommentId());
        assertEquals(userComments.get(0).getUserComments().get(0).getUser().getId(),
                userCommentDtos.get(0).getUserCommentDtos().get(0).getUserId());
        assertEquals(userComments.get(0).getUserComments().get(0).getUserOwner().getId(),
                userCommentDtos.get(0).getUserCommentDtos().get(0).getUserOwnerId());
    }

}
