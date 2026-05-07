package edu.sjsu.cmpe172.ev_charger_scheduler.repository;

import edu.sjsu.cmpe172.ev_charger_scheduler.model.Appointment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class AppointmentRepository {

    private final JdbcTemplate jdbc;

    public AppointmentRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Appointment> apptRowMapper = (rs, rowNum) -> {
        Appointment a = new Appointment();
        a.setAppointmentId(rs.getLong("appointment_id"));
        a.setUserId(rs.getLong("user_id"));
        a.setSlotId(rs.getLong("slot_id"));
        a.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        a.setCreatedAt(ts.toLocalDateTime());
        return a;
    };

    public Appointment create(long userId, long slotId) {
        // Postgres supports RETURNING for easy insert+fetch
        String sql = """
            INSERT INTO appointments (user_id, slot_id, status)
            VALUES (?, ?, 'BOOKED')
            RETURNING appointment_id, user_id, slot_id, status, created_at
            """;
        return jdbc.queryForObject(sql, apptRowMapper, userId, slotId);
    }

    public List<Appointment> findByUserId(long userId) {
        String sql = """
            SELECT appointment_id, user_id, slot_id, status, created_at
            FROM appointments
            WHERE user_id = ?
            ORDER BY created_at DESC
            """;
        return jdbc.query(sql, apptRowMapper, userId);
    }

    public Appointment findById(long appointmentId) {
        String sql = """
            SELECT appointment_id, user_id, slot_id, status, created_at
            FROM appointments
            WHERE appointment_id = ?
            """;
        return jdbc.queryForObject(sql, apptRowMapper, appointmentId);
    }
}