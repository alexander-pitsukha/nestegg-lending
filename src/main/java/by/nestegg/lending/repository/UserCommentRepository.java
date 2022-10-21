package by.nestegg.lending.repository;

import by.nestegg.lending.entity.UserComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCommentRepository extends JpaRepository<UserComment, Long> {

    @Query("select u from UserComment u where u.parentUserComment.id is null and u.user.id = :userId")
    List<UserComment> findAllByUserId(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true)
    @Query("update UserComment u set u.comment = :comment where u.id = :id")
    void updateUserCommentById(@Param("id") Long id, @Param("comment") String comment);

}
