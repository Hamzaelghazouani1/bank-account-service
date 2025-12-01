package ma.enset.hamzaelghazouani.demo.commands.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DebitAccountRequestDTO {
    private String accountId;
    private double amount;
    private String currency;
}
