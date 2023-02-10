package org.example;

import com.github.chengyuxing.sql.Args;
import com.github.chengyuxing.sql.Baki;
import com.github.chengyuxing.sql.XQLFileManager;
import com.github.chengyuxing.sql.spring.autoconfigure.SimpleTx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;

@SpringBootApplication
//@EnableJpaRepositories
public class Startup implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Startup.class, args);
    }

    @Autowired
    XQLFileManager xqlFileManager;
    @Autowired
    SimpleTx tx;
    @Autowired
    Baki baki;
    @Override
    public void run(String... args) throws Exception {
        // 使用自动提交/回滚事务
        int i = tx.using(() -> {
            int a = baki.insert("test.temp")
                    .save(Args.create("pkid", UUID.randomUUID()));
            int b = baki.insert("test.temp")
                    .save(Args.create("pkid", UUID.randomUUID()));
            return a + b;
        });

        System.out.println(i);
        baki.query("&test.mvn")
                .arg("keywords", "abc")
                .stream()
                .forEach(System.out::println);
    }
}
