package by.nestegg.lending.dto.linkedin;

import by.nestegg.lending.dto.facebook.FacebookDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.social.linkedin.api.Education;
import org.springframework.social.linkedin.api.LinkedInDate;
import org.springframework.social.linkedin.api.Location;
import org.springframework.social.linkedin.api.Position;

import java.util.List;

@Getter
@Setter
public class LinkedInDataDto extends FacebookDto {

    private LinkedInDate birthDate;

    private List<Position> positions;

    private List<Education> educations;

    private Location location;

    private String profilePicture;

}
