package ru.zoloto.xmlparser.entity.db;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Project")
public class ProjectDTO {

    @Id
    private String key;
    @Column
    private String summary;
    @OneToMany
    private List<TaskDTO> taskDTOS;

    public ProjectDTO() {
    }

    public ProjectDTO(String key, String summary, List<TaskDTO> taskDTOS) {
        this.key = key;
        this.summary = summary;
        this.taskDTOS = taskDTOS;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<TaskDTO> getTaskDTOS() {
        return taskDTOS;
    }

    public void setTaskDTOS(List<TaskDTO> taskDTOS) {
        this.taskDTOS = taskDTOS;
    }

    @Override
    public String toString() {
        return "ProjectDTO{" +
                "key='" + key + '\'' +
                ", summary='" + summary + '\'' +
                ", taskDTOS=" + taskDTOS +
                '}';
    }
}
