package by.nestegg.lending.mapper;

import java.util.List;

public interface DtoEntityMapper<D, E> {

    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntities(List<D> dtos);

    List<D> toDtos(List<E> entities);

}
