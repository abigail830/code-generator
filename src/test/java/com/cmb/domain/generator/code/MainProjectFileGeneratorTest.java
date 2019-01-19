package com.cmb.domain.generator.code;

import com.cmb.Application;
import com.cmb.domain.engine.Project;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MainProjectFileGeneratorTest {

    @Autowired
    private MainProjectGenerator generator = new MainProjectGenerator();

    @Test
    public void should_generate_gradle_file() throws IOException {
        Project project = Project.builder().group("group").name("name").build();
        String path = "/src/test/resources/template/application.java.vm";
        Map<String, String> application = generator.generateContents(path, project, "Application");

        StringBuilder buffer = new StringBuilder();
        Files.lines(Paths.get("./src/test/resources/expect/application.txt")).forEachOrdered(a -> buffer.append(a).append("\n"));

        assertThat(application.get("Application"), is(buffer.toString()));


    }
}
