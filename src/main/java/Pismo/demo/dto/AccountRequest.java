package Pismo.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class AccountRequest {
    @NotBlank
    @Size(max = 32)
    private String documentNumber;
}
