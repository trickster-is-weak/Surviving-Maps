package uk.co.brett.surviving.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@Configuration
public class ServiceContext {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Bean(name = "ioPoolTaskExecutor")
    public ThreadPoolTaskExecutor ioPoolTaskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(8);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("io-");
        executor.initialize();
        return executor;
    }

}
