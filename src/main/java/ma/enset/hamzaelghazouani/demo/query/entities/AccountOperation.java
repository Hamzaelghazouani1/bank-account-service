package ma.enset.hamzaelghazouani.demo.query.entities;

import ma.enset.hamzaelghazouani.demo.common.enums.OperationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "account_operations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date operationDate;

    private double amount;

    @Enumerated(EnumType.STRING)
    private OperationType type;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
