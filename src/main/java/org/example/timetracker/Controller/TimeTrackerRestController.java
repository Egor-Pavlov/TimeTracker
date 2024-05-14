package org.example.timetracker.Controller;

import org.example.timetracker.DTO.TaskRequest;
import org.example.timetracker.DTO.TimeEntryRequest;
import org.example.timetracker.DTO.UserIdReauest;
import org.example.timetracker.DTO.UserRequest;
import org.example.timetracker.Models.Task;
import org.example.timetracker.Models.TimeEntry;
import org.example.timetracker.Models.User;
import org.example.timetracker.Repositories.TasksRepository;
import org.example.timetracker.Repositories.UsersRepository;
import org.example.timetracker.Service.TimeTrackerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/addUser")
    public ResponseEntity<Void> addUser(@RequestBody UserRequest newUser) {
        userRepository.save(newUser.getUsername(), newUser.getEmail());
        return ResponseEntity.ok().build(); // Код 200 OK

    }
    @PostMapping("/updateUser")
    public ResponseEntity<Void> updateUser(@RequestBody User newUser) {
        userRepository.updateUserByUserID(newUser.getUserID(), newUser.getUserName(), newUser.getUserEmailAddress());
        return ResponseEntity.ok().build(); // Код 200 OK
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/addTask")
    public ResponseEntity<Void> addTask(@RequestBody TaskRequest taskRequest){
        tasksRepository.save(taskRequest.getTaskTheme(), taskRequest.getTaskDescription());
        return ResponseEntity.ok().build(); // Код 200 OK

    }
    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return tasksRepository.findAll();
    }
    @GetMapping("/timeEntries")
    public List<TimeEntry> getTimeEntries() {
        return timeTrackerService.findAll();
    }

    @PostMapping("/startTracking")
    public ResponseEntity startTracking(@RequestBody TimeEntryRequest timeEntry) {
        if(timeTrackerService.startTracking(timeEntry.getUser_id(), timeEntry.getTask_id())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("User not found or have not finished tasks");
    }

    @PostMapping("/stopTracking")
    public ResponseEntity stopTracking(@RequestBody TimeEntryRequest timeEntry) {
        if(timeTrackerService.stopTracking(timeEntry.getUser_id(), timeEntry.getTask_id())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("User with id" + timeEntry.getUser_id() + " have not start task " + timeEntry.getTask_id());
    }

    @DeleteMapping("/deleteUserTime")
    public ResponseEntity deleteUserTime(@RequestBody UserIdReauest userId) {
        if (timeTrackerService.countByUserId(userId.getUserId()) == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tasks for user " + userId.getUserId() + " not found");
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body("Deleted " + timeTrackerService.deleteByUserId(userId.getUserId()) + " rows");
    }
}
