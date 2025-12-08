package ma.enset.hamzaelghazouani.demo.query.repositories;

import ma.enset.hamzaelghazouani.demo.query.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
}
