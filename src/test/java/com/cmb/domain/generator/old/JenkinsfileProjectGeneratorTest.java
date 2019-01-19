package com.cmb.domain.generator.old;

import com.cmb.Application;
import com.cmb.domain.engine.Project;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
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
public class JenkinsfileProjectGeneratorTest {

    @Autowired
    private JenkinsfileProjectGenerator jenkinsfileProjectGenerator;

    @Test
    public void should_return_jenkinsfiles() throws IOException {
        SwaggerParser swaggerParser = new SwaggerParser();
        Swagger swagger = swaggerParser.read("swagger-example.yml");

        Project project = Project.builder().gitUrl("https://gitee.com/leHuer").name("test").swagger(swagger).build();

        String path = "/src/test/resources/template/Jenkinsfile.vm";


        Map<String, String> jenkinsfile = jenkinsfileProjectGenerator.generateContents(path, project, "Jenkinsfile");
        String content = jenkinsfile.get("Jenkinsfile");

        StringBuilder buffer = new StringBuilder();
        Files.lines(Paths.get("./src/test/resources/expect/jenkinsfile.txt")).forEachOrdered(a -> buffer.append(a).append("\n"));
        assertThat(content, is(buffer.toString()));

    }
}
