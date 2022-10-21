package by.nestegg.lending.service.impl;

import by.nestegg.lending.dto.vkontakte.VkontakteDto;
import by.nestegg.lending.entity.User;
import by.nestegg.lending.exception.ServiceException;
import by.nestegg.lending.repository.UserRepository;
import by.nestegg.lending.service.VkontakteService;
import by.nestegg.lending.service.enums.SocialNetwork;
import by.nestegg.lending.service.enums.VkontakteFields;
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
public class VkontakteServiceImpl implements VkontakteService {

    private static final String ACCOUNT_GET_PROFILE_INFO = "account.getProfileInfo";
    private static final String V = "v";

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final MessageCodeUtil messageCodeUtil;

    @Value("${uri.vkontakte}")
    private String vkontakteUri;

    @Value("${vk.api.version}")
    private String vkApiVersion;

    @Override
    public VkontakteDto getUserData(String accessToken) {
        checkSocialToken(accessToken);
        var uri = UriComponentsBuilder.fromUriString(vkontakteUri)
                .pathSegment(ACCOUNT_GET_PROFILE_INFO)
                .queryParam(Constants.FIELDS, Stream.of(VkontakteFields.ID, VkontakteFields.FIRST_NAME,
                                VkontakteFields.LAST_NAME, VkontakteFields.EMAIL).map(VkontakteFields::getField)
                        .collect(Collectors.joining(Constants.COMMA)))
                .queryParam(Constants.ACCESS_TOKEN, accessToken)
                .queryParam(V, vkApiVersion)
                .build().toUriString();
        return restTemplate.getForObject(uri, VkontakteDto.class);
    }

    @Override
    public Map getUserData() {
        var token = getUser().getUserSocialData().getVkontakteToken();
        checkSocialToken(token);
        var uri = UriComponentsBuilder.fromUriString(vkontakteUri)
                .pathSegment(ACCOUNT_GET_PROFILE_INFO)
                .queryParam(Constants.FIELDS, Stream.of(VkontakteFields.values()).map(VkontakteFields::getField)
                        .collect(Collectors.joining(Constants.COMMA)))
                .queryParam(Constants.ACCESS_TOKEN, token)
                .queryParam(V, vkApiVersion)
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
                    "error.message.social.token.empty", new Object[]{SocialNetwork.VKONTAKTE.getName()}));
        }
    }

}
