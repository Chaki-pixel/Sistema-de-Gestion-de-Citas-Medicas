package com.tuorg.businesslogic.config;

import com.tuorg.businesslogic.services.appointment.InMemoryLockProvider;
import com.tuorg.businesslogic.services.appointment.LockProvider;

import java.beans.JavaBean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.tuorg.businesslogic"})
public class BusinessLogicConfig {

    @JavaBean
    public LockProvider lockProvider() {
        return new InMemoryLockProvider();
    }
}
