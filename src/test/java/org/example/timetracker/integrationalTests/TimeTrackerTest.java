package org.example.timetracker.integrationalTests;

import org.example.timetracker.Controller.TimeTrackerRestController;
import org.example.timetracker.Service.TimeTrackerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TimeTrackerRestController.class)
public class TimeTrackerTest {
    @Autowired
    private TimeTrackerService timeTrackerService;
    @Autowired
    private MockMvc mockMvc;

    //подготовка - создание пользователя и задачи
    //тест старта трекинга - кинуть запрос с параметрами (возможно с плохими)
    //откат

    //подготовка - создание пользователя и задачи, выполнение старта трекинга
    //тест остановки трекинга - кинуть запрос с параметрами (возможно с плохими)
    //откат
}
