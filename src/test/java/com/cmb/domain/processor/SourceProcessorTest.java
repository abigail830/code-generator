package com.cmb.domain.processor;

import com.cmb.domain.engine.*;
import com.cmb.domain.templateengine.VelocityTemplateEngine;
import com.cmb.domain.utls.Constant;
import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SourceProcessorTest {

    SourceProcessor sourceProcessor;
    Project project;
    VelocityTemplateEngine velocityTemplateEngine;

    @Before
    public void setUp() throws Exception {
        sourceProcessor = new SourceProcessor();
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityTemplateEngine = new VelocityTemplateEngine(velocityEngine);
        sourceProcessor.templateEngine = velocityTemplateEngine;

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
        when(project.getJenkinsConfig()).thenReturn(JenkinsConfig.builder().build());
        when(project.getDependencies()).thenReturn(Arrays.asList(Dependency.builder().build()));
        when(project.getDependencyManagements()).thenReturn(Arrays.asList(DependencyManagement.builder().build()));
    }

    @Test
    public void convertSourceFromTemplate() {

        String projectName = "tonvertSourceFromTemplate";
        when(project.getName()).thenReturn(projectName);

        List<ProjectFile> projectFiles = sourceProcessor.convertFromTemplate(project);

        assertThat(projectFiles.size(), is(6));

        //assert Place_Holder
        assertThat(projectFiles.stream()
                        .filter(projectFile -> projectFile.getName().equals("Place_Holder"))
                        .collect(Collectors.toList()).size(),
                is(1));

        String expect = projectName +
                "/src/main/java/com/group/" + projectName + "/api/dto/Place_Holder";
        assertThat(projectFiles.stream()
                        .filter(projectFile -> projectFile.getTargetPath().equals(expect))
                        .collect(Collectors.toList()).size(),
                is(1));

        //assert .gitignore
        assertThat(projectFiles.stream()
                        .filter(projectFile -> projectFile.getName().equals(".gitignore"))
                        .collect(Collectors.toList()).size(),
                is(1));

        String expect2 = projectName + "/.gitignore";
        assertThat(projectFiles.stream()
                        .filter(projectFile -> projectFile.getTargetPath().equals(expect2))
                        .collect(Collectors.toList()).size(),
                is(1));
    }

    @Test
    public void testFileWalk() {
        Path sourcePath = Paths.get(Constant.TEMPLATE_BASE_FOLDER,
                "testservice", "testlayer", "testFramework", Constant.TYPE_SOURCE);
        try {
            assertThat(Files.walk(sourcePath)
                    .filter(path -> Files.isRegularFile(path))
                    .count(), is(6L));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testSourceProcessor() {
        String projectName = "testSourceProcessor";
        when(project.getName()).thenReturn(projectName);

        sourceProcessor.process(project);

        File targetDir = new File(projectName);
        assertTrue(targetDir.exists());
        assertTrue(targetDir.isDirectory());

        try {
            assertThat(Files.walk(targetDir.toPath()).filter(path -> Files.isRegularFile(path)).count(), is(6L));
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        FileUtils.delete(projectName);

    }


}