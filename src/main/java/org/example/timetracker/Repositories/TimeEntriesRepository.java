package org.example.timetracker.Repositories;

import org.example.timetracker.DTO.TaskDuration;
import org.example.timetracker.DTO.WorkInterval;
import org.example.timetracker.Models.TimeEntry;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TimeEntriesRepository extends CrudRepository <TimeEntry, Long> {
    public List<TimeEntry> findAll();

    public int countByUserId(Long userId);

    @Query("SELECT * FROM time_entry WHERE user_Id = :userId AND task_Id = :taskId;")
    public TimeEntry findByUserIdAndTaskId(Long userId, Long taskId);

    @Query("SELECT COUNT(*) FROM time_entry WHERE user_Id = :userId\n" +
            "AND task_Id = :taskId AND end_Time IS NULL;")
    public boolean existsByUserIdAndTaskId(Long userId, Long taskId);

    @Modifying
    @Query("INSERT INTO time_entry (user_Id, task_Id, start_Time)\n" +
            "VALUES (:userId, :taskId, :startTime);")
    public void save(Long userId, Long taskId, Timestamp startTime);

    @Modifying
    @Query("update time_entry set end_time = :endTime, duration = :duration where id = :id ")
    public void update(Timestamp endTime, double duration, Long id);

    @Modifying
    @Query("DELETE FROM time_entry where user_Id = :user_Id")
    public int deleteByUserId(Long user_Id);

    @Query("""
            SELECT SUM(duration) AS total_duration
            FROM time_entry
            WHERE user_Id = :id
            AND start_time >= :start_date
            AND end_time <= :end_date
            """)
    public double getTotalDurationForPeriod(Long id, Timestamp start_date, Timestamp end_date);

    @Query("""
    SELECT t.task_Id, SUM(te.duration) AS total_Dur 
    FROM time_entry te JOIN task t ON te.task_Id = t.task_Id 
    WHERE te.user_Id = :id AND te.start_Time >= :start_date
    AND te.start_Time <= :end_date 
    GROUP BY t.task_Id 
    ORDER BY t.creation_date DESC;
        """)
    public List<TaskDuration> getUserDurationsForPeriod(Long id, Timestamp start_date, Timestamp end_date );

    @Query("""
        SELECT t.task_Id, t.theme, t.description, te.start_Time, te.end_Time 
        FROM time_entry te JOIN task t ON te.task_Id = t.task_Id \
        WHERE te.user_Id = 1 AND start_Time >= :start_date AND  end_Time <= :end_date 
        ORDER BY start_Time DESC;
    """)
    public List<WorkInterval> getUserWorkIntervalsForPeriod(Long id, Timestamp start_date, Timestamp end_date);
}