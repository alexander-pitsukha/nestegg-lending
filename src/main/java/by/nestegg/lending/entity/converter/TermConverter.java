package by.nestegg.lending.entity.converter;

import by.nestegg.lending.entity.enums.TermType;

import javax.persistence.AttributeConverter;
import java.util.Objects;

public class TermConverter implements AttributeConverter<TermType, String> {

    @Override
    public String convertToDatabaseColumn(TermType termType) {
        return termType.getType();
    }

    @Override
    public TermType convertToEntityAttribute(String type) {
        for (TermType termType : TermType.values()) {
            if (Objects.equals(termType.getType(), type)) {
                return termType;
            }
        }
        return null;
    }

}
