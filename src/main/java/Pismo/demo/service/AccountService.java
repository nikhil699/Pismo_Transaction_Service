package Pismo.demo.service;

import Pismo.demo.entities.Account;
import Pismo.demo.exception.NotFoundException;
import Pismo.demo.repositories.AccountRepository;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final AccountRepository repo;

    public AccountService(AccountRepository repo) {
        this.repo = repo;
    }

    // CREATE → newly created account ko cache me daal do (key = new id)
    @CachePut(value = "accounts", key = "#result.id")
    public Account createAccount(String documentNumber) {
        Account account = new Account();
        account.setDocumentNumber(documentNumber);
        return repo.save(account);
    }

    // READ → cache hit/hit-miss
    @Cacheable(value = "accounts", key = "#id")
    public Account getAccount(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found: " + id));
    }
}
