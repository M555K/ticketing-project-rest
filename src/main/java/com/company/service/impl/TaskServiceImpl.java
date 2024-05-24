package com.company.service.impl;

import com.company.dto.ProjectDTO;
import com.company.dto.TaskDTO;
import com.company.entity.Project;
import com.company.entity.Task;
import com.company.enums.Status;
import com.company.mapper.ProjectMapper;
import com.company.mapper.TaskMapper;
import com.company.repository.TaskRepository;
import com.company.service.TaskService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final ProjectMapper projectMapper;

    public TaskServiceImpl(TaskRepository taskRepository, TaskMapper taskMapper, ProjectMapper projectMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        this.projectMapper = projectMapper;
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
            convertedTask.setTaskStatus(taskDTO.getTaskStatus());
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
}
