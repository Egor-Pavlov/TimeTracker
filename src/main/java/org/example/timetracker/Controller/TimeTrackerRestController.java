package org.example.timetracker.Controller;

import org.example.timetracker.DTO.TaskRequest;
import org.example.timetracker.DTO.TimeEntryRequest;
import org.example.timetracker.DTO.UserRequest;
import org.example.timetracker.Models.Task;
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
    public TimeTrackerRestController(UsersRepository userRepository, TasksRepository tasksRepository, TimeTrackerService timeTrackerService) {
        this.userRepository = userRepository;
        this.tasksRepository = tasksRepository;
        this.timeTrackerService = timeTrackerService;
    }

    @PostMapping("/addUser")
    public ResponseEntity<Void> addUser(@RequestBody UserRequest newUser) {
        try {
            userRepository.save(newUser.getUsername(), newUser.getEmail());
            return ResponseEntity.ok().build(); // Код 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Код 500 Internal Server Error
        }
    }
    @PostMapping("/updateUser")
    public ResponseEntity<Void> updateUser(@RequestBody User newUser) {
        try {
            userRepository.updateUserByUserID(newUser.getUserID(), newUser.getUserName(), newUser.getUserEmailAddress());
            return ResponseEntity.ok().build(); // Код 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Код 500 Internal Server Error
        }
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/addTask")
    public ResponseEntity<Void> addTask(@RequestBody TaskRequest taskRequest){
        try {
            tasksRepository.save(taskRequest.getTaskTheme(), taskRequest.getTaskDescription());
            return ResponseEntity.ok().build(); // Код 200 OK
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Код 500 Internal Server Error
        }
    }
    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return tasksRepository.findAll();
    }

    @PostMapping("/startTracking")
    public ResponseEntity<Void> startTracking(@RequestBody TimeEntryRequest timeEntry) {
        if(timeTrackerService.startTracking(timeEntry.getUser_id(), timeEntry.getTask_id())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @PostMapping("/stopTracking")
    public ResponseEntity<Void> stopTracking(@RequestBody TimeEntryRequest timeEntry) {
        if(timeTrackerService.stopTracking(timeEntry.getUser_id(), timeEntry.getTask_id())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
