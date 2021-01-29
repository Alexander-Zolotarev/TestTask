package ru.zoloto.xmlparser.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zoloto.xmlparser.entity.db.TaskDTO;
import ru.zoloto.xmlparser.repository.TaskRepository;

@Service
@Slf4j
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public void saveTask(TaskDTO taskDTO) {
        log.info("Save task in DB");
        taskRepository.save(taskDTO);
    }
}
