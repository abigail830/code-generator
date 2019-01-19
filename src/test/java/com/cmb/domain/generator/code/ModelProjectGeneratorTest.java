package com.cmb.domain.generator.code;

import com.cmb.Application;
import com.cmb.domain.engine.Project;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
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
public class ModelProjectGeneratorTest {

    @Autowired
    private ModelProjectGenerator modelProjectGenerator;

    @Test
    public void should_get_request_content_with_swagger_config() throws IOException {

        SwaggerParser swaggerParser = new SwaggerParser();
        Swagger swagger = swaggerParser.read("swagger-definition.yml");

        Project project = Project.builder().group("group").name("name").swagger(swagger).build();

        String path = "/src/test/resources/template/dto.java.vm";

        Map<String, String> contents = modelProjectGenerator.generateContents(path, project, null);

        StringBuilder buffer = new StringBuilder();
        Files.lines(Paths.get("./src/test/resources/expect/orderClass.txt")).forEachOrdered(a -> buffer.append(a).append("\n"));

        assertThat(contents.get("OrderDto"), is(buffer.toString()));
    }

    @Test
    public void should_recognize_ref_with_swagger_config() throws IOException {

        SwaggerParser swaggerParser = new SwaggerParser();
        Swagger swagger = swaggerParser.read("swagger-definition-with-ref.yml");

        Project project = Project.builder().group("group").name("name").swagger(swagger).build();

        String path = "/src/test/resources/template/dto.java.vm";

        Map<String, String> contents = modelProjectGenerator.generateContents(path, project, null);

        StringBuilder buffer = new StringBuilder();
        Files.lines(Paths.get("./src/test/resources/expect/pet.txt")).forEachOrdered(a -> buffer.append(a).append("\n"));

        assertThat(contents.get("PetDto"), is(buffer.toString()));
    }

}
