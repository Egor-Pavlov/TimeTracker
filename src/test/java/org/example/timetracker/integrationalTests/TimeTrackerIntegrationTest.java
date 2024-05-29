package org.example.timetracker.integrationalTests;

import jakarta.annotation.PostConstruct;
import org.example.timetracker.DTO.TaskRequest;
import org.example.timetracker.Models.Task;
import org.example.timetracker.Models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@Testcontainers
public class TimeTrackerIntegrationTest {

    @Container
    public static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:latest")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test")
            .withExposedPorts(3306)
            .withCommand("--default-authentication-plugin=mysql_native_password",
                    "--innodb-redo-log-capacity=10485760");

    @Autowired
    private TestRestTemplate restTemplate;

    private String jdbcUrl;

    @PostConstruct
    public void init() {
        String address = mysqlContainer.getHost();
        Integer port = mysqlContainer.getMappedPort(3306);
        jdbcUrl = "jdbc:mysql://100.110.1.79:" + port + "/test";
        System.out.println("JDBC URL: " + jdbcUrl);
    }

    @Test
    public void contextLoads() {
        assertThat(mysqlContainer.isRunning()).isTrue();
    }

    @Test
    public void testTimeTrackingFlow() {
        // Создание пользователя
        User user = new User();
        user.setUsername("tester");
        user.setEmail("a@a.ru");
        ResponseEntity<User> userResponse = restTemplate.postForEntity("/api/users/new", user, User.class);
        assertNotNull(userResponse.getBody());
        Long userId = userResponse.getBody().getUserID();

        // Создание задачи
        TaskRequest task = new TaskRequest("New Task","New task for test");
        ResponseEntity<Task> taskResponse = restTemplate.postForEntity("/api/tasks/new", task, Task.class);
        assertNotNull(taskResponse.getBody());
        long taskId = taskResponse.getBody().getTaskID();

//        // Начало трекинга
//        ResponseEntity<Tracking> startTrackingResponse = restTemplate.postForEntity("/tasks/" + taskId + "/start", null, Tracking.class);
//        assertNotNull(startTrackingResponse.getBody());
//        Long trackingId = startTrackingResponse.getBody().getId();
//
//        // Пауза трекинга
//        HttpHeaders headers = new HttpHeaders();
//        HttpEntity<Void> pauseRequest = new HttpEntity<>(headers);
//        ResponseEntity<Void> pauseResponse = restTemplate.exchange("/trackings/" + trackingId + "/pause", HttpMethod.PUT, pauseRequest, Void.class);
//        assertEquals(200, pauseResponse.getStatusCodeValue());
//
//        // Остановка трекинга
//        HttpEntity<Void> stopRequest = new HttpEntity<>(headers);
//        ResponseEntity<Void> stopResponse = restTemplate.exchange("/trackings/" + trackingId + "/stop", HttpMethod.PUT, stopRequest, Void.class);
//        assertEquals(200, stopResponse.getStatusCodeValue());
    }
}
