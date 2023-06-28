package io.com.bank.repository;

import io.com.bank.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

//    @Query("SELECT ac FROM Account ac JOIN FETCH ac.member m WHERE ac.number =:number")
    Optional<Account> findByNumber(Long number);

    List<Account> findAllByMemberId(Long memberId);
}
