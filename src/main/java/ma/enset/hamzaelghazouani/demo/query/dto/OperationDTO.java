package ma.enset.hamzaelghazouani.demo.query.dto;

import ma.enset.hamzaelghazouani.demo.common.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private OperationType type;
}
