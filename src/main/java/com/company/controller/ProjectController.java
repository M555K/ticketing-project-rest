package com.company.controller;

import com.company.dto.ProjectDTO;
import com.company.dto.ResponseWrapper;
import com.company.dto.UserDTO;
import com.company.service.ProjectService;
import com.company.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;
@RestController
@RequestMapping("/api/v1/project")
public class ProjectController {

    private final UserService userService;
    private final ProjectService projectService;

    public ProjectController(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;
    }
    @RolesAllowed("Manager")
    @GetMapping
    public ResponseEntity<ResponseWrapper> getProjects(){
        List<ProjectDTO> allProjects = projectService.listAllProjects();
        return ResponseEntity.ok(new ResponseWrapper("Projects are successfully retrieved",allProjects, HttpStatus.OK));
    }
    @RolesAllowed("Manager")
    @GetMapping("/{code}")
    public ResponseEntity<ResponseWrapper> getProjectByCode(@PathVariable String code){
        ProjectDTO project = projectService.getByProjectCode(code);
        return ResponseEntity.ok(new ResponseWrapper("Project is successfully retrieved",project, HttpStatus.OK));
    }
    @RolesAllowed({"Manager,Admin"})
    @PostMapping
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO){

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseWrapper("Project is successfully created", HttpStatus.CREATED));
    }
    @RolesAllowed("Manager")
    @PutMapping
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDTO){
        projectService.update(projectDTO);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ResponseWrapper("Project is successfully updated", HttpStatus.NO_CONTENT));
    }
    @RolesAllowed("Manager")
    @DeleteMapping("/{code}")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable String code){
        projectService.delete(code);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ResponseWrapper("Project is successfully updated", HttpStatus.NO_CONTENT));
    }
    @RolesAllowed("Manager")
    @GetMapping("/manager/project-status")
    public ResponseEntity<ResponseWrapper> getProjectByManager(){
        List<ProjectDTO> allProject=  projectService.listAllProjectDetails();
        return ResponseEntity.ok()
                .body(new ResponseWrapper("Projects is successfully retrieved", allProject,HttpStatus.OK));
    }
    @RolesAllowed("Manager")
    @PutMapping("/manager/complete/{projectCode}")
    public ResponseEntity<ResponseWrapper> managerCompleteProject(@PathVariable("projectCode") String code ){
        projectService.complete(code);
        return ResponseEntity.ok()
                .body(new ResponseWrapper("Project is successfully completed", HttpStatus.OK));
    }
}
