package by.nestegg.lending.entity.domain;

public interface ModifiableIdentifiable<T> extends Identifiable<T> {

    void setId(T id);

}
