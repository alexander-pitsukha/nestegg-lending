package by.nestegg.lending.repository;

import by.nestegg.lending.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.nickname = :nickname")
    User findByNickname(@Param("nickname") String nickname);

    @Query("select u from User u where u.deviceToken = :deviceToken")
    User findByDeviceToken(@Param("deviceToken") String deviceToken);

    @Query("select u from User u where u.phoneNumber = :phoneNumber")
    User findByPhoneNumber(@Param("phoneNumber") String phoneNumber);

}
