package com.company.service;

import com.company.dto.ProjectDTO;

import java.util.List;

public interface ProjectService {
    ProjectDTO getByProjectCode(String code);
    List<ProjectDTO> listAllProjects();
    void save(ProjectDTO projectDTO);
    void update(ProjectDTO projectDTO);
    void delete(String projectDTO);
    void complete(String projectCode);
}
