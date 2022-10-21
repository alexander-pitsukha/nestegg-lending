package by.nestegg.lending.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PullRequestDto {

    Map<Integer, List<UserRequestDto>> requestDtoMap;

}
