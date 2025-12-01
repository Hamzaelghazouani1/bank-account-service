package ma.enset.hamzaelghazouani.demo.common.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDebitedEvent {
    private String id;
    private double amount;
    private String currency;
}
