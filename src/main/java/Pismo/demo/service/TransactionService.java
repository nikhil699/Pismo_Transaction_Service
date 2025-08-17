package Pismo.demo.service;

import Pismo.demo.entities.Account;
import Pismo.demo.entities.OperationType;
import Pismo.demo.entities.Transaction;
import Pismo.demo.exception.NotFoundException;
import Pismo.demo.repositories.AccountRepository;
import Pismo.demo.repositories.OperationTypeRepository;
import Pismo.demo.repositories.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

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
        tx.setEventDate(OffsetDateTime.now());

        return txRepo.save(tx);
    }

    private BigDecimal applyBusinessRule(short operationTypeId, BigDecimal amount) {
        // Rule: for CASH(1), INSTALLMENT(2), WITHDRAWAL(3) => negative
        // for PAYMENT(4) => positive
        return switch (operationTypeId) {
            case 1, 2, 3 -> amount.abs().negate();
            case 4 -> amount.abs();
            default -> amount; // fallback (shouldn’t happen if DB preloaded correctly)
        };
    }
}