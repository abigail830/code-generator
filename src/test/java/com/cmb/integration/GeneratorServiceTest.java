package com.cmb.integration;


import com.cmb.Application;
import com.cmb.application.GeneratorService;
import com.cmb.domain.project.Dependency;
import com.cmb.domain.project.DependencyManagement;
import com.cmb.domain.project.Project;
import com.cmb.domain.utls.Constant;
import com.cmb.domain.utls.FileUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class GeneratorServiceTest {

    Project project;

    @Autowired(required = true)
    GeneratorService generatorService;

    @Before
    public void setUp() throws Exception {
        project = mock(Project.class);
        when(project.getBuildTool()).thenReturn(Constant.TYPE_MAVEN);
        when(project.getGroup()).thenReturn("group");
        when(project.getDecription()).thenReturn("description");
        when(project.getServiceType()).thenReturn("testservice");
        when(project.getLayerPattern()).thenReturn("testlayer");
        when(project.getFramework()).thenReturn("testFramework");
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
    public void testGenerateBuildCommonForMavenProject() {
        String projectName = "./GeneratorServiceTest_maven";
        when(project.getName()).thenReturn(projectName);
        when(project.getBuildTool()).thenReturn(Constant.TYPE_MAVEN);

        try {
            generatorService.generate(project);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        File targetFile = new File(projectName);
        assertTrue(targetFile.exists());

        File mvnw_cmd = Paths.get(projectName, "mvnw.cmd").toFile();
        assertTrue(mvnw_cmd.exists());
        assertTrue(mvnw_cmd.isFile());

        File gradlew_bat = Paths.get(projectName, "gradlew.bat").toFile();
        assertFalse(gradlew_bat.exists());

        FileUtils.delete(projectName);
    }

    @Test
    public void testGenerateBuildCommonForGradleProject() {
        String projectName = "./GeneratorServiceTest_gradle";
        when(project.getName()).thenReturn(projectName);
        when(project.getBuildTool()).thenReturn(Constant.TYPE_GRADLE);

        try {
            generatorService.generate(project);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }

        File targetDir = new File(projectName);
        assertTrue(targetDir.exists());

        File mvnw_cmd = Paths.get(projectName, "mvnw.cmd").toFile();
        assertFalse(mvnw_cmd.exists());

        File gradlew_bat = Paths.get(projectName, "gradlew.bat").toFile();
        assertTrue(gradlew_bat.exists());
        assertTrue(gradlew_bat.isFile());

        FileUtils.delete(projectName);
    }
}