package ru.zoloto.xmlparser.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.zoloto.xmlparser.entity.db.ProjectDTO;
import ru.zoloto.xmlparser.entity.db.TaskDTO;
import ru.zoloto.xmlparser.service.ProjectService;
import ru.zoloto.xmlparser.service.TaskService;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Component
@Slf4j
public class XMLHandler {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TaskService taskService;

    private static List<ProjectDTO> projectDTOS;
    private static ProjectDTO projectDTO;
    private static List<TaskDTO> taskDTOS;
    private static TaskDTO taskDTO;

    public void handle(String xmlPath) {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        AdvancedXMLHandler handler = new AdvancedXMLHandler();

        try {
            SAXParser sp = spf.newSAXParser();
            log.info("Start parse of XML file");
            sp.parse(xmlPath, handler);
        } catch (SAXException | ParserConfigurationException | IOException se) {
            se.printStackTrace();
        }
        projectDTOS = handler.getProjects();

        for (ProjectDTO projectDTO : projectDTOS) {
            for (TaskDTO taskDTO : projectDTO.getTaskDTOS()) {
                taskService.saveTask(taskDTO);
            }
        }

        for (ProjectDTO projectDTO : projectDTOS) {
            projectService.saveProject(projectDTO);
        }
    }

    public List<ProjectDTO> getProjects() {
        return projectDTOS;
    }

    private static class AdvancedXMLHandler extends DefaultHandler {

        static final String PROJECTS_TAG = "projects";
        static final String PROJECT_TAG = "project";
        static final String KEY_TAG = "key";
        static final String SUMMARY_TAG = "summary";
        static final String TASKS_TAG = "tasks";
        static final String TASK_TAG = "task";
        static final String DESCRIPTION_TAG = "description";

        private final Stack<String> tagsStack = new Stack<>();
        private final StringBuilder tempVal = new StringBuilder();

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {

            pushTag(qName);
            tempVal.setLength(0);

            if (PROJECTS_TAG.equalsIgnoreCase(qName)) {
                projectDTOS = new ArrayList<>();
            } else if (PROJECT_TAG.equalsIgnoreCase(qName)) {
                projectDTO = new ProjectDTO();
            } else if (TASKS_TAG.equalsIgnoreCase(qName)) {
                taskDTOS = new ArrayList<>();
            } else if (TASK_TAG.equalsIgnoreCase(qName)) {
                taskDTO = new TaskDTO();
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            tempVal.append(ch, start, length);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            String tag = peekTag();
            if (!qName.equals(tag)) {
                throw new InternalError();
            }

            popTag();
            String parentTag = peekTag();

            if (KEY_TAG.equalsIgnoreCase(tag)) {
                String key = tempVal.toString().trim();
                if (TASK_TAG.equalsIgnoreCase(parentTag)) {
                    taskDTO.setKey(key);
                } else if (PROJECT_TAG.equalsIgnoreCase(parentTag)) {
                    projectDTO.setKey(key);
                }
            } else if (SUMMARY_TAG.equalsIgnoreCase(tag)) {
                String summary = tempVal.toString().trim();
                if (TASK_TAG.equalsIgnoreCase(parentTag)) {
                    taskDTO.setSummary(summary);
                } else if (PROJECT_TAG.equalsIgnoreCase(parentTag)) {
                    projectDTO.setSummary(summary);
                }
            } else if (DESCRIPTION_TAG.equalsIgnoreCase(tag)) {
                String description = tempVal.toString().trim();
                taskDTO.setDescription(description);
            } else if (TASK_TAG.equalsIgnoreCase(tag)) {
                taskDTOS.add(taskDTO);
            } else if (TASKS_TAG.equalsIgnoreCase(tag)) {
                projectDTO.setTaskDTOS(taskDTOS);
            } else if (PROJECT_TAG.equalsIgnoreCase(tag)) {
                projectDTOS.add(projectDTO);
            }
        }

        @Override
        public void startDocument() {
            pushTag("");
        }

        public List<TaskDTO> getTasks() {
            return taskDTOS;
        }

        public List<ProjectDTO> getProjects() {
            return projectDTOS;
        }

        private void pushTag(String tag) {
            tagsStack.push(tag);
        }

        private String popTag() {
            return tagsStack.pop();
        }

        private String peekTag() {
            return tagsStack.peek();
        }
    }
}