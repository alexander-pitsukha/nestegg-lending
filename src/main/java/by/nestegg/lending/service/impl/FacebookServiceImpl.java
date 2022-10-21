package by.nestegg.lending.service.impl;

import by.nestegg.lending.dto.facebook.FacebookDataDto;
import by.nestegg.lending.dto.facebook.FacebookDto;
import by.nestegg.lending.entity.User;
import by.nestegg.lending.service.enums.SocialNetwork;
import by.nestegg.lending.exception.ServiceException;
import by.nestegg.lending.repository.UserRepository;
import by.nestegg.lending.service.FacebookService;
import by.nestegg.lending.service.enums.FacebookFields;
import by.nestegg.lending.util.Constants;
import by.nestegg.lending.util.MessageCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.Page;
import org.springframework.social.facebook.api.PagedList;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FacebookServiceImpl implements FacebookService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final MessageCodeUtil messageCodeUtil;

    @Value("${uri.facebook}")
    private String facebookUri;

    @Override
    public FacebookDto getUserData(String accessToken) {
        checkSocialToken(accessToken);
        var uri = UriComponentsBuilder.fromUriString(facebookUri)
                .pathSegment(Constants.ME)
                .queryParam(Constants.FIELDS, Stream.of(FacebookFields.ID, FacebookFields.FIRST_NAME,
                                FacebookFields.LAST_NAME, FacebookFields.EMAIL).map(FacebookFields::getField)
                        .collect(Collectors.joining(Constants.COMMA)))
                .queryParam(Constants.ACCESS_TOKEN, accessToken)
                .build().toUriString();
        return restTemplate.getForObject(uri, FacebookDto.class);
    }

    @Override
    public Map getUserData() {
        String token = getUser().getUserSocialData().getFacebookToken();
        checkSocialToken(token);
        var uri = UriComponentsBuilder.fromUriString(facebookUri)
                .pathSegment(Constants.ME)
                .queryParam(Constants.FIELDS, Stream.of(FacebookFields.values()).map(FacebookFields::getField)
                        .collect(Collectors.joining(Constants.COMMA)))
                .queryParam(Constants.ACCESS_TOKEN, token)
                .build().toUriString();
        return restTemplate.getForObject(uri, Map.class);
    }

    @Override
    public FacebookDataDto getFacebookUserData() {
        var facebookDataDto = new FacebookDataDto();
        String token = getUser().getUserSocialData().getFacebookToken();
        checkSocialToken(token);
        Facebook facebook = new FacebookTemplate(token);
        var facebookUser = facebook.fetchObject(Constants.ME, org.springframework.social.facebook.api.User.class,
                Stream.of(FacebookFields.values()).map(FacebookFields::getField)
                        .collect(Collectors.joining(Constants.COMMA)));
        facebookDataDto.setId(facebookUser.getId());
        facebookDataDto.setFirstName(facebookUser.getFirstName());
        facebookDataDto.setLastName(facebookUser.getLastName());
        facebookDataDto.setBirthday(facebookUser.getBirthday());
        facebookDataDto.setEmail(facebookUser.getEmail());
        facebookDataDto.setGender(facebookUser.getGender());
        facebookDataDto.setHometown(facebookUser.getHometown());
        facebookDataDto.setLocation(facebookUser.getLocation());
        facebookDataDto.setFriends(facebook.friendOperations().getFriends());
        PagedList<Page> likes = facebook.likeOperations().getBooks();
        likes.addAll(facebook.likeOperations().getMovies());
        likes.addAll(facebook.likeOperations().getGames());
        likes.addAll(facebook.likeOperations().getMusic());
        likes.addAll(facebook.likeOperations().getPagesLiked());
        likes.addAll(facebook.likeOperations().getTelevision());
        facebookDataDto.setLikes(likes);
        //facebook.mediaOperations().getAlbums();
        //facebook.mediaOperations().getPhotos();
        facebookDataDto.setPosts(facebook.feedOperations().getPosts());
        facebookDataDto.setVideos(facebook.mediaOperations().getVideos());
        return facebookDataDto;
    }

    private User getUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByDeviceToken(name);
    }

    private void checkSocialToken(String token) {
        if (!StringUtils.hasLength(token)) {
            throw new ServiceException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    "error.message.social.token.empty", new Object[]{SocialNetwork.FACEBOOK.getName()}));
        }
    }

}
