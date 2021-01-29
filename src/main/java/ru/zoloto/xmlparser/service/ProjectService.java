package ru.zoloto.xmlparser.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zoloto.xmlparser.entity.db.ProjectDTO;
import ru.zoloto.xmlparser.repository.ProjectRepository;

@Service
@Slf4j
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public void saveProject(ProjectDTO projectDTO) {
        log.info("Save project in DB");
        projectRepository.save(projectDTO);
    }
}
