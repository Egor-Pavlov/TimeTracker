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

    @BeforeEach
    void setUp() {
        // Mockito will initialize mocks and inject them into the service
    }

    @Test
    void testConvertDate_ValidDate() throws Exception {
        String validDate = "2023-05-21T14:30:00";
        Timestamp expectedTimestamp = Timestamp.valueOf("2023-05-21 14:30:00");

        Timestamp actualTimestamp = timeTrackerService.convertDate(validDate);

        assertEquals(expectedTimestamp, actualTimestamp);
    }

    @Test
    void testConvertDate_InvalidDate() {
        String invalidDate = "invalid-date-format";

        assertThrows(ParseException.class, () -> {
            timeTrackerService.convertDate(invalidDate);
        });
    }

    @Test
    void testFindAll() {
        TimeEntry entry1 = new TimeEntry();
        TimeEntry entry2 = new TimeEntry();
        List<TimeEntry> expectedEntries = Arrays.asList(entry1, entry2);

        when(timeEntriesRepository.findAll()).thenReturn(expectedEntries);

        List<TimeEntry> actualEntries = timeTrackerService.findAll();

        assertEquals(expectedEntries, actualEntries);
        verify(timeEntriesRepository, times(1)).findAll();
    }

    @Test
    void testCountByUserId() {
        long userId = 1L;
        int expectedCount = 5;

        when(timeEntriesRepository.countByUserId(userId)).thenReturn(expectedCount);

        int actualCount = timeTrackerService.countByUserId(userId);

        assertEquals(expectedCount, actualCount);
        verify(timeEntriesRepository, times(1)).countByUserId(userId);
    }

    @Test
    void testDeleteByUserId() {
        long userId = 1L;
        int expectedDeletedCount = 3;

        when(timeEntriesRepository.deleteByUserId(userId)).thenReturn(expectedDeletedCount);

        int actualDeletedCount = timeTrackerService.deleteByUserId(userId);

        assertEquals(expectedDeletedCount, actualDeletedCount);
        verify(timeEntriesRepository, times(1)).deleteByUserId(userId);
    }

    @Test
    void testStartTracking() {
        long userId = 1L;
        long taskId = 2L;

        when(usersRepository.existsById(userId)).thenReturn(true);
        when(tasksRepository.existsById(taskId)).thenReturn(true);
        when(timeEntriesRepository.existsByUserIdAndTaskId(userId, taskId)).thenReturn(0);

        boolean result = timeTrackerService.startTracking(userId, taskId);

        assertTrue(result);
        verify(usersRepository, times(1)).existsById(userId);
        verify(tasksRepository, times(1)).existsById(taskId);
        verify(timeEntriesRepository, times(1)).existsByUserIdAndTaskId(userId, taskId);
        verify(timeEntriesRepository, times(1)).save(any(Long.class), any(Long.class), any(Timestamp.class));
    }

    @Test
    void testStopTracking() {
        long userId = 1L;
        long taskId = 2L;
        TimeEntry timeEntry = new TimeEntry();
        TimeEntry endTime = new TimeEntry();
//        timeEntry.setId(1L);
//        timeEntry.setStartTime(new Timestamp(System.currentTimeMillis()));

        when(usersRepository.existsById(userId)).thenReturn(true);
        when(tasksRepository.existsById(taskId)).thenReturn(true);
        when(timeEntriesRepository.existsByUserIdAndTaskId(userId, taskId)).thenReturn(1);
        when(timeEntriesRepository.findByUserIdAndTaskId(userId, taskId)).thenReturn(timeEntry);
        when(timeEntry.setEndTime(endTime)).thenReturn();

        boolean result = timeTrackerService.stopTracking(userId, taskId);

        assertTrue(result);
        verify(usersRepository, times(1)).existsById(userId);
        verify(tasksRepository, times(1)).existsById(taskId);
        verify(timeEntriesRepository, times(1)).existsByUserIdAndTaskId(userId, taskId);
        verify(timeEntriesRepository, times(1)).findByUserIdAndTaskId(userId, taskId);
        verify(timeEntriesRepository, times(1)).update(any(Timestamp.class), any(Long.class), eq(timeEntry.getId()));
    }
}
