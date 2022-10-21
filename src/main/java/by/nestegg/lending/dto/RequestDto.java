package by.nestegg.lending.dto;

import by.nestegg.lending.dto.enums.RequestType;
import by.nestegg.lending.entity.enums.TermType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class RequestDto extends AbstractLongIdentifiableDto {

    @NotNull
    private BigDecimal sum;

    @NotNull
    private Double revenueInMouth;

    @NotNull
    private Integer termCount;

    @NotNull
    private TermType termType;

    private LocalDate termDate;

    @NotNull
    private RequestType requestType;

    private Long userId;

}
