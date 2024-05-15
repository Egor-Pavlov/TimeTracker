package org.example.timetracker.Controller;

import org.example.timetracker.DTO.*;
import org.example.timetracker.Models.Task;
import org.example.timetracker.Models.TimeEntry;
import org.example.timetracker.Models.User;
import org.example.timetracker.Repositories.TasksRepository;
import org.example.timetracker.Repositories.UsersRepository;
import org.example.timetracker.Service.TimeTrackerService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@RestController
public class TimeTrackerRestController {
    private final UsersRepository userRepository;
    private final TasksRepository tasksRepository;
    private final TimeTrackerService timeTrackerService;
    public TimeTrackerRestController(UsersRepository userRepository, TasksRepository
            tasksRepository, TimeTrackerService timeTrackerService) {
        this.userRepository = userRepository;
        this.tasksRepository = tasksRepository;
        this.timeTrackerService = timeTrackerService;
    }

//Работа только с пользователями
    @PostMapping("/api/users/new")
    public ResponseEntity<String> addUser(@RequestBody UserRequest newUser) {
        if (userRepository.existsByEmail(newUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is used!"); // Код 200 OK
        }
        userRepository.save(newUser.getUsername(), newUser.getEmail());
        return ResponseEntity.ok().build(); // Код 200 OK
    }

    @PostMapping("/api/user/update")
    public ResponseEntity<String> updateUser(@RequestBody User newUser) {
        if (userRepository.findByEmail(newUser.getUserEmailAddress()) != newUser.getUserID()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is used!");
        }
        userRepository.updateUserByUserID(newUser.getUserID(), newUser.getUserName(), newUser.getUserEmailAddress());
        return ResponseEntity.ok().build(); // Код 200 OK
    }

    @GetMapping("/api/users/list")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

//Работа только с задачами
    @PostMapping("/api/tasks/new")
    public ResponseEntity<Void> addTask(@RequestBody TaskRequest taskRequest){
        tasksRepository.save(taskRequest.getTaskTheme(), taskRequest.getTaskDescription());
        return ResponseEntity.ok().build();

    }
    @GetMapping("/api/tasks/list")
    public List<Task> getTasks() {
        return tasksRepository.findAll();
    }

//Работа со временем
    @GetMapping("/api/tracking/list")
    public List<TimeEntry> getTimeEntries() {
        return timeTrackerService.findAll();
    }

    @PostMapping("/api/user/tracking/start")
    public ResponseEntity<String> startTracking(@RequestBody TimeEntryRequest timeEntry) {
        if(timeTrackerService.startTracking(timeEntry.getUser_id(), timeEntry.getTask_id())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User not found or have not finished tasks");
    }

    @PostMapping("/api/user/tracking/stop")
    public ResponseEntity<String> stopTracking(@RequestBody TimeEntryRequest timeEntry) {
        if(timeTrackerService.stopTracking(timeEntry.getUser_id(), timeEntry.getTask_id())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("User with id \"" + timeEntry.getUser_id() + "\" have not start task " + timeEntry.getTask_id());
    }

    @DeleteMapping("/api/user/tracking/delete")
    public ResponseEntity<String> deleteUserTime(@RequestBody UserIdReauest userId) {
        if (timeTrackerService.countByUserId(userId.getUserId()) == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tasks for user " + userId.getUserId() + " not found");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("Deleted " + timeTrackerService.deleteByUserId(userId.getUserId()) + " rows");
    }
    @DeleteMapping("/api/user/delete")
    public ResponseEntity<String> deleteUser(@RequestBody UserIdReauest userId) {
        if (!userRepository.existsById(userId.getUserId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id \"" + userId.getUserId() + "\" not found");
        }
        if (timeTrackerService.countByUserId(userId.getUserId()) != 0) {
            timeTrackerService.deleteByUserId(userId.getUserId());
        }
        userRepository.deleteById(userId.getUserId());
        return ResponseEntity.ok().build();
    }

//Получение трудозатрат пользователя
//@GetMapping("/api/user/tracking/sum/period")
//public double getEffortForPeriod(
//        @RequestParam(value = "userID") Long userID,
//        @RequestParam(value = "startTime") @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") DateTimeFormat startTime,
//        @RequestParam(value = "endTime") @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm:ss") DateTimeFormat endTime) {
//    if (!userRepository.existsById(userID)) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body("User with id \"" + userID + "\" not found").getStatusCodeValue();
//    }
//    //return timeTrackerService.getTotalDurationForPeriod(userID, startTime, endTime);
//}

    @GetMapping("/api/user/tracking/sum/period")
    public double getUserTrackingForPeriod(
            @RequestParam("userID") Long userID,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Timestamp startTimestamp = new Timestamp(dateFormat.parse(startTime).getTime());
        Timestamp endTimestamp = new Timestamp(dateFormat.parse(endTime).getTime());

        if (!userRepository.existsById(userID)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id \"" + userID + "\" not found").getStatusCodeValue();
        }
        return timeTrackerService.getTotalDurationForPeriod(userID, startTimestamp, endTimestamp);
    }
}
