package by.nestegg.lending.entity;

import by.nestegg.lending.entity.converter.TermConverter;
import by.nestegg.lending.entity.enums.TermType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Embeddable
public class Request implements Serializable {

    @Column(name = "sum")
    private BigDecimal sum;

    @Column(name = "revenue_in_mouth")
    private Double revenueInMouth;

    @Column(name = "term_count")
    private Integer termCount;

    @Convert(converter = TermConverter.class)
    @Column(name = "term_type")
    private TermType termType;

    @Column(name = "term_date")
    private LocalDate termDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

}
