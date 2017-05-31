package com.example.s3rius.surveyclient.fragments.surveypac;

import java.io.File;
import java.io.Serializable;

public class Answer implements Serializable {
    private Integer id;
    private String name;
    private Integer answered;
    private File file;

    public Answer() {
    }

    public Answer(String name, Integer answered) {
        this.name = name;
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

    public Integer getAnswered() {
        return this.answered;
    }

    public void setAnswered(Integer answered) {
        this.answered = answered;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
