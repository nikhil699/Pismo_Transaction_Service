package Pismo.demo.controller;

import Pismo.demo.dto.AccountRequest;
import Pismo.demo.dto.AccountResponse;
import Pismo.demo.dto.TransactionRequest;
import Pismo.demo.entities.Account;
import Pismo.demo.mapper.AccountMapper;
import Pismo.demo.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;



@RestController
@RequestMapping("/accounts")
@Tag(name = "Accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(
            summary = "Create account",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Created",
                            content = @Content(schema = @Schema(implementation = AccountResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Validation error")
            }
    )
    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody AccountRequest req) {
        Account created = accountService.createAccount(req.getDocumentNumber());
        AccountResponse body = AccountMapper.toResponse(created);
        return ResponseEntity
                .created(URI.create("/accounts/" + created.getId()))
                .body(body);
    }

    @Operation(summary = "Get account by id")
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getById(@PathVariable Long id) {
        Account account = accountService.getAccount(id); // cached
        return ResponseEntity.ok(AccountMapper.toResponse(account));
    }
}
