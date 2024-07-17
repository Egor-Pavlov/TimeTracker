package org.example.timetracker.Repositories;

import org.example.timetracker.Models.Task;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Task Repository Interface that extends CrudRepository for Task entities.
 * Handles database operations related to Task entities.
 * <p>
 * Repository indicates that this interface serves as a repository for Task entities.
 * EnableAspectJAutoProxy enables AspectJ support for AOP in Spring.
 */
@Repository
@EnableAspectJAutoProxy
public interface TasksRepository extends CrudRepository<Task, Long> {

    /**
     * Retrieves all tasks from the database.
     *
     *
     * @return List of Task objects representing all tasks in the database.
     */
    @Query("SELECT * FROM task;")
    public List<Task> findAll();

    /**
     * Saves a new task into the database.
     *
     * @param theme The theme of the task.
     * @param description The description of the task.
     * @return true if the task is successfully saved, false otherwise.
     */
    @Modifying
    @Query("INSERT INTO task (theme, description) VALUES (:theme, :description);")
    public boolean save(String theme, String description);

    /**
     * Returns Id of task by theme and description
     * @param theme task Theme

     * @return id
     */
    @Query("SELECT task_Id FROM task where theme = :theme")
    public Long getTaskId(String theme);
    /**
     * Checks if a task with the specified ID exists in the database.
     *
     * @param id The ID of the task to check.
     * @return true if a task with the specified ID exists, false otherwise.
     */
    @Query("SELECT COUNT(*) FROM task WHERE task_Id = :id;")
    public boolean existsById(Long id);
}