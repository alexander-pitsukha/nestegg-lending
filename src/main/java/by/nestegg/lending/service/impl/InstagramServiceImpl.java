package by.nestegg.lending.service.impl;

import by.nestegg.lending.dto.instagram.InstagramDto;
import by.nestegg.lending.entity.User;
import by.nestegg.lending.exception.ServiceException;
import by.nestegg.lending.repository.UserRepository;
import by.nestegg.lending.service.InstagramService;
import by.nestegg.lending.service.enums.InstagramFields;
import by.nestegg.lending.service.enums.SocialNetwork;
import by.nestegg.lending.util.Constants;
import by.nestegg.lending.util.MessageCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class InstagramServiceImpl implements InstagramService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final MessageCodeUtil messageCodeUtil;

    @Value("${uri.instagram}")
    private String instagramUri;

    @Override
    public InstagramDto getUserData(String accessToken) {
        checkSocialToken(accessToken);
        var uri = UriComponentsBuilder.fromUriString(instagramUri)
                .pathSegment(Constants.ME)
                .queryParam(Constants.FIELDS, Stream.of(InstagramFields.ID, InstagramFields.USERNAME,
                                InstagramFields.EMAIL).map(InstagramFields::getField)
                        .collect(Collectors.joining(Constants.COMMA)))
                .queryParam(Constants.ACCESS_TOKEN, accessToken)
                .build().toUriString();
        return restTemplate.getForObject(uri, InstagramDto.class);
    }

    @Override
    public Map getUserData() {
        String token = getUser().getUserSocialData().getInstagramToken();
        checkSocialToken(token);
        var uri = UriComponentsBuilder.fromUriString(instagramUri)
                .pathSegment(Constants.ME)
                .queryParam(Constants.FIELDS, Stream.of(InstagramFields.values()).map(InstagramFields::getField)
                        .collect(Collectors.joining(Constants.COMMA)))
                .queryParam(Constants.ACCESS_TOKEN, token)
                .build().toUriString();
        return restTemplate.getForObject(uri, Map.class);
    }

    private User getUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByDeviceToken(name);
    }

    private void checkSocialToken(String token) {
        if (!StringUtils.hasLength(token)) {
            throw new ServiceException(messageCodeUtil.getFullErrorMessageByBundleCode(
                    "error.message.social.token.empty", new Object[]{SocialNetwork.LINKEDIN.getName()}));
        }
    }

}
