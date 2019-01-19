package com.cmb.domain.generator.code;

import com.cmb.Application;
import com.cmb.domain.engine.Dependency;
import com.cmb.domain.engine.DependencyManagement;
import com.cmb.domain.engine.Project;
import com.google.common.collect.Lists;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class BuildToolProjectGeneratorTest {

    @Autowired
    private BuildToolProjectGenerator buildToolProjectGenerator;

    @Test
    public void should_generate_gradle_file() throws IOException {
        List<Dependency> dependencies = Lists.newArrayList(
                Dependency.builder().option("compile").group("org.springframework.boot").name("spring-boot-starter").build(),
                Dependency.builder().option("compile").group("com.google.guava").name("guava").version("20.0").build(),
                Dependency.builder().option("testCompile").group("junit").name("junit").version("4.12").build()
        );
        List<DependencyManagement> dependenccyManageMents = Lists.newArrayList(
                DependencyManagement.builder().group("org.springframework.boot").name("spring-boot-starter").version("2.1.1.RELEASE").build()
        );
        Project project = Project.builder().group("group").name("name")
                .dependencies(dependencies)
                .dependencyManagements(dependenccyManageMents)
                .build();
        String path = "/src/test/resources/template/build.gradle.vm";
        Map<String, String> gradle = buildToolProjectGenerator.generateContents(path, project, "gradle");

        StringBuilder buffer = new StringBuilder();
        Files.lines(Paths.get("./src/test/resources/expect/gradle.txt")).forEachOrdered(a -> buffer.append(a).append("\n"));

        assertThat(gradle.get("gradle").replace("\r",""), is(buffer.toString()));

    }
}
