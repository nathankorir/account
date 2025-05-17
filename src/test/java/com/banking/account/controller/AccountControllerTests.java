package com.banking.account.controller;

import com.banking.account.dto.AccountRequestDto;
import com.banking.account.dto.AccountResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenCreateAccountThenSuccess() throws Exception {
        String json = """
                    {
                      "iban": "DE89370400440532013000",
                      "bicSwift": "DEUTDEFF",
                      "customerId": "123e4567-e89b-12d3-a456-426614174000"
                    }
                """;

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iban").value("DE89370400440532013000"));
    }

    @Test
    void whenGetAccountByIdThenReturnAccount() throws Exception {
        AccountRequestDto request = new AccountRequestDto("DE89370400440532013001", "DEUTDEFF", UUID.randomUUID());

        String response = mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountResponseDto created = objectMapper.readValue(response, AccountResponseDto.class);
        UUID accountId = created.getId();

        mockMvc.perform(get("/accounts" + "/" + accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountId.toString()))
                .andExpect(jsonPath("$.iban").value("DE89370400440532013001"));
    }

    @Test
    void whenUpdateAccountThenSuccess() throws Exception {
        AccountRequestDto request = new AccountRequestDto("DE89370400440532013011", "DGUTDEFF", UUID.randomUUID());

        String response = mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountResponseDto created = objectMapper.readValue(response, AccountResponseDto.class);
        UUID customerId = created.getId();

        AccountRequestDto updateRequest = new AccountRequestDto("DE89370400440532013021", "DGUTWEFF", request.getCustomerId());

        mockMvc.perform(put("/accounts" + "/" + customerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.iban").value("DE89370400440532013021"))
                .andExpect(jsonPath("$.bicSwift").value("DGUTWEFF"));
    }

    @Test
    void whenListAccountsThenSuccess() throws Exception {
        mockMvc.perform(get("/accounts")
                        .param("iban", "DE89370400440532013002")
                        .param("bicSwift", "DEUTDEFG")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").exists());
    }

    @Test
    void whenSoftDeleteAccountThenSuccess() throws Exception {
        AccountRequestDto request = new AccountRequestDto("DE89370400440532013003", "DEUTDEFH", UUID.randomUUID());

        String response = mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        AccountResponseDto created = objectMapper.readValue(response, AccountResponseDto.class);
        UUID accountId = created.getId();

        mockMvc.perform(delete("/accounts" + "/" + accountId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/accounts" + "/" + accountId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(accountId.toString()))
                .andExpect(jsonPath("$.voided").value("true"));
    }
}
