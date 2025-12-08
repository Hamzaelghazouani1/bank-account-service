package ma.enset.hamzaelghazouani.demo.query.dto;

import ma.enset.hamzaelghazouani.demo.common.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    private String id;
    private double balance;
    private AccountStatus status;
    private String currency;
    private Date createdAt;
}
