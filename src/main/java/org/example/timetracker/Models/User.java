package org.example.timetracker.Models;

/**
 * Represents a user with a unique userID, username, and email address.
 */
public class User {
    private Long userID;
    private String username;
    private String email;

    /**
     * Constructs a new User object with the specified userID, username, and email address.
     *
     * @param userID the unique identifier for the user
     * @param username the username of the user
     * @param email the email address of the user
     * @throws IllegalArgumentException if userID is less than 1
     */
    public User(long userID, String username, String email) {
        if ( userID < 1)
            throw new IllegalArgumentException("userID must be greater than 0");

        this.userID = userID;
        setUsername(username);
        setEmail(email);
    }

    /**
     * Default constructor for the User class.
     */
    public User() {}

    /**
     * Returns the userID of the user.
     *
     * @return the userID of the user
     */
    public long getUserID() {
        return userID;
    }

    /**
     * Returns the username of the user.
     *
     * @return the username of the user
     */
    public String getUserName() {
        return username;
    }

    /**
     * Returns the email address of the user.
     *
     * @return the email address of the user
     */
    public String getUserEmailAddress() {
        return email;
    }

    /**
     * Sets the username of the user.
     *
     * @param userName the username to set
     * @throws IllegalArgumentException if userName is null or empty
     */
    public void setUsername(String userName) throws IllegalArgumentException {
        if (userName == null || userName.isEmpty())
            throw new IllegalArgumentException("userName cannot be null or empty");
        this.username = userName;
    }

    /**
     * Sets the email address of the user.
     *
     * @param userEmailAddress the email address to set
     * @throws IllegalArgumentException if userEmailAddress is null or empty
     */
    public void setEmail(String userEmailAddress) throws IllegalArgumentException {
        if (userEmailAddress == null || userEmailAddress.isEmpty())
            throw new IllegalArgumentException("userEmailAddress cannot be null or empty");
        this.email = userEmailAddress;
    }

    /**
     * Returns a string representation of the User object.
     *
     * @return a string representation of the User object
     */
    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
