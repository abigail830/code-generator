package com.cmb.application;

import com.cmb.domain.engine.Project;
import com.cmb.domain.processor.GradleBuildCommonProcessor;
import com.cmb.domain.processor.MavenBuildCommonProcessor;
import com.cmb.domain.utls.Constant;
import com.cmb.domain.utls.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GeneratorServiceTest {

    Project project;
    GeneratorService generatorService;

    @Before
    public void setUp() throws Exception {
        project = mock(Project.class);
        generatorService = new GeneratorService();
        generatorService.mavenBuildCommonProcessor = new MavenBuildCommonProcessor();
        generatorService.gradleBuildCommonProcessor = new GradleBuildCommonProcessor();
    }


    @Test
    public void testGenerateBuildCommonForMavenProject() {
        String projectName = "./GeneratorServiceTest_testGenerateBuildCommonForMavenProject";
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
        String projectName = "./GeneratorServiceTest_testGenerateBuildCommonForGradleProject";
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