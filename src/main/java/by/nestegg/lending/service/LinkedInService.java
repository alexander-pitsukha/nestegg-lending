package by.nestegg.lending.service;

import by.nestegg.lending.dto.linkedin.LinkedInDto;
import by.nestegg.lending.dto.linkedin.LinkedInDataDto;

import java.util.Map;

public interface LinkedInService {

    LinkedInDto getUserData(String accessToken);

    Map getUserData();

    LinkedInDataDto getLinkedInData();

}
