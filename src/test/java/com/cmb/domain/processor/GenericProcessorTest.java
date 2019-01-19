package com.cmb.domain.processor;

import com.cmb.domain.engine.ProjectFile;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static com.cmb.domain.utls.Constant.*;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class GenericProcessorTest {

    GenericProcessor genericProcessor;

    @Before
    public void setUp() throws Exception {
        genericProcessor = new GenericProcessor();
    }

    @Test
    public void testGenerateGradleCommonByCopyFolder() {
        String target = "./testGenerateGradleCommonByCopyFolder";
        Path source = Paths.get(TEMPLATE_BASE_FOLDER, TYPE_BUILD_COMMON, TYPE_GRADLE);

        ProjectFile projectFile = ProjectFile.builder()
                .sourcePath(source.toString())
                .targetPath(target)
                .build();
        genericProcessor.generate(Arrays.asList(projectFile));

        File folder = new File(target);
        assertTrue(folder.exists());
        assertTrue(folder.isDirectory());
        assertThat(folder.listFiles().length, is(3));

        File gradlew_bat = Paths.get(target, "gradlew.bat").toFile();
        assertTrue(gradlew_bat.exists());
        assertTrue(gradlew_bat.isFile());

        File gradlew = Paths.get(target, "gradlew").toFile();
        assertTrue(gradlew.exists());
        assertTrue(gradlew.isFile());

        File wrapper_jar = Paths.get(target, "gradle", "wrapper", "gradle-wrapper.jar").toFile();
        assertTrue(wrapper_jar.exists());
        assertTrue(wrapper_jar.isFile());

        File wrapper_properties = Paths.get(target, "gradle", "wrapper", "gradle-wrapper.properties").toFile();
        assertTrue(wrapper_properties.exists());
        assertTrue(wrapper_properties.isFile());

        FileUtils.delete(target);
    }

    @Test
    public void testGenerateMavenCommonByCopyFolder() {
        String target = "./testGenerateMavenCommonByCopyFolder";
        Path source = Paths.get(TEMPLATE_BASE_FOLDER, TYPE_BUILD_COMMON, TYPE_MAVEN);

        ProjectFile projectFile = ProjectFile.builder()
                .sourcePath(source.toString())
                .targetPath(target)
                .build();
        genericProcessor.generate(Arrays.asList(projectFile));

        File folder = new File(target);
        assertTrue(folder.exists());
        assertTrue(folder.isDirectory());
        assertThat(folder.listFiles().length, is(3));

        File mvnw_cmd = Paths.get(target, "mvnw.cmd").toFile();
        assertTrue(mvnw_cmd.exists());
        assertTrue(mvnw_cmd.isFile());

        File mvnw = Paths.get(target, "mvnw").toFile();
        assertTrue(mvnw.exists());
        assertTrue(mvnw.isFile());

        File maven_wrapper_jar = Paths.get(target, ".mvn", "wrapper", "maven-wrapper.jar").toFile();
        assertTrue(maven_wrapper_jar.exists());
        assertTrue(maven_wrapper_jar.isFile());

        File maven_wrapper_properties = Paths.get(target, ".mvn", "wrapper", "maven-wrapper.properties").toFile();
        assertTrue(maven_wrapper_properties.exists());
        assertTrue(maven_wrapper_properties.isFile());

        FileUtils.delete(target);
    }
}