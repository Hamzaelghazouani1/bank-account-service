package ma.enset.hamzaelghazouani.demo.common.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreatedEvent {
    private String id;
    private double initialBalance;
    private String currency;
}
