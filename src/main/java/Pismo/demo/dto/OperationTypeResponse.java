package Pismo.demo.dto;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class OperationTypeResponse {
    private Short operationTypeId;
    private String description;
}
