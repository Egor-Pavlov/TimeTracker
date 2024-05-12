package org.example.timetracker.Service;

import org.example.timetracker.Repositories.TasksRepository;
import org.example.timetracker.Repositories.TimeEntriesRepository;
import org.example.timetracker.Repositories.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void startTracking(long userID, long taskID) {
        //Проверить, что пользователь существует
        //проверить что задача существует
        //проверить что нет записи в таблице timeentry для этого пользователя и этой задачи, в которой нет endtime
        //Создать запись, указать id пользователя и задачи, дату и время.
    }

}
