package Pismo.demo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class TransactionResponse {
    private Long transactionId;
    private Long accountId;
    private Short operationTypeId;
    private BigDecimal amount;
    private BigDecimal balance;
    private OffsetDateTime eventDate;
}
