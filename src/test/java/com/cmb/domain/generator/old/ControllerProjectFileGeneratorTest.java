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
public class ControllerProjectFileGeneratorTest {

    @Autowired
    private ControllerProjectFileGenerator controllerProjectFileGenerator = new ControllerProjectFileGenerator();

    @Test
    public void should_generate_controller_for_swagger_yml() throws IOException {
        SwaggerParser swaggerParser = new SwaggerParser();
        Swagger swagger = swaggerParser.read("swagger-example.yml");

        Project project = Project.builder().group("group").name("name").swagger(swagger).build();

        String path = "/src/test/resources/template/controller.java.vm";

        Map<String, String> contents = controllerProjectFileGenerator.generateContents(path, project, null);

        StringBuilder petBuffer = new StringBuilder();
        Files.lines(Paths.get("./src/test/resources/expect/petcontroller.txt")).forEachOrdered(a -> petBuffer.append(a).append("\n"));

        assertThat(contents.get("PetController"), is(petBuffer.toString()));

        StringBuilder storeBuffer = new StringBuilder();
        Files.lines(Paths.get("./src/test/resources/storecontroller.txt")).forEachOrdered(a -> storeBuffer.append(a).append("\n"));

        assertThat(contents.get("StoreController"), is(storeBuffer.toString()));
    }
}
