package com.banking.account.controller;

import com.banking.account.dto.AccountRequestDto;
import com.banking.account.dto.AccountResponseDto;
import com.banking.account.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountResponseDto> create(@RequestBody @Valid AccountRequestDto dto) {
        return ResponseEntity.ok(accountService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDto> update(@PathVariable UUID id, @RequestBody AccountRequestDto dto) {
        return ResponseEntity.ok(accountService.update(id, dto));
    }


    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> get(@PathVariable UUID id) {
        return accountService.get(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Page<AccountResponseDto> getAccounts(
            @RequestParam(required = false) String iban,
            @RequestParam(required = false) String bicSwift,
            @RequestParam(required = false) UUID customerId,
            @PageableDefault(size = 10) Pageable pageable) {
        return accountService.search(iban, bicSwift, customerId, pageable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        accountService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
