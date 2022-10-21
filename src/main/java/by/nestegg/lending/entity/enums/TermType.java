package by.nestegg.lending.entity.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public enum TermType {

    DAY("day"), MONTH("month");

    private String type;

}
