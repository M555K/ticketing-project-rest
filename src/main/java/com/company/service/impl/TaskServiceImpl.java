package com.company.service.impl;

import com.company.dto.ProjectDTO;
import com.company.dto.TaskDTO;
import com.company.dto.UserDTO;
import com.company.entity.Project;
import com.company.entity.Task;
import com.company.enums.Status;
import com.company.mapper.ProjectMapper;
import com.company.mapper.TaskMapper;
import com.company.mapper.UserMapper;
import com.company.repository.TaskRepository;
import com.company.service.TaskService;
import com.company.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectMapper projectMapper;
    private final UserService userService;
    private final UserMapper userMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectMapper projectMapper, UserService userService, UserMapper userMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectMapper = projectMapper;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Override
    public List<TaskDTO> listAllTasks() {
        List<Task> allTask  =  taskRepository.findAll();
        return allTask.stream().map(taskMapper::convertToDTO).toList();
    }

    @Override
    public void save(TaskDTO taskDTO) {
        taskDTO.setTaskStatus(Status.OPEN);
        taskDTO.setAssignedDate(LocalDate.now());
        taskRepository.save(taskMapper.convertToEntity(taskDTO));
    }

    @Override
    public void delete(Long id) {
        Optional<Task> task = taskRepository.findById(id);
        if(task.isPresent()){
        task.get().setIsDeleted(true);
        taskRepository.save(task.get());
        }
    }

    @Override
    public void update(TaskDTO taskDTO) {
        Optional<Task> task = taskRepository.findById(taskDTO.getId());
        Task convertedTask = taskMapper.convertToEntity(taskDTO);
        if (task.isPresent()){
            // if the status is null get from DB, else get from UI
            convertedTask.setTaskStatus(taskDTO.getTaskStatus()== null ? task.get().getTaskStatus(): taskDTO.getTaskStatus());
        }
        taskRepository.save(convertedTask);
    }

    @Override
    public TaskDTO getById(Long id) {
    //  return taskMapper.convertToDTO(taskRepository.findById(id).get());
        Optional<Task> task = taskRepository.findById(id);
//        if(task.isPresent()){
//            return taskMapper.convertToDTO(task.get());
//        }
        return task.map(taskMapper::convertToDTO).orElse(null);
    }

    @Override
    public int totalNonCompletedTask(String projectCode) {
        return taskRepository.totalNonCompletedTasks(projectCode);
    }

    @Override
    public int totalCompletedTask(String projectCode) {
        return taskRepository.totalCompletedTasks(projectCode);
    }

    @Override
    public void deleteTaskByProject(ProjectDTO projectDTO) {
        //to search for related task of the project in the repository
        Project project = projectMapper.convertToEntity(projectDTO);
        List<Task> allTasks = taskRepository.findAllByProject(project);
        //using delete() from taskService to delete all tasks
        allTasks.forEach(task->delete(task.getId()));
    }

    @Override
    public void completeTaskByProject(ProjectDTO projectDTO) {
        //to search for related task of the project in the repository
        Project project = projectMapper.convertToEntity(projectDTO);
        List<Task> allTasks = taskRepository.findAllByProject(project);
        //using update() from taskService to delete all tasks
        allTasks.stream().map(taskMapper::convertToDTO).forEach(taskDto->{
            taskDto.setTaskStatus(Status.COMPLETE);
            update(taskDto);
        });
    }

    @Override
    public List<TaskDTO> listAllTasksByStatusIsNot(Status status) {
        UserDTO loggedInUser = userService.findByUserName("john@employee.com");
        // get task of the employee
        List<Task> tasks = taskRepository.findAllByTaskStatusIsNotAndAssignedEmployee(status,userMapper.convertToEntity(loggedInUser));
        return tasks.stream().map(taskMapper::convertToDTO).toList();
    }

    @Override
    public List<TaskDTO> listAllTasksByStatus(Status status) {
        UserDTO loggedInUser = userService.findByUserName("john@employee.com");
        // get task of the employee
        List<Task> tasks = taskRepository.findAllByTaskStatusAndAssignedEmployee(status,userMapper.convertToEntity(loggedInUser));
        return tasks.stream().map(taskMapper::convertToDTO).toList();
    }
}
