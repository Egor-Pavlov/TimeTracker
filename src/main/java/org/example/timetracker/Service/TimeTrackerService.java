package org.example.timetracker.Service;

import org.example.timetracker.DTO.TaskDuration;
import org.example.timetracker.DTO.WorkInterval;
import org.example.timetracker.Models.TimeEntry;
import org.example.timetracker.Repositories.TasksRepository;
import org.example.timetracker.Repositories.TimeEntriesRepository;
import org.example.timetracker.Repositories.UsersRepository;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Service class for managing time tracking functionalities.
 */
@Service
@EnableAspectJAutoProxy
public class TimeTrackerService {
    private final UsersRepository UsersRepository;
    private final TasksRepository TasksRepository;
    private final TimeEntriesRepository timeEntriesRepository;
    /**
     * Constructs a new TimeTrackerService with the specified repositories.
     *
     * @param UsersRepository the user repository
     * @param TasksRepository the task repository
     * @param timeEntriesRepository the time entries repository
     */
    public TimeTrackerService(UsersRepository UsersRepository, TasksRepository TasksRepository, TimeEntriesRepository timeEntriesRepository) {
        this.UsersRepository = UsersRepository;
        this.TasksRepository = TasksRepository;
        this.timeEntriesRepository = timeEntriesRepository;
    }

    //Конвертация даты из строки в Timestamp для использования в запросах к БД
    /**
     * Converts a date string to a Timestamp object for database queries.
     *
     * @param date the date string to be converted
     * @return the converted Timestamp object
     * @throws ParseException if the date string is not in the correct format
     */
    public Timestamp convertDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return new Timestamp(dateFormat.parse(date).getTime());
    }
    //Получение всех записей трекинга пользователей
    /**
     * Retrieves all time tracking entries.
     *
     * @return the list of all time entries
     */
    public List<TimeEntry> findAll() {
        return timeEntriesRepository.findAll();
    }

    //Получение количества записей трекинга для пользователя
    /**
     * Retrieves the count of time tracking entries for a specific user.
     *
     * @param userId the user ID
     * @return the count of time entries for the user
     */
    public int countByUserId(long userId){
        return timeEntriesRepository.countByUserId(userId);
    }

    //Удаление трекинга пользователя
    /**
     * Deletes all time tracking entries for a specific user.
     *
     * @param userId the user ID
     * @return the number of deleted entries
     */
    @Transactional
    public int deleteByUserId(long userId){
        return timeEntriesRepository.deleteByUserId(userId);
    }

    //Начать отсчет времени по задаче X для пользователя Y
    /**
     * Starts tracking time for a specific task for a specific user.
     *
     * @param userID the user ID
     * @param taskID the task ID
     * @return true if tracking started successfully, false otherwise
     */
    @Transactional
    public boolean startTracking(long userID, long taskID) {
        //Проверить, что пользователь существует
        //проверить что задача существует
        if (UsersRepository.existsById(userID) && TasksRepository.existsById(taskID)) {
            //проверить что нет записи в таблице timeEntry для этого пользователя и этой задачи, в которой нет endtime
            if(timeEntriesRepository.existsByUserIdAndTaskId(userID, taskID) == 0){
                //Создать запись, указать id пользователя и задачи, дату и время.
                timeEntriesRepository.save(userID, taskID, new Timestamp(System.currentTimeMillis()));
                return true;
            }
        }
        return false;
    }

    //Остановить отсчет времени по задаче X для пользователя Y
    /**
     * Stops tracking time for a specific task for a specific user.
     *
     * @param userID the user ID
     * @param taskID the task ID
     * @return true if tracking stopped successfully, false otherwise
     */
    @Transactional
    public boolean stopTracking(long userID, long taskID) {
        //Проверить, что пользователь существует
        //проверить что задача существует
        if (UsersRepository.existsById(userID) && TasksRepository.existsById(taskID)) {
            //проверить что есть запись в таблице timeEntry для этого пользователя и этой задачи, в которой нет endtime
            if(timeEntriesRepository.existsByUserIdAndTaskId(userID, taskID) > 0){
                //установить время окончания и вычислить продолжительность
                TimeEntry timeEntry = timeEntriesRepository.findByUserIdAndTaskId(userID, taskID);
                timeEntry.setEndTime(new Timestamp(System.currentTimeMillis()));
                timeEntriesRepository.update(timeEntry.getEndTime(), timeEntry.getDuration(), timeEntry.getId());
                return true;
            }
        }
        return false;
    }
    //Остановить трекинг всех пользователей
    /**
     * Stops tracking time for all users.
     *
     * @return true if tracking stopped successfully, false otherwise
     */
    @Transactional
    public boolean stopTrackingAll(){
        return timeEntriesRepository.stopAll();
    }

    //Удалить данные трекинга, которые старше N дней
    /**
     * Deletes time tracking entries older than the specified number of days.
     *
     * @param days the number of days
     * @return the number of deleted entries
     */
    @Transactional
    public int cleanUpOldData(int days){
        return timeEntriesRepository.cleanUpOldData(days);
    }

    //Получить сумму трудозатрат пользователя за период
    /**
     * Retrieves the total work duration for a specific user over a specified period.
     *
     * @param userID the user ID
     * @param startTime the start time of the period
     * @param endTime the end time of the period
     * @return the total duration of work
     * @throws ParseException if the date strings are not in the correct format
     */
    public double getTotalDurationForPeriod(long userID, String startTime, String endTime) throws ParseException {
        return timeEntriesRepository.getTotalDurationForPeriod(userID, convertDate(startTime), convertDate(endTime));
    }

    //Получить трудозатраты пользователя по задачам за период
    /**
     * Retrieves the work durations for a specific user over a specified period, grouped by task.
     *
     * @param userID the user ID
     * @param startTime the start time of the period
     * @param endTime the end time of the period
     * @return the list of task durations
     * @throws ParseException if the date strings are not in the correct format
     */
    public List<TaskDuration> getUserDurationsForPeriod(long userID, String startTime, String endTime) throws ParseException {
        return timeEntriesRepository.getUserDurationsForPeriod(userID, convertDate(startTime), convertDate(endTime));
    }

    //Получить интервалы работы пользователя за период
    /**
     * Retrieves the work intervals for a specific user over a specified period.
     *
     * @param userID the user ID
     * @param startTime the start time of the period
     * @param endTime the end time of the period
     * @return the list of work intervals
     * @throws ParseException if the date strings are not in the correct format
     */
    public List<WorkInterval> getUserWorkIntervalsForPeriod(long userID, String startTime, String endTime) throws ParseException {
        return timeEntriesRepository.getUserWorkIntervalsForPeriod(userID, convertDate(startTime), convertDate(endTime));
    }
}
