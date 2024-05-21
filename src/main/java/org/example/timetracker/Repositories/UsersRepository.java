package org.example.timetracker.Repositories;

import org.example.timetracker.Models.User;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * This interface represents a repository for managing user data.
 * It extends the CrudRepository interface to provide basic CRUD operations.
 * The repository is annotated with @Repository and @EnableAspectJAutoProxy.
 */
@Repository
@EnableAspectJAutoProxy
public interface UsersRepository extends CrudRepository<User, Long> {

    /**
     * Retrieves all users from the database.
     *
     * @return a list of User objects representing all users in the database
     */
    public List<User> findAll();

    /**
     * Checks if a user with the specified email exists in the database.
     *
     * @param email the email of the user to check
     * @return true if a user with the email exists, false otherwise
     */
    @Query("SELECT COUNT(*) FROM user WHERE email = :email")
    public boolean existsByEmail(String email);

    /**
     * Finds a count of users with specified email and with other id`s.
     *
     * @param id the ID of the user to exclude from the search
     * @param email the email of the user to find
     * @return the ID of the user with the specified email
     */
    @Query("SELECT count(*) FROM user WHERE email = :email AND user_Id != :id")
    public Long findByEmail(long id, String email);

    /**
     * Saves a new user to the database with the specified name and email.
     *
     * @param name the username of the user
     * @param email the email of the user
     * @return true if the user was successfully saved, false otherwise
     */
    @Modifying
    @Query("INSERT INTO user (username, email) VALUES (:name, :email);")
    public boolean save(String name, String email);

    /**
     * Updates a user's information in the database by their ID.
     *
     * @param id the ID of the user to update
     * @param name the new username of the user
     * @param email the new email of the user
     * @return true if the user was successfully updated, false otherwise
     */
    @Modifying
    @Query("update user set username = :name, email = :email where user_id = :id ")
    public boolean updateUserByUserID(Long id, String name, String email);

    /**
     * Checks if a user with the specified ID exists in the database.
     *
     * @param id the ID of the user to check
     * @return true if a user with the ID exists, false otherwise
     */
    @Query("SELECT COUNT(*) FROM user WHERE user_Id = :id;")
    public boolean existsById(Long id);

    /**
     * Deletes a user from the database by their ID.
     *
     * @param id the ID of the user to delete
     */
    @Modifying
    @Query("DELETE from user WHERE user_Id = :id;")
    public void deleteById(Long id);
}
