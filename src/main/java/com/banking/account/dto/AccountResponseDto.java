package com.banking.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponseDto {
    private UUID id;
    private String iban;
    private String bicswift;
    private UUID customerId;
    private String voided;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
