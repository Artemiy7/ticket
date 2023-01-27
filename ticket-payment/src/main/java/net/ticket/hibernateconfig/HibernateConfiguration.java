package net.ticket.hibernateconfig;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class HibernateConfiguration {
    @Value("${spring.db.driver}")
    private String DRIVER;

    @Value("${spring.db.password}")
    private String PASSWORD;

    @Value("${spring.db.url}")
    private String URL;

    @Value("${spring.db.username}")
    private String USERNAME;

    @Value("${spring.jpa.properties.hibernate.dialect}")
    private String DIALECT;

    @Value("${spring.jpa.properties.hibernate.hbm2ddl.auto}")
    private String AUTO;

    @Value("${spring.jpa.properties.hibernate.show_sql}")
    private String SHOW_SQL;

    @Value("${spring.jpa.properties.hibernate.packagesToScan}")
    private String PACKAGES_TO_SCAN;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName(DRIVER);
        dataSource.setUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);

        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setJpaVendorAdapter(vendorAdaptor());
        em.setDataSource(dataSource());
        em.setPackagesToScan(PACKAGES_TO_SCAN);
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
        hibernateProperties.put("spring.jpa.properties.hibernate.dialect", DIALECT);
        hibernateProperties.put("spring.jpa.hibernate.show_sql", SHOW_SQL);
        hibernateProperties.put("spring.jpa.hibernate.hbm2ddl.auto", AUTO);
        return hibernateProperties;
    }
}