package by.nestegg.lending.service.impl;

import by.nestegg.lending.dto.linkedin.LinkedInDto;
import by.nestegg.lending.dto.linkedin.LinkedInDataDto;
import by.nestegg.lending.entity.User;
import by.nestegg.lending.service.enums.SocialNetwork;
import by.nestegg.lending.exception.ServiceException;
import by.nestegg.lending.repository.UserRepository;
import by.nestegg.lending.service.LinkedInService;
import by.nestegg.lending.util.Constants;
import by.nestegg.lending.util.MessageCodeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.linkedin.api.LinkedIn;
import org.springframework.social.linkedin.api.LinkedInProfileFull;
import org.springframework.social.linkedin.api.impl.LinkedInTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LinkedInServiceImpl implements LinkedInService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final MessageCodeUtil messageCodeUtil;

    @Value("${uri.linkedin}")
    private String linkedinUri;

    @Override
    public LinkedInDto getUserData(String accessToken) {
        checkSocialToken(accessToken);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);
        HttpEntity<LinkedInDto> entity = new HttpEntity<>(headers);
        var uri = UriComponentsBuilder
                .fromUriString(linkedinUri)
                .pathSegment(Constants.ME)
                .build().toUriString();
        return restTemplate.exchange(uri, HttpMethod.GET, entity, LinkedInDto.class).getBody();
    }

    @Override
    public Map getUserData() {
        String token = getUser().getUserSocialData().getLinkedinToken();
        checkSocialToken(token);
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<LinkedInDto> entity = new HttpEntity<>(headers);
        var uri = UriComponentsBuilder
                .fromUriString(linkedinUri)
                .pathSegment(Constants.ME)
                .queryParam("projection", "(id,firstName,lastName,profilePicture(displayImage~:playableStreams))")
                .build().toUriString();
        return restTemplate.exchange(uri, HttpMethod.GET, entity, Map.class).getBody();
    }

    @Override
    public LinkedInDataDto getLinkedInData() {
        var linkedInDataDto = new LinkedInDataDto();
        String token = getUser().getUserSocialData().getLinkedinToken();
        checkSocialToken(token);
        LinkedIn linkedIn = new LinkedInTemplate(token);
        LinkedInProfileFull linkedInProfile = linkedIn.profileOperations().getUserProfileFull();
        linkedInDataDto.setId(linkedInProfile.getId());
        linkedInDataDto.setFirstName(linkedInProfile.getFirstName());
        linkedInDataDto.setLastName(linkedInProfile.getLastName());
        linkedInDataDto.setEmail(linkedInProfile.getEmailAddress());
        linkedInDataDto.setBirthDate(linkedInProfile.getDateOfBirth());
        linkedInDataDto.setPositions(linkedInProfile.getPositions());
        linkedInDataDto.setEducations(linkedInProfile.getEducations());
        linkedInDataDto.setLocation(linkedInProfile.getLocation());
        linkedInDataDto.setProfilePicture(linkedInProfile.getProfilePictureUrl());
        return linkedInDataDto;
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
