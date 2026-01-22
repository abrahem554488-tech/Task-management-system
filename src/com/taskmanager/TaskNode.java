package com.taskmanager;

// TaskNode.java - عقدة القائمة المرتبطة
public class TaskNode {
    private Task task;
    private TaskNode next;

    public TaskNode(Task task) {
        this.task = task;
        this.next = null;
    }

    // Getters and Setters
    public Task getTask() { return task; }
    public void setTask(Task task) { this.task = task; }
    public TaskNode getNext() { return next; }
    public void setNext(TaskNode next) { this.next = next; }
}