package com.example.demo.common.events;

import com.example.demo.common.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountActivatedEvent {
    private String id;
    private AccountStatus status;
}
