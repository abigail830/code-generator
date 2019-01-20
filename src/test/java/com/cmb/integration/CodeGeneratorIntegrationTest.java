package com.cmb.integration;

import com.cmb.Application;
import com.cmb.apis.dto.ProjectDTO;
import com.cmb.domain.project.Dependency;
import com.cmb.domain.utls.FileUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CodeGeneratorIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void before() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }


    @Test
    public void testGenerateMavenZA23Project() throws Exception {
        List<Dependency> dependencies = Lists.newArrayList(
                Dependency.builder().option("compile").group("org.apache.velocity").name("velocity-engine-core").version("2.0").build()
        );

        Map<String, String> mavenPropertiesMaven = new HashMap<>();
        mavenPropertiesMaven.put("java.version", "1.8");
        Dependency mavenParent = Dependency.builder()
                .group("org.springframework.boot")
                .name("spring-boot-starter-parent")
                .version("2.1.1.RELEASE")
                .build();

        ProjectDTO project = ProjectDTO.builder()
                .name("code-generator")
                .group("cmb")
                .framework("za23")
                .layerPattern("microservice")
                .buildTool("maven")
                .serviceType("bizservice")
                .gitUrl("https://gitee.com/leHuer")
                .dependencies(dependencies)
                .mavenParent(mavenParent)
                .decription("test")
                .bootVersion("2.1.1.RELEASE")
                .build();

        String content = objectMapper.writeValueAsString(project);
        MvcResult result = mockMvc.perform(post("/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andReturn();// 返回执行请求的结果

        String path = System.getProperty("user.dir") + File.separator + result.getResponse().getContentAsString();

        String projectName = "codegenerator";
        File targetDir = new File(projectName);
        assert targetDir.exists();

        File mvnw = Paths.get(projectName, "mvnw").toFile();
        assertTrue(mvnw.exists());
        assertTrue(mvnw.isFile());

        File pom = Paths.get(projectName, "pom.xml").toFile();
        assertTrue(pom.exists());
        assertTrue(pom.isFile());

        File Place_Holder = Paths.get(projectName, "src", "main", "java", "com", "cmb", "codegenerator", "api", "dto", "Place_Holder").toFile();
        assertTrue(Place_Holder.exists());
        assertTrue(Place_Holder.isFile());

        FileUtils.delete(projectName);
    }

}
