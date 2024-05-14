package org.example.timetracker.Service;

import org.example.timetracker.Models.TimeEntry;
import org.example.timetracker.Repositories.TasksRepository;
import org.example.timetracker.Repositories.TimeEntriesRepository;
import org.example.timetracker.Repositories.UsersRepository;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TimeTrackerService {
    private final UsersRepository UsersRepository;
    private final TasksRepository TasksRepository;
    private final TimeEntriesRepository timeEntriesRepository;
    public TimeTrackerService(UsersRepository UsersRepository, TasksRepository TasksRepository, TimeEntriesRepository timeEntriesRepository) {
        this.UsersRepository = UsersRepository;
        this.TasksRepository = TasksRepository;
        this.timeEntriesRepository = timeEntriesRepository;
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
            if(!timeEntriesRepository.existsByUserIdAndTaskId(userID, taskID)){
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
            if(timeEntriesRepository.existsByUserIdAndTaskId(userID, taskID)){
                //установить время окончания и вычислить продолжительность
                TimeEntry timeEntry = timeEntriesRepository.findByUserIdAndTaskId(userID, taskID);
                timeEntry.setEndTime(new Timestamp(System.currentTimeMillis()));
                timeEntriesRepository.update(timeEntry.getEndTime(), timeEntry.getDuration(), timeEntry.getId());
                return true;
            }
        }
        return false;
    }

}
