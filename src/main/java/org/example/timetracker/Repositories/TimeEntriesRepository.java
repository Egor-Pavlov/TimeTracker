package org.example.timetracker.Repositories;

import org.example.timetracker.Models.TimeEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeEntriesRepository extends CrudRepository <TimeEntry, Long> {

}
