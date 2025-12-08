package ma.enset.hamzaelghazouani.demo.query.entities;

import ma.enset.hamzaelghazouani.demo.common.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Date;

@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    private String id;

    private double balance;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    private String currency;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY)
    private Collection<AccountOperation> operations;
}
