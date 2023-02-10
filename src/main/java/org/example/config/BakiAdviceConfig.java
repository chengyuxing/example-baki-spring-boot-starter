package org.example.config;

import com.github.chengyuxing.sql.Baki;
import org.example.jpa.Baki4JpaAdvice;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class BakiAdviceConfig {
    private final Baki baki;

    public BakiAdviceConfig(Baki baki) {
        this.baki = baki;
    }

    @Bean
    public Baki4JpaAdvice baki4JpaAdvice() {
        return new Baki4JpaAdvice(baki);
    }
}
