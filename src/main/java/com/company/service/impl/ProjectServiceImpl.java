package com.company.service.impl;

import com.company.dto.ProjectDTO;
import com.company.service.ProjectService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectServiceImpl implements ProjectService {
    @Override
    public ProjectDTO getByProjectCode(String code) {
        return null;
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        return List.of();
    }

    @Override
    public void save(ProjectDTO projectDTO) {

    }

    @Override
    public void update(ProjectDTO projectDTO) {

    }

    @Override
    public void delete(ProjectDTO projectDTO) {

    }
}
