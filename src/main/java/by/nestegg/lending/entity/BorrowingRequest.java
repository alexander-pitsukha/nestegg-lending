package by.nestegg.lending.entity;

import by.nestegg.lending.entity.domain.AbstractTimestamp;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "borrowing_requests")
public class BorrowingRequest extends AbstractTimestamp {

    @Embedded
    private Request request;

}
