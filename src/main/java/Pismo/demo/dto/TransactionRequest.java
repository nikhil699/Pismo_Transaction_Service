package Pismo.demo.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class TransactionRequest {

    @NotNull
    private Long accountId;

    @NotNull
    private Short operationTypeId;

    @NotNull
    @Positive
    private BigDecimal amount;
}
