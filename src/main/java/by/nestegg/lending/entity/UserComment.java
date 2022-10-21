package by.nestegg.lending.entity;

import by.nestegg.lending.entity.domain.AbstractTimestamp;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user_comments")
public class UserComment extends AbstractTimestamp {

    @Column(name = "comment")
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_comment_id")
    private UserComment parentUserComment;

    @OneToMany(mappedBy = "parentUserComment", cascade = CascadeType.ALL)
    private List<UserComment> userComments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_owner_id", nullable = false, updatable = false)
    private User userOwner;

}
