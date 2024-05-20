package com.company.repository;

import com.company.dto.TaskDTO;

import java.util.List;

public interface TaskService {
    List<TaskDTO> listAllTasks();
    void save(TaskDTO taskDTO);
    void delete(Long id);
    void update(TaskDTO taskDTO);
    TaskDTO getById(Long id);

}
