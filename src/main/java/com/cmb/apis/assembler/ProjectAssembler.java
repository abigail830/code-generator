package com.cmb.apis.assembler;

import com.cmb.apis.dto.ProjectDTO;
import com.cmb.application.ProjectFactory;
import com.cmb.domain.project.Project;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectAssembler {
    @Autowired
    private ModelMapper mapper;

    @Autowired
    private ProjectFactory projectFactory;


    public Project convertToDomainProject(ProjectDTO projectDTO) {
        Project project = mapper.map(projectDTO, Project.class);
        return projectFactory.setSwaggerIfURLProvided(project, projectDTO.getSwaggerUrl());
    }

}
