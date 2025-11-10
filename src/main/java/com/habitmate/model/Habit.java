package com.habitmate.model;

public class Habit {
    private Long id;
    private String name;
    private String description;
    private boolean completed;

    public Habit(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.completed = false;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
