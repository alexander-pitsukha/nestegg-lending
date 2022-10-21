package by.nestegg.lending.util;

import by.nestegg.lending.entity.domain.Identifiable;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public class IdentifiableUtils {

    public final Comparator<Identifiable<? extends Comparable>> NULLS_FIRST_IDENTIFIABLE_COMPARATOR =
            Comparator.nullsFirst(Comparator.comparing(Identifiable::getId));

    public <T, I extends Identifiable<T>> boolean isEmpty(I identifiable) {
        return identifiable == null || identifiable.getId() == null;
    }

    public <I> List<I> toIdListOrEmptyList(Collection<? extends Identifiable<I>> identifiables) {
        if (CollectionUtils.isEmpty(identifiables)) {
            return List.of();
        } else {
            return identifiables.stream().map(Identifiable::getId).collect(Collectors.toList());
        }
    }

    public <T> boolean equalsById(Identifiable<T> o1, T id) {
        return o1 != null && id != null && o1.getId() != null && Objects.equals(o1.getId(), id);
    }

    public <T> boolean equalsById(Identifiable<T> o1, Identifiable<T> o2) {
        if (o1 == o2) {
            return true;
        }
        return o1 != null && o2 != null && o1.getId() != null && Objects.equals(o1.getId(), o2.getId());
    }

    public <T> boolean existsById(Set<T> list, T id) {
        return id != null && list.contains(id);
    }

    public <T> boolean existsById(Set<T> list, Identifiable<T> identifiable) {
        var id = identifiableToIdNullable(identifiable);
        return existsById(list, id);
    }

    public <T, I extends Identifiable<T>> boolean existsById(Collection<I> list, T id) {
        return findFirstByIdNullable(list, id) != null;
    }

    public <T, I extends Identifiable<T>> boolean existsById(Collection<I> list, Identifiable<T> identifiable) {
        return findFirstByIdNullable(list, identifiable) != null;
    }

    public static <T> boolean existsByIdSimple(Collection<T> list, T id) {
        return id != null && list.contains(id);
    }

    public <T> boolean existsByIdSimple(Collection<T> list, Identifiable<T> identifiable) {
        return identifiable != null && existsByIdSimple(list, identifiable.getId());
    }

    public <T, I extends Identifiable<T>> I findFirstByIdNullable(Collection<I> list, T id) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        for (I item : list) {
            if (equalsById(item, id)) {
                return item;
            }
        }
        return null;
    }

    public <T, I extends Identifiable<T>> I findFirstByIdNullable(Collection<I> list,
                                                                  Identifiable<T> identifiable) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        for (I item : list) {
            if (equalsById(item, identifiable)) {
                return item;
            }
        }
        return null;
    }

    public <T> T identifiableToIdNullable(Identifiable<T> identifiable) {
        return identifiable == null ? null : Objects.requireNonNull(identifiable.getId(),
                "id should not be null in identifiable");
    }

}
