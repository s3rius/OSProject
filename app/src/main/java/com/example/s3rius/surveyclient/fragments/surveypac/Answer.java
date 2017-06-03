package com.example.s3rius.surveyclient.fragments.surveypac;

import android.support.annotation.NonNull;

import java.io.File;
import java.io.Serializable;

public class Answer implements Serializable, Comparable<Answer> {
    private Integer id;
    private String name;

    public Answer() {
    }

    public Answer(String name, Integer answered) {
        this.name = name;
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
    return id > o.getId()? 1: id < o.getId()? -1: 0;
    }
}
