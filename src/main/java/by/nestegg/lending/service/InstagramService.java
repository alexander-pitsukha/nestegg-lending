package by.nestegg.lending.service;

import by.nestegg.lending.dto.instagram.InstagramDto;

import java.util.Map;

public interface InstagramService {

    InstagramDto getUserData(String accessToken);

    Map getUserData();

}
