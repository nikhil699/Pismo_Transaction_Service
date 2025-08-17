package Pismo.demo.controller;


import Pismo.demo.dto.TransactionRequest;
import Pismo.demo.dto.TransactionResponse;
import Pismo.demo.entities.Transaction;
import Pismo.demo.mapper.TransactionMapper;
import Pismo.demo.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Transactions")
public class TransactionController {

    private final TransactionService txService;

    public TransactionController(TransactionService txService) {
        this.txService = txService;
    }

    @Operation(
            summary = "Create transaction",
            description = "Amount हमेशा positive भेजिए; service business rules के अनुसार sign set करेगी.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created",
                            content = @Content(schema = @Schema(implementation = TransactionResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error / Unknown OperationType"),
                    @ApiResponse(responseCode = "404", description = "Account / OperationType not found")
            }
    )
    @PostMapping
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionRequest req) {
        Transaction created = txService.createTransaction(
                req.getAccountId(), req.getOperationTypeId(), req.getAmount()
        );
        return ResponseEntity
                .status(201)
                .body(TransactionMapper.toResponse(created));
    }
}
