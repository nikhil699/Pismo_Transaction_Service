package Pismo.demo.mapper;

import Pismo.demo.dto.AccountResponse;
import Pismo.demo.entities.Account;

public final class AccountMapper {
    private AccountMapper() {}

    public static AccountResponse toResponse(Account account) {
        if (account == null) return null;
        return AccountResponse.builder()
                .accountId(account.getId())
                .documentNumber(account.getDocumentNumber())
                .build();
    }
}
