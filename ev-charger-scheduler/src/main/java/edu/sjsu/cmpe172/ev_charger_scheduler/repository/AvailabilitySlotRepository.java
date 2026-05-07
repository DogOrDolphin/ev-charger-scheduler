package edu.sjsu.cmpe172.ev_charger_scheduler.repository;

import edu.sjsu.cmpe172.ev_charger_scheduler.model.AvailabilitySlot;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Repository
public class AvailabilitySlotRepository {

    private final JdbcTemplate jdbc;

    public AvailabilitySlotRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<AvailabilitySlot> slotRowMapper = (rs, rowNum) -> {
        AvailabilitySlot s = new AvailabilitySlot();
        s.setSlotId(rs.getLong("slot_id"));
        s.setChargerId(rs.getLong("charger_id"));
        s.setChargerLabel(rs.getString("label"));
        s.setLocation(rs.getString("location"));
        Timestamp st = rs.getTimestamp("start_time");
        Timestamp et = rs.getTimestamp("end_time");
        s.setStartTime(st.toLocalDateTime());
        s.setEndTime(et.toLocalDateTime());
        s.setStatus(rs.getString("status"));
        s.setVersion(rs.getInt("version"));
        return s;
    };

    public List<AvailabilitySlot> findOpenSlotsByDate(LocalDate date) {
        String sql = """
            SELECT s.slot_id, s.charger_id, c.label, c.location, s.start_time, s.end_time, s.status, s.version
            FROM availability_slots s
            JOIN chargers c ON c.charger_id = s.charger_id
            WHERE s.status = 'OPEN'
              AND s.start_time::date = ?
            ORDER BY s.start_time ASC
            """;
        return jdbc.query(sql, slotRowMapper, date);
    }

    public AvailabilitySlot findById(long slotId) {
        String sql = """
            SELECT s.slot_id, s.charger_id, c.label, c.location, s.start_time, s.end_time, s.status, s.version
            FROM availability_slots s
            JOIN chargers c ON c.charger_id = s.charger_id
            WHERE s.slot_id = ?
            """;
        return jdbc.queryForObject(sql, slotRowMapper, slotId);
    }

    public boolean tryBlockSlot(long slotId) {
        String sql = "UPDATE availability_slots SET status='BLOCKED' WHERE slot_id=? AND status='OPEN'";
        int updated = jdbc.update(sql, slotId);
        return updated == 1;
    }

    public boolean reserveSlot(long slotId, int expectedVersion) {
        String sql = """
        UPDATE availability_slots
        SET status = 'BLOCKED',
            version = version + 1
        WHERE slot_id = ?
          AND status = 'OPEN'
          AND version = ?
        """;

        int rowsUpdated = jdbc.update(sql, slotId, expectedVersion);
        return rowsUpdated == 1;
    }
}