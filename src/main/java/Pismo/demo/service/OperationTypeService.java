package Pismo.demo.service;

import Pismo.demo.entities.OperationType;
import Pismo.demo.exception.NotFoundException;
import Pismo.demo.repositories.OperationTypeRepository;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;

@Service
public class OperationTypeService {

    private final OperationTypeRepository repo;

    public OperationTypeService(OperationTypeRepository repo) {
        this.repo = repo;
    }

    @Cacheable("operationTypes")
    public OperationType getById(short id) {
        return repo.findById(id)
                .orElseThrow(() -> new NotFoundException("OperationType not found: " + id));
    }
}