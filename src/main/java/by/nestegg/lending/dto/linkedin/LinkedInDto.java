package by.nestegg.lending.dto.linkedin;

import by.nestegg.lending.dto.AbstractStringIdentifiableDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkedInDto extends AbstractStringIdentifiableDto {

    @JsonProperty("localizedFirstName")
    private String firstName;

    @JsonProperty("localizedLastName")
    private String lastName;

}
