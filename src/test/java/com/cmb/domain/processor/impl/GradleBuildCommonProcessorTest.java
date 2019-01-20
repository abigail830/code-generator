package com.cmb.domain.processor.impl;

import com.cmb.domain.engine.Project;
import com.cmb.domain.processor.ProcessFile;
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

public class GradleBuildCommonProcessorTest {

    Project project;
    GradleBuildCommonProcessor gradleBuildCommonProcessor;

    @Before
    public void setUp() throws Exception {
        project = mock(Project.class);
        gradleBuildCommonProcessor = new GradleBuildCommonProcessor();
    }

    @Test
    public void testIsValidToProceedReturnFalseForMavenProject() {
        when(project.getBuildTool()).thenReturn(Constant.TYPE_MAVEN);
        assertFalse(gradleBuildCommonProcessor.isValidToProceed(project));
    }

    @Test
    public void testIsValidToProceedReturnFalseForBuildToolNull() {
        when(project.getBuildTool()).thenReturn(null);
        assertFalse(gradleBuildCommonProcessor.isValidToProceed(project));
    }

    @Test
    public void testIsValidToProceedReturnTrueForGradleProject() {
        when(project.getBuildTool()).thenReturn(Constant.TYPE_GRADLE);
        assertTrue(gradleBuildCommonProcessor.isValidToProceed(project));
    }

    @Test
    public void testConvertFromTemplate() {
        String projectName = "./GradleBuildCommonProcessorTest_testConvertFromTemplate";
        when(project.getName()).thenReturn(projectName);
        when(project.getBuildTool()).thenReturn(Constant.TYPE_GRADLE);

        List<ProcessFile> actual = gradleBuildCommonProcessor.convertFromTemplate(project);

        assertThat(actual.size(), is(1));
        assertTrue(actual.get(0).getName() == null);
    }

    @Test
    public void testProcessMavenProject() {
        String projectName = "./GradleBuildCommonProcessorTest_testProcessMavenProject";
        when(project.getName()).thenReturn(projectName);
        when(project.getBuildTool()).thenReturn(Constant.TYPE_MAVEN);

        gradleBuildCommonProcessor.process(project);
        File targetFile = new File(projectName);
        assertFalse(targetFile.exists());
    }

    @Test
    public void testProcessGradleProject() {
        String projectName = "./GradleBuildCommonProcessorTest_testProcessGradleProject";
        when(project.getName()).thenReturn(projectName);
        when(project.getBuildTool()).thenReturn(Constant.TYPE_GRADLE);

        gradleBuildCommonProcessor.process(project);
        File targetFile = new File(projectName);
        assertTrue(targetFile.exists());
        assertTrue(targetFile.isDirectory());
        assertThat(targetFile.listFiles().length, is(3));

        File gradlew_bat = Paths.get(projectName, "gradlew.bat").toFile();
        assertTrue(gradlew_bat.exists());
        assertTrue(gradlew_bat.isFile());

        File gradlew = Paths.get(projectName, "gradlew").toFile();
        assertTrue(gradlew.exists());
        assertTrue(gradlew.isFile());

        File wrapper_jar = Paths.get(projectName, "gradle", "wrapper", "gradle-wrapper.jar").toFile();
        assertTrue(wrapper_jar.exists());
        assertTrue(wrapper_jar.isFile());

        File wrapper_properties = Paths.get(projectName, "gradle", "wrapper", "gradle-wrapper.properties").toFile();
        assertTrue(wrapper_properties.exists());
        assertTrue(wrapper_properties.isFile());

        FileUtils.delete(projectName);
    }
}