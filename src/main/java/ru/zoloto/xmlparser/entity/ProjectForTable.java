package ru.zoloto.xmlparser.entity;

public class ProjectForTable {

    private String projectKey;
    private String taskKey;
    private String taskDescription;

    public String getProjectKey() {
        return projectKey;
    }

    public void setProjectKey(String projectKey) {
        this.projectKey = projectKey;
    }

    public String getTaskKey() {
        return taskKey;
    }

    public void setTaskKey(String taskKey) {
        this.taskKey = taskKey;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    @Override
    public String toString() {
        return "ProjectForTable{" +
                "projectKey='" + projectKey + '\'' +
                ", taskKey='" + taskKey + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                '}';
    }
}
