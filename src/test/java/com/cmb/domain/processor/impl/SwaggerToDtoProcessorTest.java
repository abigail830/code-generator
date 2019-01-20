package com.cmb.domain.processor.impl;

import com.cmb.domain.processor.ProcessFile;
import com.cmb.domain.project.Dependency;
import com.cmb.domain.project.DependencyManagement;
import com.cmb.domain.project.Project;
import com.cmb.domain.templateengine.VelocityTemplateEngine;
import com.cmb.domain.utls.Constant;
import com.cmb.domain.utls.FileUtils;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SwaggerToDtoProcessorTest {

    Swagger swagger;
    SwaggerParser swaggerParser = new SwaggerParser();
    SwaggerToDtoProcessor swaggerToDtoProcessor;
    Project project;
    VelocityTemplateEngine velocityTemplateEngine;

    @Before
    public void setUp() throws Exception {
        swaggerToDtoProcessor = new SwaggerToDtoProcessor();
        swagger = swaggerParser.read("swagger-example.yml");

        VelocityEngine velocityEngine = new VelocityEngine();
        velocityTemplateEngine = new VelocityTemplateEngine(velocityEngine);
        swaggerToDtoProcessor.templateEngine = velocityTemplateEngine;

        project = mock(Project.class);
        when(project.getName()).thenReturn("code-generator");
        when(project.getSwagger()).thenReturn(swagger);
        when(project.getBuildTool()).thenReturn(Constant.TYPE_MAVEN);
        when(project.getGroup()).thenReturn("cmb");
        when(project.getDecription()).thenReturn("description");
        when(project.getServiceType()).thenReturn("bizservice");
        when(project.getLayerPattern()).thenReturn("microservice");
        when(project.getFramework()).thenReturn("za23");
        when(project.getBootVersion()).thenReturn("bootVersion");
        when(project.getServiceId()).thenReturn("serviceID");
        when(project.getProductId()).thenReturn("productID");
        when(project.getBuildPropertiesGradle()).thenReturn(new HashMap<>());
        when(project.getBuildPropertiesMaven()).thenReturn(new HashMap<>());
        when(project.getMavenParent()).thenReturn(Dependency.builder().build());
        when(project.getDependencies()).thenReturn(Arrays.asList(Dependency.builder().build()));
        when(project.getDependencyManagements()).thenReturn(Arrays.asList(DependencyManagement.builder().build()));
    }

    @Test
    public void testConvertFromTemplate() {
        List<ProcessFile> processFiles = swaggerToDtoProcessor.convertFromTemplate(project);
        assertThat(processFiles.size(), is(6));

        assertThat(processFiles.stream().filter(file -> file.getName().equals("TagDTO.java"))
                .collect(Collectors.toList()).size(), is(1));
    }

    @Test
    public void testProcessSwaggerToDtoFlow() {
        String projectName = "testProcessSwaggerToDtoFlow";
        when(project.getName()).thenReturn(projectName);
        swaggerToDtoProcessor.process(project);

        File targetDir = new File(projectName);
        assertTrue(targetDir.exists());
        assertTrue(targetDir.isDirectory());

        String expect_DtoBasePath = Paths.get(projectName, "src", "main", "java",
                "com", "cmb", projectName, "api", "dto").toString();

        String expect_ApiResponseDTO = Paths.get(expect_DtoBasePath, "ApiResponseDTO.java").toString();
        File actual = new File(expect_ApiResponseDTO);
        assertTrue(actual.exists());
        assertTrue(actual.isFile());

        String expect_UserDTO = Paths.get(expect_DtoBasePath, "UserDTO.java").toString();
        actual = new File(expect_UserDTO);
        assertTrue(actual.exists());
        assertTrue(actual.isFile());

        String expect_TagDTO = Paths.get(expect_DtoBasePath, "TagDTO.java").toString();
        actual = new File(expect_TagDTO);
        assertTrue(actual.exists());
        assertTrue(actual.isFile());

        String expect_PetDTO = Paths.get(expect_DtoBasePath, "PetDTO.java").toString();
        actual = new File(expect_PetDTO);
        assertTrue(actual.exists());
        assertTrue(actual.isFile());

        String expect_OrderDTO = Paths.get(expect_DtoBasePath, "OrderDTO.java").toString();
        actual = new File(expect_OrderDTO);
        assertTrue(actual.exists());
        assertTrue(actual.isFile());

        String expect_CategoryDTO = Paths.get(expect_DtoBasePath, "CategoryDTO.java").toString();
        actual = new File(expect_CategoryDTO);
        assertTrue(actual.exists());
        assertTrue(actual.isFile());

        FileUtils.delete(projectName);
    }
}