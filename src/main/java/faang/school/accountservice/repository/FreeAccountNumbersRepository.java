package faang.school.accountservice.repository;

import faang.school.accountservice.entity.account.numbers.FreeAccountNumber;
import faang.school.accountservice.entity.account.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FreeAccountNumbersRepository extends JpaRepository<FreeAccountNumber, String> {

    long countByAccountType(AccountType accountType);

    @Query(nativeQuery = true, value = """
            DELETE FROM free_account_numbers
            WHERE account_number = (
            SELECT account_number
            FROM free_account_numbers
            WHERE account_type = :accountType
            ORDER BY created_at ASC
            LIMIT 1)
            RETURNING account_number
            """)
    @Modifying
    Optional<String> deleteAndReturnFirstByAccountTypeOrderByCreatedAtAsc(@Param("accountType") String accountType);
}