package io.com.bank.repository;

import io.com.bank.domain.Transaction;
import io.com.bank.domain.TransactionEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

interface Dao {
    List<Transaction> findTransactionList(@Param("accountId") Long accountId,
                                          @Param("gubun") String gubun,
                                          @Param("page") Integer page);


}

@RequiredArgsConstructor
public class TransactionRepositoryImpl implements Dao{

    private final EntityManager em;

    @Override
    public List<Transaction> findTransactionList(Long accountId, String gubun, Integer page) {
        String sql = "";
        sql += "SELECT t " +
                "FROM Transaction t ";

        if (gubun.equals(String.valueOf(TransactionEnum.WITHDRAW))) {
            sql += "JOIN FETCH t.withdrawAccount wa " +
                    "WHERE t.withdrawAccount.id = :withdrawAccountId";
        }
        else if (gubun.equals(String.valueOf(TransactionEnum.DEPOSIT))) {
            sql += "JOIN FETCH t.depositAccount da " +
                    "WHERE t.depositAccount.id = :depositAccountId";
        }
        else {
            sql += "LEFT JOIN FETCH t.withdrawAccount wa " +
                    "LEFT JOIN FETCH t.depositAccount da " +
                    "WHERE t.withdrawAccount.id = :withdrawAccountId " +
                    "OR " +
                    "t.depositAccount.id = :depositAccountId";
        }

        TypedQuery<Transaction> query = em.createQuery(sql, Transaction.class);

        if (gubun.equals(String.valueOf(TransactionEnum.WITHDRAW))) {
            query = query.setParameter("withdrawAccountId", accountId);
        }
        else if (gubun.equals(String.valueOf(TransactionEnum.DEPOSIT))) {
            query = query.setParameter("depositAccountId", accountId);
        }
        else {
            query = query.setParameter("withdrawAccountId", accountId);
            query = query.setParameter("depositAccountId", accountId);
        }

        query.setFirstResult(page * 5);
        query.setMaxResults(5);

        return query.getResultList();
    }
}
