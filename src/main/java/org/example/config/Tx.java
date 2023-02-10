package org.example.config;

import com.github.chengyuxing.sql.exceptions.TransactionException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

//@Component
public class Tx {
    final DataSourceTransactionManager dataSourceTransactionManager;

    public Tx(DataSourceTransactionManager dataSourceTransactionManager) {
        this.dataSourceTransactionManager = dataSourceTransactionManager;
    }

    public void using(Runnable runnable) {
        TransactionStatus status = dataSourceTransactionManager.getTransaction(TransactionDefinition.withDefaults());
        try {
            runnable.run();
            dataSourceTransactionManager.commit(status);
        } catch (Exception e) {
            dataSourceTransactionManager.rollback(status);
            throw new TransactionException("transaction will rollback cause: ", e);
        }
    }
}
