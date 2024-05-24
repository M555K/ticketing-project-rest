package com.company.service.impl;

import com.company.dto.ProjectDTO;
import com.company.dto.UserDTO;
import com.company.entity.Project;
import com.company.entity.User;
import com.company.enums.Status;
import com.company.mapper.ProjectMapper;
import com.company.mapper.UserMapper;
import com.company.repository.ProjectRepository;
import com.company.service.ProjectService;
import com.company.service.TaskService;
import com.company.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private static final Logger log = LoggerFactory.getLogger(ProjectServiceImpl.class);
    private final ProjectRepository projectRepository;
private final ProjectMapper projectMapper;
private final UserService userService;
private  final UserMapper userMapper;
private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper, UserService userService, UserMapper userMapper, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.taskService = taskService;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        return projectMapper.convertToDTO(projectRepository.findByProjectCode(code));
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        List<Project> projects = projectRepository.findAll(Sort.by("projectCode"));
        return projects.stream().map(projectMapper::convertToDTO).toList();
    }

    @Override
    public void save(ProjectDTO projectDTO) {
        // before saving the project I need to define Status because, even the when I create a poject doesn't have the Status but, the table are expecting a value when the project is created
        projectDTO.setProjectStatus(Status.OPEN);
        Project project = projectMapper.convertToEntity(projectDTO);
        //projectMapper mapped id to null
        projectRepository.save(project);
    }

    @Override
    public void update(ProjectDTO projectDTO) {
        Project project = projectRepository.findByProjectCode(projectDTO.getProjectCode());
        Project convertedProject = projectMapper.convertToEntity(projectDTO);
        convertedProject.setId(project.getId());
        convertedProject.setProjectStatus(project.getProjectStatus());
        projectRepository.save(convertedProject);
    }

    @Override
    public void delete(String projectDTO) {
        Project project = projectRepository.findByProjectCode(projectDTO);
        project.setIsDeleted(true);
        //condition for using the same project code when the other one with the same project code was deleted
        project.setProjectCode(project.getProjectCode()+"-"+project.getId());
        projectRepository.save(project);
        // for task related
        taskService.deleteTaskByProject(projectMapper.convertToDTO(project));
    }

    @Override
    public void complete(String projectCode) {
        Project projectDTO = projectRepository.findByProjectCode(projectCode);
        projectDTO.setProjectStatus(Status.COMPLETE);
        projectRepository.save(projectDTO);
    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() {
        UserDTO currentUser = userService.findByUserName("harold@manager.com");
        User user = userMapper.convertToEntity(currentUser);
        // get all project with specific manager
        List<Project> list = projectRepository.findAllByAssignedManager(user);
        // I need to set completed and pending task
        //so first convert to the dto and then set the fields
     return list.stream().map(project ->{
            ProjectDTO projectDTO = projectMapper.convertToDTO(project);
            projectDTO.setCompleteTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
            projectDTO.setUnfinishedTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));
            return projectDTO;
        }).collect(Collectors.toList());

    }
}
