package com.taskmanager;

// Task.java - كلاس يمثل المهمة الواحدة
public class Task {
    private int id;
    private String title;
    private String description;
    private boolean isCompleted;

    public Task(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isCompleted = false;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }

    @Override
    public String toString() {
        String status = isCompleted ? "✓" : "✗";
        return String.format("[%d] %s - %s %s",
                id, title, description, status);
    }

    public Task copy() {
        Task copy = new Task(this.id, this.title, this.description);
        copy.setCompleted(this.isCompleted);
        return copy;
    }
}