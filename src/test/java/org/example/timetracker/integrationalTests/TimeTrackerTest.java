package org.example.timetracker.integrationalTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.timetracker.Controller.TimeTrackerRestController;
import org.example.timetracker.DTO.TaskRequest;
import org.example.timetracker.DTO.TimeEntryRequest;
import org.example.timetracker.DTO.UserRequest;
import org.example.timetracker.Models.User;
import org.example.timetracker.Repositories.TasksRepository;
import org.example.timetracker.Repositories.UsersRepository;
import org.example.timetracker.Service.TimeTrackerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TimeTrackerRestController.class)
public class TimeTrackerTest {
    @Autowired
    private MockMvc mockMvc;
//    @MockBean
//    private TimeTrackerService timeTrackerService;
    @MockBean
    private TimeTrackerRestController timeTrackerRestController;
//    @MockBean
//    private UsersRepository UsersRepository;
//    @MockBean
//    private TasksRepository TasksRepository;

    public void executeAddUser(String url, UserRequest newUser) throws Exception {
        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUser)))
                .andExpect(status().isOk());
    }
    public void executeAddTask(String url, TaskRequest newTask) throws Exception {

        mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"theme\":\"" + newTask.getTaskTheme() + "\", \"description\":\"" + newTask.getTaskDescription() + "\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testTimeTracker() throws Exception {

        UserRequest newUser = new UserRequest("testUser", "test@test.com");
        //отправка запроса на добавление пользователя с созданным объектом
        executeAddUser("/api/users/new", newUser);

        TaskRequest newTask = new TaskRequest("testTask", "testTask_descr");
        executeAddTask("/api/tasks/new", newTask);

    }
    //подготовка - создание пользователя и задачи
    //тест старта трекинга - кинуть запрос с параметрами (возможно с плохими)
    //откат

    //подготовка - создание пользователя и задачи, выполнение старта трекинга
    //тест остановки трекинга - кинуть запрос с параметрами (возможно с плохими)
    //откат
}
