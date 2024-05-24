package org.example.timetracker.Repositories;

import org.example.timetracker.Models.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ExtendWith(SpringExtension.class)
class UsersRepositoryTest {

    @Autowired
    private UsersRepository usersRepository;

    /**
     * Создание пользователя для тестов и дальнейшего удаления
     * @return user возвращает созданного пользователя
     */
    public static User createTestUser() {
        // Создание нового пользователя
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        return user;
    }


    /**
     * Проверка поиска записи в БД
     */
    @Test
    public void testExistsByEmail() {
        User user = createTestUser();
        // Сохранение пользователя в базе данных
        usersRepository.save(user.getUserName(), user.getUserEmailAddress());
        // Проверка, существует ли пользователь с данным email
        assertTrue(usersRepository.existsByEmail("test@example.com"));

        usersRepository.deleteByEmail(user.getUserEmailAddress());
    }
}