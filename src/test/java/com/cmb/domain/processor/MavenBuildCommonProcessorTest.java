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
        String projectName = "testConvertMavenProjectFromTemplate";
        when(project.getName()).thenReturn(projectName);
        when(project.getBuildTool()).thenReturn(Constant.TYPE_MAVEN);

        List<ProjectFile> actual = mavenBuildCommonProcessor.convertFromTemplate(project);

        assertThat(actual.size(), is(1));
        assertTrue(actual.get(0).getName() == null);
    }

    @Test
    public void testProcessGradleProjectGenerateNth() {
        String projectName = "testProcessGradleProjectGenerateNth";
        when(project.getName()).thenReturn(projectName);
        when(project.getBuildTool()).thenReturn(Constant.TYPE_GRADLE);

        mavenBuildCommonProcessor.process(project);
        File targetFile = new File(projectName);
        assertFalse(targetFile.exists());
    }

    @Test
    public void testProcessMavenProjectSuccess() {
        String projectName = "testProcessMavenProjectSuccess";
        when(project.getName()).thenReturn(projectName);
        when(project.getBuildTool()).thenReturn(Constant.TYPE_MAVEN);

        mavenBuildCommonProcessor.process(project);
        File targetFile = new File(projectName);
        assertTrue(targetFile.exists());

        FileUtils.delete(projectName);
    }

}