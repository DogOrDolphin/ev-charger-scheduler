package edu.sjsu.cmpe172.ev_charger_scheduler.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final JdbcTemplate jdbc;

    public UserRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public boolean existsById(long userId) {
        Integer count = jdbc.queryForObject("SELECT COUNT(*) FROM users WHERE user_id = ?", Integer.class, userId);
        return count != null && count > 0;
    }
}