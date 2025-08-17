package Pismo.demo.repositories;


import Pismo.demo.entities.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationTypeRepository extends JpaRepository<OperationType, Short> {
}
