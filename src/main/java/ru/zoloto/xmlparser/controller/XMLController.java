package ru.zoloto.xmlparser.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.zoloto.xmlparser.entity.ProjectForTable;
import ru.zoloto.xmlparser.entity.db.ProjectDTO;
import ru.zoloto.xmlparser.entity.db.TaskDTO;
import ru.zoloto.xmlparser.handler.XMLHandler;
import ru.zoloto.xmlparser.handler.XSDValidationHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class XMLController {

    @Autowired
    private XSDValidationHandler xsdValidationHandler;

    @Autowired
    private XMLHandler xmlHandler;

    private static final String UPLOADED_FOLDER = "E:/Projects/xml-parser/src/main/resources/";
    private static final String PATH_TO_XML = "src/main/resources/file.xml";
    private static final String PATH_TO_XSD = "src/main/resources/file2.xsd";

    @GetMapping
    public String upload() {
        return "upload";
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file, Model model) {

        if (file.isEmpty()) {
            log.info("File is empty");
            model.addAttribute("message", "Please select a file to upload");
            return "upload";
        }

        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            if (!xsdValidationHandler.isValid(PATH_TO_XML, PATH_TO_XSD)) {
                log.info("File is not validate by XSD schema");
                model.addAttribute("message", "File is not validate by XSD schema");
                return "upload";
            }

            xmlHandler.handle(path.toString());

            model.addAttribute("message",
                    "You successfully uploaded '" + file.getOriginalFilename() + "'");
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return "upload";
    }

    @GetMapping("/data")
    public String data(Model model) {

        ProjectForTable projectForTable;
        List<ProjectForTable> projectForTableList = new ArrayList<>();

        for (ProjectDTO projectDTO : xmlHandler.getProjects()) {
            for (TaskDTO taskDTO : projectDTO.getTaskDTOS()) {
                projectForTable = new ProjectForTable();
                projectForTable.setProjectKey(projectDTO.getKey());
                projectForTable.setTaskKey(taskDTO.getKey());
                projectForTable.setTaskDescription(taskDTO.getDescription());
                projectForTableList.add(projectForTable);
            }
        }
        model.addAttribute("projects", projectForTableList);
        return "data";
    }

    @GetMapping(value = "/task/{keyTask}")
    @ResponseBody
    public TaskDTO getTaskByKey(@PathVariable("keyTask") String keyTask) {

        TaskDTO target = new TaskDTO();

        for (ProjectDTO projectDTO : xmlHandler.getProjects()) {
            for (TaskDTO taskDTO : projectDTO.getTaskDTOS()) {
                if (taskDTO.getKey().equalsIgnoreCase(keyTask)) {
                    target = taskDTO;
                }
            }
        }
        return target;
    }

    @GetMapping(value = "/project/{keyProject}")
    @ResponseBody
    public List<TaskDTO> getAllTasksByProjectKey(@PathVariable("keyProject") String keyProject) {

        List<TaskDTO> taskDTOS = new ArrayList<>();

        for (ProjectDTO projectDTO : xmlHandler.getProjects()) {
            if (projectDTO.getKey().equalsIgnoreCase(keyProject)) {
                taskDTOS.addAll(projectDTO.getTaskDTOS());
            }
        }
        return taskDTOS;
    }
}
