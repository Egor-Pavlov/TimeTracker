package org.example.timetracker.integrationalTests;

import org.example.timetracker.DTO.TaskRequest;
import org.example.timetracker.DTO.TimeEntryRequest;
import org.example.timetracker.DTO.UserRequest;
import org.example.timetracker.Repositories.TasksRepository;
import org.example.timetracker.Repositories.TimeEntriesRepository;
import org.example.timetracker.Repositories.UsersRepository;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.example.timetracker.Models.Task;
import org.example.timetracker.Models.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TimeTrackerIntegrationTest {

    @Autowired
    private DataSource dataSource;

    //@Autowired
    private final TestRestTemplate restTemplate = new TestRestTemplate();

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private TimeEntriesRepository timeEntriesRepository;

    private final String prefix= "http://127.0.0.1:8080";

    @BeforeEach
    public void setUp() throws SQLException {
        timeEntriesRepository.deleteAll();
        usersRepository.deleteAll();
        tasksRepository.deleteAll();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        timeEntriesRepository.deleteAll();
        usersRepository.deleteAll();
        tasksRepository.deleteAll();
    }
    @Test
    void contextLoads() throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            assertThat(connection.isValid(1)).isTrue();
        }
    }

    /**
     * Тест услуги трекинга времени. Проверяется работоспособность функционала трекинга: начало трекинга, окончание и подсчет трудозатрат.
     * Создается пользователь и задача, начинается отсчет времени (с помощью API запроса).
     * Ожидание 30 секунд
     * Отсчет времени по задаче завершается (API запрос)
     * Запрашивается сумма трудозатрат пользователя за период настоящее время +- 5 минут
     * трудозатраты должны быть равны 0,01, так как работа велась 30 секунд
     * @throws InterruptedException
     */
    @Test
    public void testTimeTrackingFlow() throws InterruptedException {
//        // Создание пользователя
        usersRepository.save("tester", "a@a.ru");
        Long userId = usersRepository.getIdByEmail("a@a.ru");
                // Создание задачи
        tasksRepository.save("New Task", "New task for test");
        long taskId = tasksRepository.getTaskId("New Task");

        // Начало трекинга
        TimeEntryRequest startTracking = new TimeEntryRequest(userId, taskId);
        restTemplate.postForEntity(prefix + "/api/user/tracking/start", startTracking, null);
        Thread.sleep(30000);
        restTemplate.postForEntity(prefix + "/api/user/tracking/stop", startTracking,null);

        // Получение текущего времени
        LocalDateTime now = LocalDateTime.now();

        //Вычисление времени 5 минут до и 5 минут после текущего времени для получения трудозатрат
        LocalDateTime startTime = now.minusMinutes(5);
        LocalDateTime endTime = now.plusMinutes(5);

        // Форматирование времени в строку в формате ISO_LOCAL_DATE_TIME
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String startTimeStr = startTime.format(formatter);
        String endTimeStr = endTime.format(formatter);
        // Выполнение запроса с автоматически сгенерированными датами
        String url = String.format(prefix + "/api/user/tracking/sum/period?userID=%d&startTime=%s&endTime=%s", userId, startTimeStr, endTimeStr);ResponseEntity<String> durationResponse = restTemplate.getForEntity(url, String.class);
        String responseBody = durationResponse.getBody();
        System.out.println("Response body: " + responseBody);

        // Попытка десериализации в Double
        Double duration = null;
        if (responseBody != null) {
            try {
                duration = Double.parseDouble(responseBody);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        assertNotNull(duration);
        assertEquals(0.01, duration, 0.001);
    }
}
