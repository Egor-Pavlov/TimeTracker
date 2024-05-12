package org.example.timetracker.Models;

public class User {
      private Long userID;
    private String username;
    private String email;

    public User(long userID, String username, String email) {
        if ( userID < 1)
            throw new IllegalArgumentException("userID must be greater than 0");

        this.userID = userID;
        setUsername(username);
        setEmail(email);
    }

    public User() {}

    public long getUserID() {
        return userID;
    }
    public String getUserName() {
        return username;
    }
    public String getUserEmailAddress() {
        return email;
    }

    public void setUsername(String userName) throws IllegalArgumentException {
        if (userName == null || userName.isEmpty())
            throw new IllegalArgumentException("userName cannot be null or empty");
        this.username = userName;
    }

    public void setEmail(String userEmailAddress) throws IllegalArgumentException {
        if (userEmailAddress == null || userEmailAddress.isEmpty())
            throw new IllegalArgumentException("userEmailAddress cannot be null or empty");
        this.email = userEmailAddress;
    }
}
