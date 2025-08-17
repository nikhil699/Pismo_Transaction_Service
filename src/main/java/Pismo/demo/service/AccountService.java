package Pismo.demo.service;

import Pismo.demo.entities.Account;
import Pismo.demo.exception.NotFoundException;
import Pismo.demo.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

@Service
public class AccountService {

    private final AccountRepository repo;

    public AccountService(AccountRepository repo) {
        this.repo = repo;
    }

    public Account createAccount(String documentNumber) {
        Account account = new Account();
        account.setDocumentNumber(documentNumber);
        return repo.save(account);
    }

    @Cacheable("accounts")
    public Account getAccount(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found: " + id));
    }
}