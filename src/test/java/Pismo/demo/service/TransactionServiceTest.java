package Pismo.demo.service;

import Pismo.demo.entities.Account;
import Pismo.demo.entities.OperationType;
import Pismo.demo.entities.Transaction;
import Pismo.demo.exception.NotFoundException;
import Pismo.demo.repositories.AccountRepository;
import Pismo.demo.repositories.OperationTypeRepository;
import Pismo.demo.repositories.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock private TransactionRepository txRepo;
    @Mock private AccountRepository accountRepo;
    @Mock private OperationTypeRepository otRepo;

    @InjectMocks
    private TransactionService service;

    private Account acc;
    private OperationType cash, installment, withdrawal, payment;

    @BeforeEach
    void setUp() {
        acc = Account.builder().id(1L).documentNumber("12345678900").build();
        cash = OperationType.builder().id((short)1).description("CASH PURCHASE").build();
        installment = OperationType.builder().id((short)2).description("INSTALLMENT PURCHASE").build();
        withdrawal = OperationType.builder().id((short)3).description("WITHDRAWAL").build();
        payment = OperationType.builder().id((short)4).description("PAYMENT").build();
    }

    private Transaction negativeTx(Account acc, OperationType op, String amt, String ts) {
        return Transaction.builder()
                .id(null)
                .account(acc)
                .operationType(op)
                .amount(new BigDecimal(amt))
                .balance(new BigDecimal(amt))
                .eventDate(OffsetDateTime.parse(ts))
                .build();
    }

    @Test
    void createTransaction_cash_isSavedAsNegative_andBalanceNegative() {
        when(accountRepo.findById(1L)).thenReturn(Optional.of(acc));
        when(otRepo.findById((short)1)).thenReturn(Optional.of(cash));
        when(txRepo.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        Transaction result = service.createTransaction(1L, (short)1, new BigDecimal("100.50"));

        assertThat(result.getAmount()).isEqualByComparingTo("-100.50");
        assertThat(result.getBalance()).isEqualByComparingTo("-100.50");
    }

    @Test
    void createTransaction_payment_isSavedAsPositive_andBalancePositive() {
        when(accountRepo.findById(1L)).thenReturn(Optional.of(acc));
        when(otRepo.findById((short)4)).thenReturn(Optional.of(payment));
        when(txRepo.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        Transaction result = service.createTransaction(1L, (short)4, new BigDecimal("200.00"));

        assertThat(result.getAmount()).isEqualByComparingTo("200.00");
        assertThat(result.getBalance()).isEqualByComparingTo("200.00");
    }

    @Test
    void createTransaction_throwsWhenAccountMissing() {
        when(accountRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.createTransaction(99L, (short)4, new BigDecimal("1.00")));
    }

    @Test
    void createTransaction_throwsWhenOpTypeMissing() {
        when(accountRepo.findById(1L)).thenReturn(Optional.of(acc));
        when(otRepo.findById((short)9)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> service.createTransaction(1L, (short)9, new BigDecimal("1.00")));
    }

    @Test
    void payment_discharge_FIFOFullyClearsTwoNegatives_andLeavesRemainder() {
        when(accountRepo.findById(1L)).thenReturn(Optional.of(acc));
        when(otRepo.findById((short)4)).thenReturn(Optional.of(payment));
        when(txRepo.save(any(Transaction.class))).thenAnswer(inv -> inv.getArgument(0));

        Transaction n1 = negativeTx(acc, withdrawal, "-150.50", "2025-08-29T16:00:14Z");
        Transaction n2 = negativeTx(acc, withdrawal, "-1950.50", "2025-08-29T16:00:19Z");

        when(txRepo.findByAccount_IdAndBalanceLessThanAndOperationType_IdInOrderByEventDateAsc(
                eq(1L), eq(BigDecimal.ZERO), anyList()))
                .thenReturn(List.of(n1, n2));

        Transaction pay = service.createTransaction(1L, (short)4, new BigDecimal("6050.50"));

        // Both negatives discharged
        assertThat(n1.getBalance()).isEqualByComparingTo("0.00");
        assertThat(n2.getBalance()).isEqualByComparingTo("0.00");
        // Payment leftover
        assertThat(pay.getBalance()).isEqualByComparingTo("3949.50");
    }
}
