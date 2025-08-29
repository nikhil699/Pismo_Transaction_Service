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
import java.util.List;

@Service
public class TransactionService {

    private static final short CASH = 1;
    private static final short INSTALLMENT = 2;
    private static final short WITHDRAWAL = 3;
    private static final short PAYMENT = 4;

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

        // sign rules: purchases/withdrawals negative, payment positive
        BigDecimal signedAmount = switch (operationTypeId) {
            case CASH, INSTALLMENT, WITHDRAWAL -> amount.abs().negate();
            case PAYMENT -> amount.abs();
            default -> amount;
        };

        Transaction tx = new Transaction();
        tx.setAccount(account);
        tx.setOperationType(ot);
        tx.setAmount(signedAmount);
        tx.setEventDate(OffsetDateTime.now());
        tx.setBalance(signedAmount);


        tx = txRepo.save(tx);

        if (operationTypeId == PAYMENT && tx.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            applyPaymentDischarge(tx);
        }

        return tx;
    }

    /**
     * Discharge rule:
     *  - Pick older negative-balance transactions (opTypes 1,2,3) for the same account ordered by eventDate ASC
     *  - Subtract payment into them until payment runs out
     *  - Update both sides' balances
     */
    private void applyPaymentDischarge(Transaction paymentTx) {
        BigDecimal remaining = paymentTx.getBalance(); // always positive here

        List<Transaction> negatives = txRepo.findByAccount_IdAndBalanceLessThanAndOperationType_IdInOrderByEventDateAsc(
                paymentTx.getAccount().getId(),
                BigDecimal.ZERO,
                List.of(CASH, INSTALLMENT, WITHDRAWAL)
        );

        for (Transaction neg : negatives) {
            if (remaining.signum() <= 0) break;

            BigDecimal need = neg.getBalance().abs(); // positive magnitude needed to clear this row
            BigDecimal used = remaining.min(need);

            // apply discharge
            neg.setBalance(neg.getBalance().add(used));       // since neg.balance is negative, +used moves towards zero
            paymentTx.setBalance(paymentTx.getBalance().subtract(used));

            txRepo.save(neg);
            remaining = paymentTx.getBalance();
        }

        txRepo.save(paymentTx);
    }
}
