package com.oluwafemi.mjournal.model;

import android.arch.persistence.room.Entity;

/**
 * Created by femi on 6/28/18.
 */

@Entity
public class User {

    private String userId;
    private String userName;
    private String userEmail;

    // required empty constructor for firebase
    public User() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
