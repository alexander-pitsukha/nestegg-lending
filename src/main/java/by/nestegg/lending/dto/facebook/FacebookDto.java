package by.nestegg.lending.dto.facebook;

import by.nestegg.lending.dto.AbstractStringIdentifiableDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacebookDto extends AbstractStringIdentifiableDto {

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    private String email;

}
