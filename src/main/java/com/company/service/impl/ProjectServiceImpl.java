package com.company.service.impl;

import com.company.dto.ProjectDTO;
import com.company.entity.Project;
import com.company.enums.Status;
import com.company.mapper.ProjectMapper;
import com.company.repository.ProjectRepository;
import com.company.service.ProjectService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
private final ProjectRepository projectRepository;
private final ProjectMapper projectMapper;

    public ProjectServiceImpl(ProjectRepository projectRepository, ProjectMapper projectMapper) {
        this.projectRepository = projectRepository;
        this.projectMapper = projectMapper;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        return null;
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

    }

    @Override
    public void delete(ProjectDTO projectDTO) {

    }
}
