package Pismo.demo.service;

import Pismo.demo.entities.Account;
import Pismo.demo.entities.OperationType;
import Pismo.demo.entities.Transaction;
import Pismo.demo.exception.NotFoundException;
import Pismo.demo.repositories.AccountRepository;
import Pismo.demo.repositories.OperationTypeRepository;
import Pismo.demo.repositories.TransactionRepository;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository txRepo;
    private final AccountRepository accountRepo;
    private final OperationTypeRepository otRepo;

    public TransactionService(TransactionRepository txRepo,
                              AccountRepository accountRepo,
                              OperationTypeRepository otRepo) {
        this.txRepo = txRepo;
        this.accountRepo = accountRepo;
        this.otRepo = otRepo;
    }

    @Transactional
    public Transaction createTransaction(Long accountId, Short operationTypeId, BigDecimal amount) {
        Account account = accountRepo.findById(accountId)
                .orElseThrow(() -> new NotFoundException("Account not found: " + accountId));
        OperationType ot = otRepo.findById(operationTypeId)
                .orElseThrow(() -> new NotFoundException("OperationType not found: " + operationTypeId));

        BigDecimal signedAmount = applyBusinessRule(ot.getId(), amount);

        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setOperationType(ot);
        tx.setAmount(signedAmount);

        if (operationTypeId== 4)
        {
            tx.setBalance(BigDecimal.ZERO);
        }
        else {
            tx.setBalance(signedAmount);
        }
        tx.setEventDate(OffsetDateTime.now());



        if (operationTypeId == 4)
        {
            dischargeTransactions(accountId, signedAmount);
        }
        return txRepo.save(tx);
    }

    private BigDecimal applyBusinessRule(short operationTypeId, BigDecimal amount) {
        // Rule: for CASH(1), INSTALLMENT(2), WITHDRAWAL(3) => negative
        // for PAYMENT(4) => positive
        return switch (operationTypeId) {
            case 1, 2, 3 -> amount.abs().negate();
            case 4 -> amount.abs();
            default -> amount;
        };
    }

    private void dischargeTransactions(Long accountId, BigDecimal payment)
    {
        List<Transaction> debts = txRepo.findByAccount_Id(accountId).stream()
                .filter(t -> t.getBalance().compareTo(BigDecimal.ZERO) < 0)
                .sorted(Comparator.comparing(Transaction::getEventDate))
                .toList();

        BigDecimal remaining = payment ;

        for (Transaction debt : debts)
        {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0 ) break;

            BigDecimal debtAbs = debt.getBalance().abs();

            if (remaining.compareTo(debtAbs) >= 0)
            {
                debt.setBalance(BigDecimal.ZERO);
                remaining = remaining.subtract(debtAbs);
            }
            else{
                debt.setBalance(debt.getBalance().add(remaining));
                remaining = BigDecimal.ZERO;
            }

            txRepo.save(debt);
        }

    }
}