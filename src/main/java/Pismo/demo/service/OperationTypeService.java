package Pismo.demo.service;

import Pismo.demo.entities.OperationType;
import Pismo.demo.exception.NotFoundException;
import Pismo.demo.repositories.OperationTypeRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class OperationTypeService {

    private final OperationTypeRepository repo;

    public OperationTypeService(OperationTypeRepository repo) {
        this.repo = repo;
    }

    @Cacheable(value = "operationTypes", key = "#id")
    public OperationType getById(short id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("OperationType not found: " + id));
    }
}
