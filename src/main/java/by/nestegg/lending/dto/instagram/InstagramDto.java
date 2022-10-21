package by.nestegg.lending.dto.instagram;

import by.nestegg.lending.dto.AbstractStringIdentifiableDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InstagramDto extends AbstractStringIdentifiableDto {

    private String username;

    private String email;

}
