package by.nestegg.lending.repository;

import by.nestegg.lending.entity.BankCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankCardRepository extends JpaRepository<BankCard, Long> {
}
