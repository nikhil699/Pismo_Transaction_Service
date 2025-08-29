package Pismo.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TransactionRequest {

    @NotNull
    private Long accountId;

    @NotNull
    private Short operationTypeId;

    /**
     * The client will always send a positive amount.
     * According to the business rule, the service layer will store it
     * as negative for PURCHASE/WITHDRAWAL,
     * and will keep it positive for PAYMENT.
     */
    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    @Positive
    private BigDecimal balance;




    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class AccountRequest {

        @NotBlank
        @Size(max = 32)
        private String documentNumber;
    }
}