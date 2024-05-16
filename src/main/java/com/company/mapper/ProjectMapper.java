package com.company.mapper;

import com.company.dto.ProjectDTO;
import com.company.dto.UserDTO;
import com.company.entity.Project;
import com.company.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProjectMapper {
    private final ModelMapper modelMapper;

    public ProjectMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Project convertToEntity(ProjectDTO projectDTO){
        return modelMapper.map(projectDTO,Project.class);
    }
    public ProjectDTO convertToDTO(Project entity){
        return modelMapper.map(entity,ProjectDTO.class);
    }
}
