package org.example.timetracker.integrationalTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.timetracker.Controller.TimeTrackerRestController;
import org.example.timetracker.DTO.TaskRequest;
import org.example.timetracker.DTO.TimeEntryRequest;
import org.example.timetracker.DTO.UserRequest;
import org.example.timetracker.Models.User;
import org.example.timetracker.Service.TimeTrackerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TimeTrackerRestController.class)
public class TimeTrackerTest {
    @Autowired
    private TimeTrackerService timeTrackerService;
    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testTimeTracker() throws Exception {

        UserRequest newUser = new UserRequest("testUser", "test@test.com");
        //отправка запроса на добавление пользователя с созданным объектом
        mockMvc.perform(post("/api/users/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUser)))
                        .andExpect(status().isOk());


        TaskRequest newtask = new TaskRequest("testTask", "testTask_descr");
        mockMvc.perform(post("/api/tasks/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newtask)))
                        .andExpect(status().isOk());



    }
    //подготовка - создание пользователя и задачи
    //тест старта трекинга - кинуть запрос с параметрами (возможно с плохими)
    //откат

    //подготовка - создание пользователя и задачи, выполнение старта трекинга
    //тест остановки трекинга - кинуть запрос с параметрами (возможно с плохими)
    //откат
}
