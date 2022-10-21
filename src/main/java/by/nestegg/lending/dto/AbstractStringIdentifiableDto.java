package by.nestegg.lending.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractStringIdentifiableDto implements IdentifiableDto<String> {

    private String id;

}
