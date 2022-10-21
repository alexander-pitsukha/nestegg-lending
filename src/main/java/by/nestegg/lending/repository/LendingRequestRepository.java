package by.nestegg.lending.repository;

import by.nestegg.lending.entity.LendingRequest;
import by.nestegg.lending.entity.enums.TermType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LendingRequestRepository extends JpaRepository<LendingRequest, Long> {

    @Query("select case when count(l) > 0 then true else false end from LendingRequest l where exists (select lr from LendingRequest lr where lr.request.user.id = :userId)")
    boolean existsByUserId(@Param("userId") Long userId);

    @Query("select l from LendingRequest l where l.request.sum <= :sum and l.request.revenueInMouth = :revenueInMouth and l.request.termCount = :termCount and l.request.termType = :termType")
    List<LendingRequest> findAllBySumLessThanEqualAndRevenueInMouthAndTermCountAndTermType(@Param("sum") BigDecimal sum, @Param("revenueInMouth") Double revenueInMouth, @Param("termCount") Integer termCount, @Param("termType") TermType termType);

}
