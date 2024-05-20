package org.example.timetracker.Repositories;

import org.example.timetracker.Models.User;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@EnableAspectJAutoProxy
public interface UsersRepository extends CrudRepository<User, Long> {

    public List<User> findAll();

    @Query("SELECT COUNT(*) FROM user WHERE email = :email")
    public boolean existsByEmail(String email);

    @Query("SELECT count(*) FROM user WHERE email = :email AND user_Id != :id")
    public Long findByEmail( long id, String email);

    @Modifying
    @Query("INSERT INTO user (username, email) VALUES (:name, :email);")
    public boolean save(String name, String email);

    @Modifying
    @Query("update user set username = :name, email = :email where user_id = :id ")
    public boolean updateUserByUserID(Long id, String name, String email);

    @Query("SELECT COUNT(*) FROM user WHERE user_Id = :id;")
    public boolean existsById(Long id);

    @Modifying
    @Query("DELETE from user WHERE user_Id = :id;")
    public void deleteById(Long id);
}
