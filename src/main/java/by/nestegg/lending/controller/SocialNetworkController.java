package by.nestegg.lending.controller;

import by.nestegg.lending.dto.facebook.FacebookDataDto;
import by.nestegg.lending.dto.linkedin.LinkedInDataDto;
import by.nestegg.lending.service.FacebookService;
import by.nestegg.lending.service.InstagramService;
import by.nestegg.lending.service.LinkedInService;
import by.nestegg.lending.service.VkontakteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Tag(name = "Social Network Controller")
@RestController
@RequestMapping("socials")
@RequiredArgsConstructor
public class SocialNetworkController {

    private final FacebookService facebookService;
    private final LinkedInService linkedInService;
    private final InstagramService instagramService;
    private final VkontakteService vkontakteService;

    @GetMapping("facebook/user-data")
    public ResponseEntity<Map> getFacebookUserData() {
        return ResponseEntity.ok(facebookService.getUserData());
    }

    @GetMapping("facebook/facebook-user-data")
    public ResponseEntity<FacebookDataDto> getFacebookData() {
        return ResponseEntity.ok(facebookService.getFacebookUserData());
    }

    @GetMapping("linkedin/user-data")
    public ResponseEntity<Map> getLinkedInUserData() {
        return ResponseEntity.ok(linkedInService.getUserData());
    }

    @GetMapping("linkedin/linkedin-user-data")
    public ResponseEntity<LinkedInDataDto> getLinkedInData() {
        return ResponseEntity.ok(linkedInService.getLinkedInData());
    }

    @GetMapping("instagram/user-data")
    public ResponseEntity<Map> getInstagramUserData() {
        return ResponseEntity.ok(instagramService.getUserData());
    }

    @GetMapping("vkontakte/user-data")
    public ResponseEntity<Map> getVkontakteData() {
        return ResponseEntity.ok(vkontakteService.getUserData());
    }

}
