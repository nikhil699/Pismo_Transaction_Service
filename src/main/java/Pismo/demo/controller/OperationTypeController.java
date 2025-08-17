package Pismo.demo.controller;

import Pismo.demo.dto.OperationTypeResponse;
import Pismo.demo.entities.OperationType;
import Pismo.demo.mapper.OperationTypeMapper;
import Pismo.demo.service.OperationTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/operation-types")
@Tag(name = "OperationTypes")
public class OperationTypeController {

    private final OperationTypeService service;

    public OperationTypeController(OperationTypeService service) {
        this.service = service;
    }

    @Operation(summary = "Get operation type by id")
    @GetMapping("/{id}")
    public ResponseEntity<OperationTypeResponse> get(@PathVariable short id) {
        OperationType ot = service.getById(id); // cached
        return ResponseEntity.ok(OperationTypeMapper.toResponse(ot));
    }
}
