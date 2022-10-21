package by.nestegg.lending.entity.domain;

import by.nestegg.lending.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractAuditable extends AbstractIdentifiable<Long> {

    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "create_by", nullable = false, updatable = false)
    private User createBy;

    @CreatedDate
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "modify_by", insertable = false)
    private User lastModifiedBy;

    @LastModifiedDate
    @Column(name = "modify_date", insertable = false)
    private LocalDateTime modifyDate;

}
