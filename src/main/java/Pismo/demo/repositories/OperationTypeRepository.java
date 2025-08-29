package Pismo.demo.repositories;


import Pismo.demo.entities.Account;
import Pismo.demo.entities.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OperationTypeRepository extends JpaRepository<OperationType, Short> {
    Optional<Account> findByDocumentNumber(String documentNumber);
}
