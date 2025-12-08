package com.example.demo.query.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatementDTO {
    private AccountDTO account;
    private List<OperationDTO> operations;
}
