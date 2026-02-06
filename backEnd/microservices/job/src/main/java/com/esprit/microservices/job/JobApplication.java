package com.esprit.microservices.job;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class JobApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobApplication.class, args);
    }

    @Bean
    ApplicationRunner init(JobRepository repository) {
        return args -> {
            if (repository.count() == 0) {
                repository.save(new Job("Développeur Java", "oui"));
                repository.save(new Job("Ingénieur DevOps", "oui"));
                repository.save(new Job("Data Analyst", "non"));
                repository.save(new Job("Product Manager", "oui"));
            }
            repository.findAll().forEach(System.out::println);
        };
    }
}
