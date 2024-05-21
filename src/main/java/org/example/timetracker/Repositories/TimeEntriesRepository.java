package org.example.timetracker.Repositories;

import org.example.timetracker.DTO.TaskDuration;
import org.example.timetracker.DTO.WorkInterval;
import org.example.timetracker.Models.TimeEntry;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * Time Entry Repository Interface that extends CrudRepository for time entry table (Users tracking).
 * <p>
 * Repository indicates that this interface serves as a repository for Task entities.
 * EnableAspectJAutoProxy enables AspectJ support for AOP in Spring.
 */
@Repository
@EnableAspectJAutoProxy
public interface TimeEntriesRepository extends CrudRepository <TimeEntry, Long> {
    /**
     * Retrieves all time entries.
     *
     * @return a List of TimeEntry objects
     */
    public List<TimeEntry> findAll();

    /**
     * Counts the number of time entries for a specific user.
     *
     * @param userId the ID of the user
     * @return the number of time entries
     */
    public int countByUserId(Long userId);

    /**
     * Stops all active time entries by updating their end time and calculating the duration.
     *
     * @return true if the update was successful, false otherwise
     */
    @Modifying
    @Query("""
        UPDATE time_entry
        SET end_Time = NOW(),  -- Устанавливаем текущее время в end_Time
            duration = ROUND(TIMESTAMPDIFF(SECOND, start_Time, NOW()) / 3600, 2) -- расчет продолжительности и округление до 2 знаков после запятой
        WHERE
            end_Time IS NULL;
    """)
    public boolean stopAll();

    /**
     * Cleans up old time entries based on a specified interval.
     *
     * @param interval the number of days to keep in the database
     * @return the number of time entries deleted
     */
    @Modifying
    @Query("DELETE FROM time_entry WHERE start_Time < CURDATE() - INTERVAL :interval DAY;")
    public int cleanUpOldData(int interval);

    /**
     * Finds an active time entry for a specific user and task.
     *
     * @param userId the ID of the user
     * @param taskId the ID of the task
     * @return the active TimeEntry object
     */
    @Query("SELECT * FROM time_entry WHERE user_Id = :userId AND task_Id = :taskId AND end_time is NULL")
    public TimeEntry findByUserIdAndTaskId(Long userId, Long taskId);

    /**
     * Checks if an active time entry exists for a specific user and task.
     *
     * @param userId the ID of the user
     * @param taskId the ID of the task
     * @return 1 if the entry exists, 0 otherwise
     */
    @Query("SELECT COUNT(*) FROM time_entry WHERE user_Id = :userId\n" +
            "AND task_Id = :taskId AND end_Time IS NULL;")
    public int existsByUserIdAndTaskId(Long userId, Long taskId);

    /**
     * Saves a new time entry for a user and task with the specified start time.
     *
     * @param userId the ID of the user
     * @param taskId the ID of the task
     * @param startTime the start time of the entry
     */
    @Modifying
    @Query("INSERT INTO time_entry (user_Id, task_Id, start_Time)\n" +
            "VALUES (:userId, :taskId, :startTime);")
    public void save(Long userId, Long taskId, Timestamp startTime);

    /**
     * Updates the end time and duration for a specific time entry.
     *
     * @param endTime the end time of the entry
     * @param duration the duration of the entry
     * @param id the ID of the entry to update
     */
    @Modifying
    @Query("update time_entry set end_time = :endTime, duration = :duration where id = :id ")
    public void update(Timestamp endTime, double duration, Long id);

    /**
     * Deletes all time entries for a specific user.
     *
     * @param user_Id the ID of the user
     * @return the number of entries deleted
     */
    @Modifying
    @Query("DELETE FROM time_entry where user_Id = :user_Id")
    public int deleteByUserId(Long user_Id);

    /**
     * Method to get the total duration for a user in a specified period
     * @param id the user id
     * @param start_date the start date of the period
     * @param end_date the end date of the period
     * @return the total duration for the user in the specified period
     */
    @Query("""
            SELECT SUM(duration) AS total_duration
            FROM time_entry
            WHERE user_Id = :id
            AND start_time >= :start_date
            AND end_time <= :end_date
            """)
    public double getTotalDurationForPeriod(Long id, Timestamp start_date, Timestamp end_date);

    /**
     * Method to get a list of TaskDuration objects for a user in a specified period
     * @param id the user id
     * @param start_date the start date of the period
     * @param end_date the end date of the period
     * @return a list of TaskDuration objects representing task durations for the user in the specified period
     */
    @Query("""
    SELECT t.task_Id, SUM(te.duration) AS total_Dur 
    FROM time_entry te JOIN task t ON te.task_Id = t.task_Id 
    WHERE te.user_Id = :id AND te.start_Time >= :start_date
    AND te.start_Time <= :end_date 
    GROUP BY t.task_Id 
    ORDER BY t.creation_date DESC;
        """)
    public List<TaskDuration> getUserDurationsForPeriod(Long id, Timestamp start_date, Timestamp end_date );

    /**
     * Method to get a list of WorkInterval objects for a user in a specified period
     * @param id the user id
     * @param start_date the start date of the period
     * @param end_date the end date of the period
     * @return a list of WorkInterval objects representing work intervals for the user in the specified period
     */
    @Query("""
        SELECT t.task_Id, t.theme, t.description, te.start_Time, te.end_Time 
        FROM time_entry te JOIN task t ON te.task_Id = t.task_Id \
        WHERE te.user_Id = 1 AND start_Time >= :start_date AND  end_Time <= :end_date 
        ORDER BY start_Time DESC;
    """)
    public List<WorkInterval> getUserWorkIntervalsForPeriod(Long id, Timestamp start_date, Timestamp end_date);
}