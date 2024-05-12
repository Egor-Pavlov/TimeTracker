package org.example.timetracker.Controller;

import org.example.timetracker.DTO.TaskRequest;
import org.example.timetracker.DTO.UserRequest;
import org.example.timetracker.Models.Task;
import org.example.timetracker.Models.User;
import org.example.timetracker.Repositories.TasksRepository;
import org.example.timetracker.Repositories.UsersRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TimeTrackerRestController {
    private final UsersRepository userRepository;
    private final TasksRepository tasksRepository;
    public TimeTrackerRestController(UsersRepository userRepository, TasksRepository tasksRepository) {
        this.userRepository = userRepository;
        this.tasksRepository = tasksRepository;
    }

    @PostMapping("/addUser")
    public void addUser(@RequestBody UserRequest newUser) {
        userRepository.save(newUser.getUsername(), newUser.getEmail());
    }

    @PostMapping("/updateUser")
    public void updateUser(@RequestBody User newUser) {
        userRepository.updateUserByUserID(newUser.getUserID(), newUser.getUserName(), newUser.getUserEmailAddress());
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/addTask")
    public void addTask(@RequestBody TaskRequest taskRequest){
        tasksRepository.save(taskRequest.getTaskTheme(), taskRequest.getTaskDescription());
    }
    @GetMapping("/tasks")
    public List<Task> getTasks() {
        return tasksRepository.findAll();
    }
}
