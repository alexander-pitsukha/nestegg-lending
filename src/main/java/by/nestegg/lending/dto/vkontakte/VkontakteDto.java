package by.nestegg.lending.dto.vkontakte;

import by.nestegg.lending.dto.AbstractStringIdentifiableDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VkontakteDto extends AbstractStringIdentifiableDto {

    private Response response;

    public String getFirstName() {
        return response.getFirstName();
    }

    public String getLastName() {
        return response.getLastName();
    }

    @Getter
    @Setter
    static class Response extends AbstractStringIdentifiableDto {

        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;

        private String email;

    }

}
