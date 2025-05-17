package com.banking.account.service;

import com.banking.account.dto.AccountRequestDto;
import com.banking.account.dto.AccountResponseDto;
import com.banking.account.mapper.AccountMapper;
import com.banking.account.model.Account;
import com.banking.account.model.QAccount;
import com.banking.account.repository.AccountRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    public AccountResponseDto create(AccountRequestDto dto) {
        return accountMapper.toDto(accountRepository.save(accountMapper.toEntity(dto)));
    }

    public AccountResponseDto update(UUID id, AccountRequestDto dto) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Account does not exist"));
        accountMapper.updateFromDTO(dto, account);
        return accountMapper.toDto(accountRepository.save(account));
    }

    public Optional<AccountResponseDto> get(UUID id) {
        return accountRepository.findById(id).map(accountMapper::toDto);
    }

    public Page<AccountResponseDto> search(String iban, String bicswift, Pageable pageable) {
        BooleanExpression predicate = buildAccountPredicate(iban, bicswift);
        return accountRepository.findAll(predicate, pageable)
                .map(accountMapper::toDto);
    }

    public void delete(UUID id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Account does not exist"));
        account.setVoided(true);
        accountRepository.save(account);
    }

    public static BooleanExpression buildAccountPredicate(String iban, String bicswift) {
        QAccount qAccount = QAccount.account;
        BooleanExpression predicate = qAccount.voided.isFalse();

        if (StringUtils.hasText(iban)) {
            predicate = predicate.and(qAccount.iban.eq(iban));
        }

        if (StringUtils.hasText(bicswift)) {
            predicate = predicate.and(qAccount.bicswift.eq(bicswift));
        }

        return predicate;
    }

}
