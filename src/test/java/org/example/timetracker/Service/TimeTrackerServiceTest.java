package org.example.timetracker.Service;

import org.example.timetracker.Models.TimeEntry;
import org.example.timetracker.Repositories.TasksRepository;
import org.example.timetracker.Repositories.TimeEntriesRepository;
import org.example.timetracker.Repositories.UsersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TimeTrackerServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private TasksRepository tasksRepository;

    @Mock
    private TimeEntriesRepository timeEntriesRepository;

    @InjectMocks
    private TimeTrackerService timeTrackerService;

    /**
     * convertDate
     * Тест проверяет корректность конвертации данных при правильном формате
     * @throws Exception
     */
    @Test
    void testConvertDate_ValidDate() throws Exception {
        // Устанавливаем входные данные
        String validDate = "2023-05-21T14:30:00";
        // Ожидаемое значение временной метки (результат)
        Timestamp expectedTimestamp = Timestamp.valueOf("2023-05-21 14:30:00");
        // Вызываем метод конвертации даты
        Timestamp actualTimestamp = timeTrackerService.convertDate(validDate);
        // Проверяем, что ожидаемое и фактическое значение совпадают
        assertEquals(expectedTimestamp, actualTimestamp);
    }

    /**
     * convertDate
     * Проверка, что при передаче недопустимой даты генерируется исключение ParseException
     */
    @Test
    void testConvertDate_InvalidDate() {
        // Устанавливаем недопустимую дату
        String invalidDate = "invalid-date-format";
        // Проверяем, что при передаче недопустимой даты генерируется исключение ParseException
        assertThrows(ParseException.class, () -> {
            timeTrackerService.convertDate(invalidDate);
        });
    }

    /**
     * Создается 2 записи трекинга и проверяется что метод findAll вызвал правильный метод репозитория 1 раз
     */
    @Test
    void testFindAll() {
        // Создаем два объекта TimeEntry
        TimeEntry entry1 = new TimeEntry();
        TimeEntry entry2 = new TimeEntry();
        // Создаем список ожидаемых записей
        List<TimeEntry> expectedEntries = Arrays.asList(entry1, entry2);
        // Устанавливаем поведение репозитория при вызове метода findAll()
        when(timeEntriesRepository.findAll()).thenReturn(expectedEntries);
        // Вызываем метод findAll() и проверяем корректность возвращаемых данных
        List<TimeEntry> actualEntries = timeTrackerService.findAll();
        assertEquals(expectedEntries, actualEntries);
        // Проверяем, что метод findAll() был вызван один раз
        verify(timeEntriesRepository, times(1)).findAll();
    }

    /**
     * Проверка корректности работы метода countByUserId
     */
    @Test
    void testCountByUserId() {
        // Устанавливаем идентификатор пользователя
        long userId = 1L;
        // Устанавливаем ожидаемое количество записей для данного пользователя
        int expectedCount = 5;
        // Устанавливаем поведение репозитория при вызове метода countByUserId()
        when(timeEntriesRepository.countByUserId(userId)).thenReturn(expectedCount);
        // Вызываем метод countByUserId() и проверяем корректность возвращаемого значения
        assertEquals(expectedCount, timeTrackerService.countByUserId(userId));
        // Проверяем, что метод countByUserId() был вызван один раз с указанным userId
        verify(timeEntriesRepository, times(1)).countByUserId(userId);
    }

    /**
     * deleteByUserId
     * Проверка передаваемых параметров и количества вызовов метода репозитория
     */
    @Test
    void testDeleteByUserId() {
        // Установка значений для теста
        long userId = 1L;
        int expectedDeletedCount = 3;
        // Установка поведения репозитория
        when(timeEntriesRepository.deleteByUserId(userId)).thenReturn(expectedDeletedCount);
        // Выполнение метода
        int actualDeletedCount = timeTrackerService.deleteByUserId(userId);
        // Проверка результата
        assertEquals(expectedDeletedCount, actualDeletedCount);
        //Проверка параметров метода и количества вызовов
        verify(timeEntriesRepository, times(1)).deleteByUserId(userId);
    }

    /**
     * startTracking
     * Проверка, что при начале трекинга выполняются все проверки на существование записей в таблицах и что параметры не искажаются
     */
    @Test
    void testStartTracking() {
        //Параметры
        long userId = 1L;
        long taskId = 2L;
        //Поведение заглушек
        when(usersRepository.existsById(userId)).thenReturn(true);
        when(tasksRepository.existsById(taskId)).thenReturn(true);
        when(timeEntriesRepository.existsByUserIdAndTaskId(userId, taskId)).thenReturn(0);
        //Вызов метода
        boolean result = timeTrackerService.startTracking(userId, taskId);
        //Проверка результата
        assertTrue(result);
        //Проверка количества вызовов и параметров методов
        verify(usersRepository, times(1)).existsById(userId);
        verify(tasksRepository, times(1)).existsById(taskId);
        verify(timeEntriesRepository, times(1)).existsByUserIdAndTaskId(userId, taskId);
        verify(timeEntriesRepository, times(1)).save(any(Long.class), any(Long.class), any(Timestamp.class));
    }

    /**
     * stopTracking
     * Проверка, что при окончании трекинга выполняются все проверки на существование записей в таблицах и что параметры не искажаются
     */
    @Test
    void testStopTracking() {
        //Параметры
        long userId = 1L;
        long taskId = 2L;
        //Установка параметров
        TimeEntry timeEntry = new TimeEntry();
        timeEntry.setId(1L);
        timeEntry.setStartTime(new Timestamp(System.currentTimeMillis()));

        //Установка поведения заглушек
        when(usersRepository.existsById(userId)).thenReturn(true);
        when(tasksRepository.existsById(taskId)).thenReturn(true);
        when(timeEntriesRepository.existsByUserIdAndTaskId(userId, taskId)).thenReturn(1);
        when(timeEntriesRepository.findByUserIdAndTaskId(userId, taskId)).thenReturn(timeEntry);

        //вызов метода
        boolean result = timeTrackerService.stopTracking(userId, taskId);

        //проверка результата
        assertTrue(result);

        //Проверка количества вызовов и параметров методов
        verify(usersRepository, times(1)).existsById(userId);
        verify(tasksRepository, times(1)).existsById(taskId);
        verify(timeEntriesRepository, times(1)).existsByUserIdAndTaskId(userId, taskId);
        verify(timeEntriesRepository, times(1)).findByUserIdAndTaskId(userId, taskId);
        verify(timeEntriesRepository, times(1)).update(eq(timeEntry.getEndTime()), eq(timeEntry.getDuration()), eq(timeEntry.getId()));
    }
}
