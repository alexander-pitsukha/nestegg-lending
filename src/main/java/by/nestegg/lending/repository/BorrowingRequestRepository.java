package by.nestegg.lending.repository;

import by.nestegg.lending.entity.BorrowingRequest;
import by.nestegg.lending.entity.enums.TermType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface BorrowingRequestRepository extends JpaRepository<BorrowingRequest, Long> {

    @Query("select case when count(b) > 0 then true else false end from BorrowingRequest b where exists (select br from BorrowingRequest br where br.request.user.id = :userId)")
    boolean existsByUserId(@Param("userId") Long userId);

    @Query("select b from BorrowingRequest b where b.request.sum <= :sum and b.request.revenueInMouth = :revenueInMouth and b.request.termCount = :termCount and b.request.termType = :termType")
    List<BorrowingRequest> findBySumLessThanEqualAndRevenueInMouthAndTermCountAndTermType(@Param("sum") BigDecimal sum, @Param("revenueInMouth") Double revenueInMouth, @Param("termCount") Integer termCount, @Param("termType") TermType termType);

}
