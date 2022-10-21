package by.nestegg.lending.entity.data;

import by.nestegg.lending.entity.domain.ModifiableIdentifiable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FacebookData implements ModifiableIdentifiable<String>, Serializable {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

}
