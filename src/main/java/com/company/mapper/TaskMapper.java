package com.company.mapper;

import com.company.entity.Task;
import com.company.dto.TaskDTO;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    private final ModelMapper modelMapper;

    public TaskMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Task convertToEntity(TaskDTO TaskDTO){
        return modelMapper.map(TaskDTO,Task.class);
    }
    public TaskDTO convertToDTO(Task entity){
        return modelMapper.map(entity,TaskDTO.class);
    }
}
