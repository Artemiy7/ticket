package net.ticket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import net.ticket.config.ValidatingSerializerModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;


import javax.annotation.PostConstruct;
import javax.validation.Validator;
import java.util.TimeZone;

@EnableEurekaClient
@EnableTransactionManagement
@SpringBootApplication//(exclude = {HibernateJpaAutoConfiguration.class, JpaRepositoriesAutoConfiguration.class})
public class TicketPaymentConfiguration {
    @Autowired
    Jackson2ObjectMapperBuilder builder;
    @Value("${server.timeZone}")
    private String timeZone;
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    }

//    @Bean
//    public ObjectMapper objectMapper() {
//        return new Jackson2ObjectMapperBuilder().createXmlMapper(false).build().registerModule(new SimpleModule()
//                .setSerializerModifier(new ValidatingSerializerModifier()));
//
//    }

    public static void main(String[] args) {
        SpringApplication.run(TicketPaymentConfiguration.class, args);
    }
}