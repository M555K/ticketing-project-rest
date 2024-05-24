package com.company.service;

import com.company.dto.ProjectDTO;
import com.company.dto.TaskDTO;
import com.company.dto.UserDTO;
import com.company.enums.Status;

import java.util.List;

public interface TaskService {
    List<TaskDTO> listAllTasks();
    void save(TaskDTO taskDTO);
    void delete(Long id);
    void update(TaskDTO taskDTO);
    TaskDTO findById(Long id);

    int totalNonCompletedTask(String projectCode);

    int totalCompletedTask(String projectCode);

    void deleteTaskByProject(ProjectDTO projectDTO);

    void completeTaskByProject(ProjectDTO projectDTO);

    List<TaskDTO>listAllTasksByStatusIsNot(Status status);

    List<TaskDTO> listAllTasksByStatus(Status status);

    List<ProjectDTO> listAllNonCompletedByAssignedEmployee(UserDTO employee);
}
