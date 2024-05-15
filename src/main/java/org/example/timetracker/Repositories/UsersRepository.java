package org.example.timetracker.Repositories;

import org.example.timetracker.Models.User;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends CrudRepository<User, Long> {
    @Query("SELECT COUNT(*) FROM user WHERE email = :email")
    public boolean existsByEmail(String email);

    @Query("SELECT user_Id FROM user WHERE email = :email")
    public Long findByEmail(String email);

    @Modifying
    @Query("INSERT INTO user (username, email) VALUES (:name, :email);")
    public void save(String name, String email);

    @Modifying
    @Query("update user set username = :name, email = :email where user_id = :id ")
    public void updateUserByUserID(Long id, String name, String email);

    public List<User> findAll();

    @Query("SELECT COUNT(*) FROM user WHERE user_Id = :id;")
    public boolean existsById(Long id);
}
