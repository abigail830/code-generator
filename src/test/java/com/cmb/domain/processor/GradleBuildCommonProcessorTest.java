package com.cmb.domain.processor;

import com.cmb.domain.engine.Project;
import com.cmb.domain.engine.ProjectFile;
import com.cmb.domain.utls.Constant;
import com.cmb.domain.utls.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
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
        String projectName = "testConvertFromTemplate";
        when(project.getName()).thenReturn(projectName);
        when(project.getBuildTool()).thenReturn(Constant.TYPE_GRADLE);

        List<ProjectFile> actual = gradleBuildCommonProcessor.convertFromTemplate(project);

        assertThat(actual.size(), is(1));
        assertTrue(actual.get(0).getName() == null);
    }

    @Test
    public void testProcessMavenProject() {
        String projectName = "testProcessMavenProject";
        when(project.getName()).thenReturn(projectName);
        when(project.getBuildTool()).thenReturn(Constant.TYPE_MAVEN);

        gradleBuildCommonProcessor.process(project);
        File targetFile = new File(projectName);
        assertFalse(targetFile.exists());
    }

    @Test
    public void testProcessGradleProject() {
        String projectName = "testProcessGradleProject";
        when(project.getName()).thenReturn(projectName);
        when(project.getBuildTool()).thenReturn(Constant.TYPE_GRADLE);

        gradleBuildCommonProcessor.process(project);
        File targetFile = new File(projectName);
        assertTrue(targetFile.exists());

        FileUtils.delete(projectName);
    }
}