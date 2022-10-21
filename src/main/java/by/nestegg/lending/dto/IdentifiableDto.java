package by.nestegg.lending.dto;

import by.nestegg.lending.entity.domain.Identifiable;
import by.nestegg.lending.entity.domain.ModifiableIdentifiable;

public interface IdentifiableDto<T> extends Identifiable<T>, ModifiableIdentifiable<T> {
}
