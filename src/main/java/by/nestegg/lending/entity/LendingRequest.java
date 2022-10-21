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
@Table(name = "lending_requests")
public class LendingRequest extends AbstractTimestamp {

    @Embedded
    private Request request;

}
