package Pismo.demo.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AccountResponse {
    private Long accountId;
    private String documentNumber;
}