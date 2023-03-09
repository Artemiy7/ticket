package net.ticket.config.hibernate;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.db")
public class HibernatePropertiesConfig {
    private String driver;
    private String password;
    private String url;
    private String username;
    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String dialect;
    @Value("${spring.jpa.properties.hibernate.hbm2ddl.auto}")
    private String auto;
    @Value("${spring.jpa.properties.hibernate.show_sql}")
    private String showSql;
    @Value("${spring.jpa.properties.hibernate.packagesToScan}")
    private String packagesToScan;
}
