package com.cmb.domain.processor.impl;

import com.cmb.domain.engine.Project;
import com.cmb.domain.engine.ProjectFile;
import com.cmb.domain.utls.Constant;
import com.cmb.domain.utls.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MavenBuildCommonProcessorTest {

    Project project;
    MavenBuildCommonProcessor mavenBuildCommonProcessor;

    @Before
    public void setUp() throws Exception {
        project = mock(Project.class);
        mavenBuildCommonProcessor = new MavenBuildCommonProcessor();
    }

    @Test
    public void testIsValidToProceedReturnTrueForMavenProject() {
        when(project.getBuildTool()).thenReturn(Constant.TYPE_MAVEN);
        assertTrue(mavenBuildCommonProcessor.isValidToProceed(project));
    }

    @Test
    public void testIsValidToProceedReturnFalseForBuildToolNull() {
        when(project.getBuildTool()).thenReturn(null);
        assertFalse(mavenBuildCommonProcessor.isValidToProceed(project));
    }

    @Test
    public void testIsValidToProceedReturnTrueForGradleProject() {
        when(project.getBuildTool()).thenReturn(Constant.TYPE_GRADLE);
        assertFalse(mavenBuildCommonProcessor.isValidToProceed(project));
    }

    @Test
    public void testConvertMavenProjectFromTemplate() {
        String projectName = "./MavenBuildCommonProcessorTest_testConvertMavenProjectFromTemplate";
        when(project.getName()).thenReturn(projectName);
        when(project.getBuildTool()).thenReturn(Constant.TYPE_MAVEN);

        List<ProjectFile> actual = mavenBuildCommonProcessor.convertFromTemplate(project);

        assertThat(actual.size(), is(1));
        assertTrue(actual.get(0).getName() == null);
    }

    @Test
    public void testProcessGradleProjectGenerateNth() {
        String projectName = "./MavenBuildCommonProcessorTest_testProcessGradleProjectGenerateNth";
        when(project.getName()).thenReturn(projectName);
        when(project.getBuildTool()).thenReturn(Constant.TYPE_GRADLE);

        mavenBuildCommonProcessor.process(project);
        File targetFile = new File(projectName);
        assertFalse(targetFile.exists());
    }

    @Test
    public void testProcessMavenProjectSuccess() {
        String projectName = "./MavenBuildCommonProcessorTest_testProcessMavenProjectSuccess";
        when(project.getName()).thenReturn(projectName);
        when(project.getBuildTool()).thenReturn(Constant.TYPE_MAVEN);

        mavenBuildCommonProcessor.process(project);
        File targetFile = new File(projectName);
        assertTrue(targetFile.exists());
        assertTrue(targetFile.isDirectory());
        assertThat(targetFile.listFiles().length, is(3));

        File mvnw_cmd = Paths.get(projectName, "mvnw.cmd").toFile();
        assertTrue(mvnw_cmd.exists());
        assertTrue(mvnw_cmd.isFile());

        File mvnw = Paths.get(projectName, "mvnw").toFile();
        assertTrue(mvnw.exists());
        assertTrue(mvnw.isFile());

        File maven_wrapper_jar = Paths.get(projectName, ".mvn", "wrapper", "maven-wrapper.jar").toFile();
        assertTrue(maven_wrapper_jar.exists());
        assertTrue(maven_wrapper_jar.isFile());

        File maven_wrapper_properties = Paths.get(projectName, ".mvn", "wrapper", "maven-wrapper.properties").toFile();
        assertTrue(maven_wrapper_properties.exists());
        assertTrue(maven_wrapper_properties.isFile());

        FileUtils.delete(projectName);
    }

}