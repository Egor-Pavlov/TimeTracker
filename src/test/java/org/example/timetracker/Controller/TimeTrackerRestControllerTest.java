package org.example.timetracker.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.timetracker.DTO.UserRequest;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TimeTrackerRestController.class)
class TimeTrackerRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsersRepository userRepository;


    /**
     * Проверка работы обработчика запроса на создание пользователя /api/users/new/
     * Пользователь создается и добавляется в БД, после проверяется что метод сохранения в БД был вызван с корректными параметрами
     * @throws Exception
     */
    @Test
    public void testAddUser() throws Exception {
        //объект, который отправится в запросе
        UserRequest newUser = new UserRequest("testUser", "test@test.com");
        //Установка поведения заглушки
        Mockito.when(userRepository.existsByEmail(newUser.getEmail())).thenReturn(false);
        //отправка запроса на добавление пользователя с созданным объектом
        mockMvc.perform(post("/api/users/new")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newUser)))
                        .andExpect(status().isOk());
        //Проверка, что метод сохранения в базу вызван с нужными параметрами
        Mockito.verify(userRepository, Mockito.times(1)).save(newUser.getUsername(), newUser.getEmail());
    }
}
