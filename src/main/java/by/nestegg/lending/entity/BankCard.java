package by.nestegg.lending.entity;

import by.nestegg.lending.entity.domain.AbstractAuditable;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "bank_cards")
public class BankCard extends AbstractAuditable {

    @Column(name = "card_number", unique = true)
    private String cardNumber;

    @Column(name = "cvv")
    private String cvv;

    @Column(name = "expire_date")
    private String expireDate;

    @Column(name = "card_holder_name")
    private String cardholderName;

    @Column(name = "is_default")
    private Boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

}
