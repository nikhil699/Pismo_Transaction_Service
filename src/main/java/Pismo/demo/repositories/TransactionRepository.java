package Pismo.demo.repositories;

import Pismo.demo.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // account relation ke through fetch (correct property path: account.id)
    List<Transaction> findByAccount_Id(Long accountId);

    // 👇 OLD name wrong tha (accountId property entity me nahi hai). Corrected:
    List<Transaction> findByAccount_IdAndBalanceLessThanOrderByEventDateAsc(Long accountId, BigDecimal zero);

    // 🆕 Useful for discharge: sirf purchases/withdrawals (1,2,3) with negative balance, oldest first
    List<Transaction> findByAccount_IdAndBalanceLessThanAndOperationType_IdInOrderByEventDateAsc(
            Long accountId,
            BigDecimal zero,
            Collection<Short> operationTypeIds
    );
}
