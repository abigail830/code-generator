package com.cmb.domain.generator.code;

import com.cmb.Application;
import com.cmb.domain.engine.JenkinsConfig;
import com.cmb.domain.engine.Project;
import org.junit.Ignore;
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


@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class JenkinsProjectGeneratorTest {

    @Autowired
    private JenkinsfileProjectGenerator jenkinsProjectGenerator;

    @Test
    public void should_generate_gradle_file() throws IOException {
        JenkinsConfig jenkinsConfig = JenkinsConfig.builder()
                .gitBranch("master")
                .gitUrl("https://gitee.com/leHuer/codegenerator1.git")
                .scriptPath("Jenkinsfile")
                .description("test description")
                .build();

        Project project = Project.builder()
                .jenkinsConfig(jenkinsConfig)
                .build();
        String path = "/src/test/resources/template/jenkins-config.xml.vm";
        Map<String, String> config = jenkinsProjectGenerator.generateContents(path, project, "config");

        StringBuilder sb = new StringBuilder();
        Files.lines(Paths.get("./src/test/resources/expect/jenkins-config.txt")).forEachOrdered(a -> sb.append(a).append("\n"));

        assertThat(config.get("config").replace("\r", ""), is(sb.toString()));

    }
}
