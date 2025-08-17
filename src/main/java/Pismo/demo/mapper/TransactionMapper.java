package Pismo.demo.mapper;

import Pismo.demo.dto.TransactionResponse;
import Pismo.demo.entities.Transaction;

public final class TransactionMapper {
    private TransactionMapper() {}

    public static TransactionResponse toResponse(Transaction tx) {
        if (tx == null) return null;
        return TransactionResponse.builder()
                .transactionId(tx.getId())
                .accountId(tx.getAccount() != null ? tx.getAccount().getId() : null)
                .operationTypeId(tx.getOperationType() != null ? tx.getOperationType().getId() : null)
                .amount(tx.getAmount())
                .eventDate(tx.getEventDate())
                .build();
    }
}