package com.company.service;

import com.company.dto.ProjectDTO;
import com.company.dto.TaskDTO;

import java.util.List;

public interface TaskService {
    List<TaskDTO> listAllTasks();
    void save(TaskDTO taskDTO);
    void delete(Long id);
    void update(TaskDTO taskDTO);
    TaskDTO getById(Long id);

    int totalNonCompletedTask(String projectCode);

    int totalCompletedTask(String projectCode);

    void deleteTaskByProject(ProjectDTO projectDTO);

    void completeTaskByProject(ProjectDTO projectDTO);
}
