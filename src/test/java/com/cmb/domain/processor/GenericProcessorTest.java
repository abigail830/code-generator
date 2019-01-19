package com.cmb.domain.processor;

import com.cmb.domain.engine.Project;
import com.cmb.domain.templateengine.VelocityTemplateEngine;
import com.cmb.domain.utls.Constant;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GenericProcessorTest {

    GenericProcessor genericProcessor;

    VelocityTemplateEngine velocityTemplateEngine;
    VelocityEngine velocityEngine = new VelocityEngine();

    @Before
    public void setUp() throws Exception {
        genericProcessor = new GenericProcessor();
        genericProcessor.templateEngine = new VelocityTemplateEngine(velocityEngine);
    }


    @Test
    public void testGenerateTargetPath() {

        String projectName = "projectName";

        Project project = mock(Project.class);
        when(project.getBuildTool()).thenReturn(Constant.TYPE_MAVEN);
        when(project.getGroup()).thenReturn("group");
        when(project.getDecription()).thenReturn("description");
        when(project.getServiceType()).thenReturn("testservice");
        when(project.getLayerPattern()).thenReturn("testlayer");
        when(project.getFramework()).thenReturn("testFramework");
        when(project.getName()).thenReturn(projectName);


        Path basePath = Paths.get(Constant.TEMPLATE_BASE_FOLDER, project.getServiceType(),
                project.getLayerPattern(), project.getFramework(), Constant.TYPE_SOURCE);

        Path filePath = Paths.get(basePath.toString(),
                "src", "main", "java", "com", Constant.DEFAULT_GROUP, Constant.DEFAULT_NAME, "api");

        String finalPath = genericProcessor.generateTargetPath(basePath.toString(), filePath.toString(), project);

        assertTrue(finalPath.contains("group"));
        assertFalse(finalPath.contains(Constant.DEFAULT_GROUP));

        assertTrue(finalPath.contains(projectName));
        assertFalse(finalPath.contains(Constant.DEFAULT_NAME));

        assertEquals("projectName/src/main/java/com/group/projectName/api", finalPath);

    }
}