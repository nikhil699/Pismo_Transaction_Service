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
     * Client हमेशा positive amount भेजेगा.
     * Business rule के अनुसार service layer इसे
     * PURCHASE/WITHDRAWAL के लिए negative में store करेगी,
     * PAYMENT के लिए positive ही रखेगी.
     */
    @NotNull
    @Positive
    private BigDecimal amount;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class AccountRequest {

        @NotBlank
        @Size(max = 32)
        private String documentNumber;
    }
}