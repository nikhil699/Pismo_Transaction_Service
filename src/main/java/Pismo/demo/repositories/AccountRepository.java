package Pismo.demo.repositories;



import Pismo.demo.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByDocumentNumber(String documentNumber);
}