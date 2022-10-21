package by.nestegg.lending.repository;

import by.nestegg.lending.entity.UserSocialData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSocialDataRepository extends JpaRepository<UserSocialData, Long> {

    @Query("select s from UserSocialData s where s.user.id = :userId")
    UserSocialData findByUserId(@Param("userId") Long userId);

    @Query("select s from UserSocialData s where s.facebookId = :facebookId")
    UserSocialData findByFacebookId(@Param("facebookId") String facebookId);

    @Query("select s from UserSocialData s where s.linkedinId = :linkedinId")
    UserSocialData findByLinkedinId(@Param("linkedinId") String linkedinId);

    @Query("select s from UserSocialData s where s.instagramId = :instagramId")
    UserSocialData findByInstagramId(@Param("instagramId") String instagramId);

    @Query("select s from UserSocialData s where s.vkontakteId = :vkontakteId")
    UserSocialData findByVkontakteId(@Param("vkontakteId") String vkontakteId);

    @Modifying(clearAutomatically = true)
    @Query("update UserSocialData s set s.facebookId = null, s.facebookToken = null, s.facebookData = null where s.user.id = :userId")
    void updateFacebook(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true)
    @Query("update UserSocialData s set s.linkedinId = null, s.linkedinToken = null, s.linkedinData = null where s.user.id = :userId")
    void updateLinkedIn(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true)
    @Query("update UserSocialData s set s.instagramId = null, s.instagramToken = null, s.instagramData = null where s.user.id = :userId")
    void updateInstagram(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true)
    @Query("update UserSocialData s set s.vkontakteId = null, s.vkontakteToken = null, s.vkontakteData = null where s.user.id = :userId")
    void updateVkontakte(@Param("userId") Long userId);

}
