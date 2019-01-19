package com.cmb.apis;

import com.cmb.apis.assembler.ProjectAssembler;
import com.cmb.apis.dto.ProjectDTO;
import com.cmb.application.GeneratorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GeneratorController {
    private static Logger logger = LoggerFactory.getLogger(GeneratorController.class);

    @Autowired
    private GeneratorService service;

    @Autowired
    private ProjectAssembler projectAssembler;

    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(value = "/generate", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    @ApiOperation("generate a project.")
    public ResponseEntity<String> generate(@RequestBody @Validated ProjectDTO projectDTO) throws Exception {
        logger.info(objectMapper.writeValueAsString(projectDTO));

        final String gradleFile = service.generate(projectAssembler.convertToDomainProject(projectDTO));
        return new ResponseEntity<>(gradleFile, HttpStatus.OK);
    }

}
