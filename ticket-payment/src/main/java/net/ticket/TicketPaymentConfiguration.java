package net.ticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;

@EnableEurekaClient
@EnableTransactionManagement
@SpringBootApplication(exclude = {HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
public class TicketPaymentConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(TicketPaymentConfiguration.class, args);
    }

    @LoadBalanced
    @Bean
    public RestTemplate loadBalancedRestTemplate(){
        return new RestTemplate();
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}