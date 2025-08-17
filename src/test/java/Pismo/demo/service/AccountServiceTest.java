package Pismo.demo.service;


import Pismo.demo.entities.Account;
import Pismo.demo.repositories.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class AccountServiceTest {

    private final AccountRepository repo = Mockito.mock(AccountRepository.class);
    private final AccountService service = new AccountService(repo);

    @Test
    void testCreateAccount() {
        Account acc = new Account();
        acc.setId(1L);
        acc.setDocumentNumber("12345678900");

        when(repo.save(Mockito.any(Account.class))).thenReturn(acc);

        Account saved = service.createAccount("12345678900");

        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getDocumentNumber()).isEqualTo("12345678900");
    }

    @Test
    void testGetAccountSuccess() {
        Account acc = new Account(1L, "12345678900");
        when(repo.findById(1L)).thenReturn(Optional.of(acc));

        Account found = service.getAccount(1L);

        assertThat(found.getDocumentNumber()).isEqualTo("12345678900");
    }


}
