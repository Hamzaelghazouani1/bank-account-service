package ma.enset.hamzaelghazouani.demo.common.events;

import ma.enset.hamzaelghazouani.demo.common.enums.AccountStatus;
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
