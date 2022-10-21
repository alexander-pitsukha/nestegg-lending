package by.nestegg.lending.repository;

import by.nestegg.lending.controller.view.LikeCountView;
import by.nestegg.lending.controller.view.UserLikeResultView;
import by.nestegg.lending.entity.UserLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLikeRepository extends JpaRepository<UserLike, Long> {

    @Query(value = "select sum(case when u.is_like then 1 else 0 end) as like_count, sum(case when u.is_like = false then 1 else 0 end) as dislike_count from user_likes u where user_id = :userId", nativeQuery = true)
    LikeCountView getLikeAndDislikeCountByUserId(@Param("userId") Long userId);

    @Query(value = "select (select u.is_like from user_likes u where u.user_id = :userId and u.user_owner_id = :userOwnerId) as is_like, sum(case when u.is_like then 1 else 0 end) as like_count, sum(case when u.is_like = false then 1 else 0 end) as dislike_count from user_likes u where user_id = :userId", nativeQuery = true)
    UserLikeResultView getLikeResultAndLikeAndDislikeCountByUserId(@Param("userId") Long userId, @Param("userOwnerId") Long userOwnerId);

    @Query("select u from UserLike u where u.user.id = :userId and u.userOwner.id = :userOwnerId")
    UserLike findByUserIdAndUserOwnerId(@Param("userId") Long userId, @Param("userOwnerId") Long userOwnerId);

    @Query("select case when count(u) > 0 then true else false end from UserLike u where exists (select ul from UserLike ul where ul.user.id = :userId and ul.userOwner.id = :userOwnerId)")
    boolean existsByUserIdAndUserOwnerId(@Param("userId") Long userId, @Param("userOwnerId") Long userOwnerId);

    @Modifying(clearAutomatically = true)
    @Query("update UserLike u set u.isLike = :isLike where u.user.id = :userId and u.userOwner.id = :userOwnerId")
    void updateUserLike(@Param("isLike") Boolean isLike, @Param("userId") Long userId, @Param("userOwnerId") Long userOwnerId);

    @Modifying(clearAutomatically = true)
    @Query("delete from UserLike u where u.user.id = :userId and u.userOwner.id = :userOwnerId")
    void delete(@Param("userId") Long userId, @Param("userOwnerId") Long userOwnerId);

}
