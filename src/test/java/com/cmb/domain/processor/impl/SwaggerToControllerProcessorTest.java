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

public class SwaggerToControllerProcessorTest {


    Swagger swagger;
    SwaggerParser swaggerParser = new SwaggerParser();
    SwaggerToControllerProcessor swaggerToControllerProcessor;
    Project project;
    VelocityTemplateEngine velocityTemplateEngine;

    @Before
    public void setUp() throws Exception {

        swaggerToControllerProcessor = new SwaggerToControllerProcessor();
        swagger = swaggerParser.read("swagger-example.yml");

        VelocityEngine velocityEngine = new VelocityEngine();
        velocityTemplateEngine = new VelocityTemplateEngine(velocityEngine);
        swaggerToControllerProcessor.templateEngine = velocityTemplateEngine;

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
        List<ProcessFile> processFiles = swaggerToControllerProcessor.convertFromTemplate(project);

        assertThat(processFiles.size(), is(3));

        assertThat(processFiles.stream().filter(file -> file.getName().equals("StoreController.java"))
                .collect(Collectors.toList()).size(), is(1));
        assertThat(processFiles.stream().filter(file -> file.getName().equals("UserController.java"))
                .collect(Collectors.toList()).size(), is(1));
        assertThat(processFiles.stream().filter(file -> file.getName().equals("PetController.java"))
                .collect(Collectors.toList()).size(), is(1));
    }

    @Test
    public void testProcessSwaggerToControllerFlow() {
        String projectName = "testProcessSwaggerToControllerFlow";
        when(project.getName()).thenReturn(projectName);
        swaggerToControllerProcessor.process(project);

        File targetDir = new File(projectName);
        assertTrue(targetDir.exists());
        assertTrue(targetDir.isDirectory());

        String expect_DtoBasePath = Paths.get(projectName, "src", "main", "java",
                "com", "cmb", projectName, "api").toString();

        String expect = Paths.get(expect_DtoBasePath, "StoreController.java").toString();
        File actual = new File(expect);
        assertTrue(actual.exists());
        assertTrue(actual.isFile());

        expect = Paths.get(expect_DtoBasePath, "UserController.java").toString();
        actual = new File(expect);
        assertTrue(actual.exists());
        assertTrue(actual.isFile());

        expect = Paths.get(expect_DtoBasePath, "PetController.java").toString();
        actual = new File(expect);
        assertTrue(actual.exists());
        assertTrue(actual.isFile());

        FileUtils.delete(projectName);
    }
}