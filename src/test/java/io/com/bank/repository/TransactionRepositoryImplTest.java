package io.com.bank.repository;

import io.com.bank.domain.Account;
import io.com.bank.domain.Member;
import io.com.bank.domain.Transaction;
import io.com.bank.domain.TransactionEnum;
import io.com.bank.dummy.DummyObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;

@DataJpaTest // DB 관련 Bean이 다 올라옴
public class TransactionRepositoryImplTest extends DummyObject {

    @Autowired private TransactionRepository transactionRepository;
    @Autowired private AccountRepository accountRepository;
    @Autowired private MemberRepository memberRepository;
    @Autowired private EntityManager em;

    @BeforeEach
    void setUp() {
        autoIncrementReset();
        dataSetting();
        em.clear();
    }


    @Test
    void findTransactionList_test() {
        // given
        Long accountId = 1L;

        // when
        List<Transaction> transactions = transactionRepository.findTransactionList(accountId, String.valueOf(TransactionEnum.ALL), 0);
        transactions.forEach((transaction -> {
            System.out.println("================================================================================");
            System.out.println("총 리스트 수 = " + transactions.size());
            System.out.println("transaction.getId() = " + transaction.getId());
            System.out.println("transaction.getGubun() = " + transaction.getGubun());
            System.out.println("transaction.getAmount() = " + transaction.getAmount());
            System.out.println("transaction.getSender() = " + transaction.getSender());
            System.out.println("transaction.getReceiver() = " + transaction.getReceiver());
            System.out.println("transaction.getWithdrawAccountBalance() = " + transaction.getWithdrawAccountBalance());
            System.out.println("transaction.getDepositAccountBalance() = " + transaction.getDepositAccountBalance());
            System.out.println("transaction.getWithdrawAccount().getMember().getFullname() = " + transaction.getWithdrawAccount().getMember().getFullname());
            System.out.println("================================================================================");
        }));



        // then
    }


    @Test
    void jpa_test1() {
        List<Transaction> transactions = transactionRepository.findAll();
        transactions.forEach((transaction)-> {
            System.out.println("========================================");
            System.out.println("transaction.getId() = " + transaction.getId());
            System.out.println("transaction.getSender() = " + transaction.getSender());
            System.out.println("transaction.getReceiver() = " + transaction.getReceiver());
            System.out.println("transaction.getGubun() = " + transaction.getGubun());
            System.out.println("========================================");
            }
        );
    }


    @Test
    void jpa_test2() {
        List<Transaction> transactions = transactionRepository.findAll();
        transactions.forEach((transaction)-> {
                    System.out.println("========================================");
                    System.out.println("transaction.getId() = " + transaction.getId());
                    System.out.println("transaction.getSender() = " + transaction.getSender());
                    System.out.println("transaction.getReceiver() = " + transaction.getReceiver());
                    System.out.println("transaction.getGubun() = " + transaction.getGubun());
                    System.out.println("========================================");
                }
        );
    }


    void dataSetting() {
        Member ssar = memberRepository.save(newMember("ssar", "쌀"));
        Member cos = memberRepository.save(newMember("cos", "코스,"));
        Member love = memberRepository.save(newMember("love", "러브"));
        Member admin = memberRepository.save(newMember("admin", "관리자"));

        Account ssarAccount1 = accountRepository.save(newAccount(1111L, ssar));
        Account cosAccount = accountRepository.save(newAccount(2222L, cos));
        Account loveAccount = accountRepository.save(newAccount(3333L, love));
        Account ssarAccount2 = accountRepository.save(newAccount(4444L, ssar));

        Transaction withdrawTransaction1 = transactionRepository
                .save(newWithdrawTransaction(ssarAccount1, accountRepository));
        Transaction depositTransaction1 = transactionRepository
                .save(newDepositTransaction(cosAccount, accountRepository));
        Transaction transferTransaction1 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, cosAccount, accountRepository));
        Transaction transferTransaction2 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, loveAccount, accountRepository));
        Transaction transferTransaction3 = transactionRepository
                .save(newTransferTransaction(cosAccount, ssarAccount1, accountRepository));
    }


    void autoIncrementReset() {
        em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE account_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE transaction_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }
}
