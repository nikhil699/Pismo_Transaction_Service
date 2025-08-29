package Pismo.demo.repositories;

import Pismo.demo.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount_Id(Long accountId);

    // 👇 OLD method hata do (signature mismatch karta tha)
    // List<Transaction> findByAccountIdAndBalanceLessThanOrderByEventDateAsc(Long accountId, BigDecimal zero);

    // 👇 NEW: service me jis method ka use ho raha hai wahi add karo
    List<Transaction> findByAccount_IdAndBalanceLessThanAndOperationType_IdInOrderByEventDateAsc(
            Long accountId,
            BigDecimal balance,
            List<Short> operationTypeIds
    );
}
