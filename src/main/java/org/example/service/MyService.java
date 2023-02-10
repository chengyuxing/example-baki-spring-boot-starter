package org.example.service;

import com.github.chengyuxing.sql.Args;
import com.github.chengyuxing.sql.Baki;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyService {
    @Autowired
    Baki baki;

//    @Autowired
//    DataSourceTransactionManager dataSourceTransactionManager;

    @Transactional
    public void add() {
        baki.insert("test.tx").save(Args.of("a", 1));
        int i = 1 / 0;
        baki.insert("test.tx").save(Args.of("a", 2));
    }

    public void queryMvn() {
        baki.query("&test.mvn").arg("keywords", "chengyuxing").rows()
                .forEach(System.out::println);
    }

    public void queryRow() {
        baki.query("select 'Hello world!'").findFirst()
                .ifPresent(System.out::println);
    }


//    public void stepAdd() {
//        TransactionStatus status = dataSourceTransactionManager.getTransaction(TransactionDefinition.withDefaults());
//        try {
//            baki.insert("test.tx").save(Args.of("a", 1));
//            int i = 1 / 0;
//            baki.insert("test.tx").save(Args.of("a", 2));
//            dataSourceTransactionManager.commit(status);
//        } catch (Exception e) {
//            dataSourceTransactionManager.rollback(status);
//        }
//    }
}
