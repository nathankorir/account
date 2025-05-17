package com.banking.account.mapper;

import com.banking.account.dto.AccountRequestDto;
import com.banking.account.dto.AccountResponseDto;
import com.banking.account.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponseDto toDto(Account account);

    Account toEntity(AccountRequestDto accountRequestDTO);

    void updateFromDTO(AccountRequestDto dto, @MappingTarget Account entity);

}
