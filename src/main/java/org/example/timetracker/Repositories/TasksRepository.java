package org.example.timetracker.Repositories;

import org.example.timetracker.Models.Task;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TasksRepository extends CrudRepository <Task, Long>{
    public List<Task> findAll();

    @Modifying
    @Query("INSERT INTO task (theme, description) VALUES (:theme, :description);")
    public void save(String theme, String description);
}
