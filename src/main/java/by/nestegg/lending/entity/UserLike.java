package by.nestegg.lending.entity;

import by.nestegg.lending.entity.domain.AbstractTimestamp;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "user_likes")
public class UserLike extends AbstractTimestamp {

    @Column(name = "is_like", nullable = false)
    private Boolean isLike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_owner_id", nullable = false, updatable = false)
    private User userOwner;
}
