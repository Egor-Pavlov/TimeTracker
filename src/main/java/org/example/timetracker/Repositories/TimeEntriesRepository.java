package org.example.timetracker.Repositories;

import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
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
    @Query("SELECT * FROM time_entry WHERE user_Id = :userId AND task_Id = :taskId;")
    public TimeEntry findByUserIdAndTaskId(Long userId, Long taskId);

    @Query("SELECT COUNT(*) FROM time_entry WHERE user_Id = :userId\n" +
            "AND task_Id = :taskId AND endTime IS NULL;")
    public boolean existsByUserIdAndTaskId(Long userId, Long taskId);

    @Modifying
    @Query("INSERT INTO time_entry (user_Id, task_Id, startTime)\n" +
            "VALUES (:userId, :taskId, :startTime);")
    public void save(Long userId, Long taskId, Timestamp startTime);

    @Modifying
    @Query("update time_entry set endtime = :endTime and duration = :duration where id = :id ")
    public void update(Long id, Timestamp endTime, double duration);




}
