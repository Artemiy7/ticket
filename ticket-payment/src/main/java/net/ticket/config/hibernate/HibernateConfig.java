package net.ticket.config.hibernate;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class HibernateConfig {

    private final HibernatePropertiesConfig hibernatePropertiesConfig;

    public HibernateConfig(HibernatePropertiesConfig hibernatePropertiesConfig) {
        this.hibernatePropertiesConfig = hibernatePropertiesConfig;
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(hibernatePropertiesConfig.getDriver());
        dataSource.setUrl(hibernatePropertiesConfig.getUrl());
        dataSource.setUsername(hibernatePropertiesConfig.getUsername());
        dataSource.setPassword(hibernatePropertiesConfig.getPassword());

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setJpaVendorAdapter(vendorAdaptor());
        em.setDataSource(dataSource());
        em.setPackagesToScan(hibernatePropertiesConfig.getPackagesToScan());
        em.setPersistenceProviderClass(HibernatePersistenceProvider.class);
        em.setJpaProperties(additionalProperties());
        return em;
    }

    @Bean
    public JpaTransactionManager jpaTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }


    private HibernateJpaVendorAdapter vendorAdaptor() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        return vendorAdapter;
    }

    Properties additionalProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.put("spring.jpa.properties.hibernate.dialect", hibernatePropertiesConfig.getDialect());
        hibernateProperties.put("spring.jpa.hibernate.show_sql", hibernatePropertiesConfig.getShowSql());
        hibernateProperties.put("spring.jpa.hibernate.hbm2ddl.auto", hibernatePropertiesConfig.getAuto());
        return hibernateProperties;
    }
}