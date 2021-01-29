package ru.zoloto.xmlparser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zoloto.xmlparser.entity.db.ProjectDTO;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectDTO, String> {

}
