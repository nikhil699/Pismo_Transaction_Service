package Pismo.demo.service;


import Pismo.demo.entities.Account;
import Pismo.demo.entities.OperationType;
import Pismo.demo.entities.Transaction;
import Pismo.demo.repositories.AccountRepository;
import Pismo.demo.repositories.OperationTypeRepository;
import Pismo.demo.repositories.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock private TransactionRepository transactionRepository;
    @Mock private AccountRepository accountRepository;
    @Mock private OperationTypeRepository operationTypeRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void createTransaction_success() {
        // GIVEN
        Long accountId = 1L;
        short opId = 4; // PAYMENT
        BigDecimal amount = new BigDecimal("200.50");

        Account acc = Account.builder().id(accountId).documentNumber("12345678900").build();
        OperationType op = OperationType.builder().id(opId).description("PAYMENT").build();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(acc));
        when(operationTypeRepository.findById(opId)).thenReturn(Optional.of(op));

        // saved entity returned by repo.save(...)
        Transaction saved = Transaction.builder()
                .id(10L)
                .account(acc)
                .operationType(op)
                .amount(amount)
                .eventDate(OffsetDateTime.now())
                .build();
        when(transactionRepository.save(any(Transaction.class))).thenReturn(saved);

        // WHEN
        Transaction result = transactionService.createTransaction(accountId, opId, amount);

        // THEN: result echoed from repo.save
        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getAmount()).isEqualByComparingTo("200.50");
        assertThat(result.getAccount().getId()).isEqualTo(1L);
        assertThat(result.getOperationType().getId()).isEqualTo(opId);

        // AND: verify what we tried to save
        ArgumentCaptor<Transaction> txCap = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(txCap.capture());
        Transaction toSave = txCap.getValue();
        assertThat(toSave.getAccount()).isSameAs(acc);
        assertThat(toSave.getOperationType()).isSameAs(op);
        assertThat(toSave.getAmount()).isEqualByComparingTo("200.50");

        verifyNoMoreInteractions(transactionRepository);
    }

}