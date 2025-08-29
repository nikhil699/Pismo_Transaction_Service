package Pismo.demo.repositories;

import Pismo.demo.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount_Id(Long accountId);


    List<Transaction> findByAccount_IdAndBalanceLessThanAndOperationType_IdInOrderByEventDateAsc(
            Long accountId,
            BigDecimal balance,
            List<Short> operationTypeIds
    );
}
