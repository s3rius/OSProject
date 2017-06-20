package com.example.s3rius.surveyclient.fragments.surveypac;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class Answer implements Serializable, Comparable<Answer> {
    private Integer id;
    private String name;
    private Integer usersAnswered = 0;
    @JsonIgnore
    private boolean isAnswered = false;

    public Answer() {
    }

    public Answer(String name, Integer isAnswered) {
        this.name = name;
    }

    public Integer getUsersAnswered() {
        return usersAnswered;
    }

    public void setUsersAnswered(Integer usersAnswered) {
        this.usersAnswered = usersAnswered;
    }

    @JsonIgnore
    public boolean isIsAnswered() {

        return isAnswered;
    }

    @JsonIgnore
    public void setIsAnswered(boolean answered) {
        this.isAnswered = answered;
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(@NonNull Answer o) {
        return usersAnswered < o.usersAnswered ? 1 : usersAnswered > o.usersAnswered ? -1 : 0;
    }
}
