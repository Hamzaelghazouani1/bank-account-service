package ma.enset.hamzaelghazouani.demo.query.repositories;

import ma.enset.hamzaelghazouani.demo.query.entities.Account;
import ma.enset.hamzaelghazouani.demo.query.entities.AccountOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<AccountOperation, Long> {
    List<AccountOperation> findByAccountOrderByOperationDateDesc(Account account);
}
