package Pismo.demo.mapper;

import Pismo.demo.dto.OperationTypeResponse;
import Pismo.demo.entities.OperationType;

public final class OperationTypeMapper {
    private OperationTypeMapper() {}

    public static OperationTypeResponse toResponse(OperationType ot) {
        if (ot == null) return null;
        return OperationTypeResponse.builder()
                .operationTypeId(ot.getId())
                .description(ot.getDescription())
                .build();
    }
}
