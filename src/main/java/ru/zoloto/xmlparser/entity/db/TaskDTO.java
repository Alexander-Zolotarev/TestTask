package ru.zoloto.xmlparser.entity.db;

import javax.persistence.*;

@Entity
@Table(name = "Task")
public class TaskDTO {

    @Id
    private String key;
    @Column
    private String summary;
    @Column
    private String description;

    @ManyToOne
    private ProjectDTO projectDTO;

    public TaskDTO() {
    }

    public TaskDTO(String key, String summary, String description, ProjectDTO projectDTO) {
        this.key = key;
        this.summary = summary;
        this.description = description;
        this.projectDTO = projectDTO;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProjectDTO getProjectDTO() {
        return projectDTO;
    }

    public void setProjectDTO(ProjectDTO projectDTO) {
        this.projectDTO = projectDTO;
    }

    @Override
    public String toString() {
        return "TaskDTO{" +
                "key='" + key + '\'' +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", projectDTO=" + projectDTO +
                '}';
    }
}
