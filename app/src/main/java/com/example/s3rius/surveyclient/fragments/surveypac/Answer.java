package com.example.s3rius.surveyclient.fragments.surveypac;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

public class Answer implements Serializable, Comparable<Answer> {
    private Integer id;
    private String name;

    @JsonIgnore
    private boolean answered = false;

    public Answer(){
    }

    public Answer(String name, Integer answered) {
        this.name = name;
    }

    @JsonIgnore
    public boolean isAnswered() {

        return answered;
    }

    @JsonIgnore
    public void setAnswered(boolean answered) {
        this.answered = answered;
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
        return id > o.getId() ? 1 : id < o.getId() ? -1 : 0;
    }
}
