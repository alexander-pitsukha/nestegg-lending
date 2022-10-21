package by.nestegg.lending.service;

import by.nestegg.lending.dto.vkontakte.VkontakteDto;

import java.util.Map;

public interface VkontakteService {

    VkontakteDto getUserData(String accessToken);

    Map getUserData();

}
