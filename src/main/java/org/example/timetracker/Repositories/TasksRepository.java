package org.example.timetracker.Repositories;

import org.example.timetracker.Models.Task;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@EnableAspectJAutoProxy
public interface TasksRepository extends CrudRepository <Task, Long>{
    @Query("SELECT * FROM task;")
    public List<Task> findAll();

    @Modifying
    @Query("INSERT INTO task (theme, description) VALUES (:theme, :description);")
    public boolean save(String theme, String description);

    @Query("SELECT COUNT(*) FROM task WHERE task_Id = :id;")
    public boolean existsById(Long id);
}
