package org.example.timetracker.Controller;

import org.example.timetracker.DTO.*;
import org.example.timetracker.Models.Task;
import org.example.timetracker.Models.TimeEntry;
import org.example.timetracker.Models.User;
import org.example.timetracker.Repositories.TasksRepository;
import org.example.timetracker.Repositories.UsersRepository;
import org.example.timetracker.Service.TimeTrackerService;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

/**
 * REST controller for managing users, tasks, and time tracking.
 */
@RestController
@EnableAspectJAutoProxy
public class TimeTrackerRestController {
    private final UsersRepository userRepository;
    private final TasksRepository tasksRepository;
    private final TimeTrackerService timeTrackerService;
    /**
     * Constructs a new TimeTrackerRestController with the specified repositories and service.
     *
     * @param userRepository the user repository
     * @param tasksRepository the tasks repository
     * @param timeTrackerService the time tracker service
     */
    public TimeTrackerRestController(UsersRepository userRepository, TasksRepository
            tasksRepository, TimeTrackerService timeTrackerService) {
        this.userRepository = userRepository;
        this.tasksRepository = tasksRepository;
        this.timeTrackerService = timeTrackerService;
    }

//Работа только с пользователями

    /**
     * Add new user.
     *
     * @param newUser the request containing user details (username, email)
     * @return the response entity with status and message
     */
    @PostMapping("/api/users/new")
    public ResponseEntity<String> addUser(@RequestBody UserRequest newUser) {
        if (userRepository.existsByEmail(newUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is used!"); // Код 200 OK
        }
        userRepository.save(newUser.getUsername(), newUser.getEmail());
        return ResponseEntity.ok().build(); // Код 200 OK
    }
    /**
     * Updates an existing user's information.
     *
     * @param newUser the updated user details
     * @return the response entity with status and message
     */
    @PostMapping("/api/user/update")
    public ResponseEntity<String> updateUser(@RequestBody User newUser) {
        if (userRepository.findByEmail(newUser.getUserID(), newUser.getUserEmailAddress()) != newUser.getUserID()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is used!");
        }
        
        userRepository.updateUserByUserID(newUser.getUserID(), newUser.getUserName(), newUser.getUserEmailAddress());
        return ResponseEntity.ok().build(); // Код 200 OK
    }

    /**
     * Retrieves a list of all users.
     *
     * @return the list of users
     */
    @GetMapping("/api/users/list")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Adds a new task.
     *
     * @param taskRequest the request containing task details
     * @return the response entity with status
     */
    @PostMapping("/api/tasks/new")
    public ResponseEntity<Void> addTask(@RequestBody TaskRequest taskRequest){
        tasksRepository.save(taskRequest.getTaskTheme(), taskRequest.getTaskDescription());
        return ResponseEntity.ok().build();

    }
    /**
     * Retrieves a list of all tasks.
     *
     * @return the list of tasks
     */
    @GetMapping("/api/tasks/list")
    public List<Task> getTasks() {
        return tasksRepository.findAll();
    }

    /**
     * Retrieves all time entries (users tracking).
     *
     * @return the list of time entries
     */
    @GetMapping("/api/tracking/list")
    public List<TimeEntry> getTimeEntries() {
        return timeTrackerService.findAll();
    }

    /**
     * Starts tracking time for a specific task.
     *
     * @param timeEntry the request containing user id and task id
     * @return the response entity with status and message
     */
    @PostMapping("/api/user/tracking/start")
    public ResponseEntity<String> startTracking(@RequestBody TimeEntryRequest timeEntry) {
        if(timeTrackerService.startTracking(timeEntry.getUser_id(), timeEntry.getTask_id())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User not found or have not finished tasks");
    }

    /**
     * Stops tracking time for a specific task.
     *
     * @param timeEntry the request containing user id and task id
     * @return the response entity with status and message
     */
    @PostMapping("/api/user/tracking/stop")
    public ResponseEntity<String> stopTracking(@RequestBody TimeEntryRequest timeEntry) {
        if(timeTrackerService.stopTracking(timeEntry.getUser_id(), timeEntry.getTask_id())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("User with id \"" + timeEntry.getUser_id() + "\" have not start task " + timeEntry.getTask_id());
    }
    /**
     * Deletes tracking information for a specific user.
     *
     * @param userId the request containing user ID
     * @return the response entity with status and message
     */
    @DeleteMapping("/api/user/tracking/delete")
    public ResponseEntity<String> deleteUserTime(@RequestBody UserIdRequest userId) {
        if (timeTrackerService.countByUserId(userId.getUserId()) == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tasks for user " + userId.getUserId() + " not found");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("Deleted " + timeTrackerService.deleteByUserId(userId.getUserId()) + " rows");
    }
    /**
     * Deletes all information for a specific user.
     *
     * @param userId the request containing user ID
     * @return the response entity with status and message
     */
    @DeleteMapping("/api/user/delete")
    public ResponseEntity<String> deleteUser(@RequestBody UserIdRequest userId) {
        if (!userRepository.existsById(userId.getUserId())) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id \"" + userId.getUserId() + "\" not found");
        }
        int count = 0;
        if (timeTrackerService.countByUserId(userId.getUserId()) != 0) {
            count = timeTrackerService.deleteByUserId(userId.getUserId()) + 1;
        }
        userRepository.deleteById(userId.getUserId());
        return ResponseEntity.status(HttpStatus.OK)
                .body("Deleted " + count + " rows");
    }
    /**
     * Retrieves the total work duration for a specific user over a specified period.
     *
     * @param userID the user ID
     * @param startTime the start time of the period
     * @param endTime the end time of the period
     * @return the response entity with total duration or error message
     */
    @GetMapping("/api/user/tracking/sum/period")
    public ResponseEntity<?> getSumUserDurationForPeriod(
            @RequestParam("userID") Long userID,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime) {

        if (!userRepository.existsById(userID)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id \"" + userID + "\" not found");
        }
        try{
            double totalDuration = timeTrackerService.getTotalDurationForPeriod(userID, startTime, endTime);
            return ResponseEntity.ok().body(totalDuration);
        }
        catch (ParseException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Incorrect date!");
        }
    }
    /**
     * Retrieves the work durations for a specific user over a specified period.
     *
     * @param userID the user ID
     * @param startTime the start time of the period
     * @param endTime the end time of the period
     * @return the response entity with list of task durations or error message
     */
    @GetMapping("/api/user/tracking/durations/period")
    public ResponseEntity<?> getUserDurationsForPeriod(
            @RequestParam("userID") Long userID,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime) {

        if (!userRepository.existsById(userID)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id \"" + userID + "\" not found");
        }
        try {
            List<TaskDuration> durations = timeTrackerService.getUserDurationsForPeriod(userID, startTime, endTime);
            return ResponseEntity.ok(durations);
        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Incorrect date!");
        }
    }

    /**
     * Retrieves the work intervals for a specific user over a specified period.
     *
     * @param userID the user ID
     * @param startTime the start time of the period
     * @param endTime the end time of the period
     * @return the response entity with list of work intervals or error message
     */
    @GetMapping("/api/user/tracking/intervals/period")
    public ResponseEntity<?> getUserWorkIntervalsForPeriod(
            @RequestParam("userID") Long userID,
            @RequestParam("startTime") String startTime,
            @RequestParam("endTime") String endTime) {

        if (!userRepository.existsById(userID)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with id \"" + userID + "\" not found");
        }
        try {
            List<WorkInterval> durations = timeTrackerService.getUserWorkIntervalsForPeriod(userID, startTime, endTime);
            return ResponseEntity.ok(durations);
        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Incorrect date!");
        }
    }
}
