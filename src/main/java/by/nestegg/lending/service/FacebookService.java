package by.nestegg.lending.service;

import by.nestegg.lending.dto.facebook.FacebookDataDto;
import by.nestegg.lending.dto.facebook.FacebookDto;

import java.util.Map;

public interface FacebookService {

    FacebookDto getUserData(String accessToken);

    Map getUserData();

    FacebookDataDto getFacebookUserData();

}
