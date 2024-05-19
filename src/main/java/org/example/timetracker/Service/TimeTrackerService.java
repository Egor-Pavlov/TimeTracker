package org.example.timetracker.Service;

import org.example.timetracker.DTO.TaskDuration;
import org.example.timetracker.DTO.WorkInterval;
import org.example.timetracker.Models.TimeEntry;
import org.example.timetracker.Repositories.TasksRepository;
import org.example.timetracker.Repositories.TimeEntriesRepository;
import org.example.timetracker.Repositories.UsersRepository;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableAspectJAutoProxy
public class TimeTrackerService {
    private final UsersRepository UsersRepository;
    private final TasksRepository TasksRepository;
    private final TimeEntriesRepository timeEntriesRepository;
    public TimeTrackerService(UsersRepository UsersRepository, TasksRepository TasksRepository, TimeEntriesRepository timeEntriesRepository) {
        this.UsersRepository = UsersRepository;
        this.TasksRepository = TasksRepository;
        this.timeEntriesRepository = timeEntriesRepository;
    }

    public Timestamp convertDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return new Timestamp(dateFormat.parse(date).getTime());
    }
    public List<TimeEntry> findAll() {
        return timeEntriesRepository.findAll();
    }
    public int countByUserId(long userId){
        return timeEntriesRepository.countByUserId(userId);
    }

    @Transactional
    public int deleteByUserId(long userId){
        return timeEntriesRepository.deleteByUserId(userId);
    }

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
    @Transactional
    public boolean stopTrackingAll(){
        return timeEntriesRepository.stopAll();
    }

    @Transactional
    public int cleanUpOldData(int days){
        return timeEntriesRepository.cleanUpOldData(days);
    }

    public double getTotalDurationForPeriod(long userID, String startTime, String endTime) throws ParseException {
        return timeEntriesRepository.getTotalDurationForPeriod(userID, convertDate(startTime), convertDate(endTime));
    }

    public List<TaskDuration> getUserDurationsForPeriod(long userID, String startTime, String endTime) throws ParseException {
        return timeEntriesRepository.getUserDurationsForPeriod(userID, convertDate(startTime), convertDate(endTime));
    }

    public List<WorkInterval> getUserWorkIntervalsForPeriod(long userID, String startTime, String endTime) throws ParseException {
        return timeEntriesRepository.getUserWorkIntervalsForPeriod(userID, convertDate(startTime), convertDate(endTime));
    }


}
