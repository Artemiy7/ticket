package net.ticket.repository.job;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Transactional
@Repository
public class ScheduledJobRepository extends JdbcDaoSupport {
    private final JdbcTemplate jdbcTemplate;

    public ScheduledJobRepository(JdbcTemplate jdbcTemplate,
                                  DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        setDataSource(dataSource);
    }

    public void insertSchedulerLockLog(String instanceName) {
        jdbcTemplate.update("INSERT INTO shedlockLog(instance_name) VALUES (?);", instanceName);
    }
}
