package com.cmb.domain.generator.code;

import com.cmb.Application;
import com.cmb.apis.dto.ProjectDTO;
import com.cmb.domain.engine.Dependency;
import com.cmb.domain.engine.JenkinsConfig;
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
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class GenerateProjectTest {

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
    public void getPath() {
        String path = System.getProperty("user.dir");
        assert !StringUtils.isEmpty(path);
    }

    @Test
    public void should_generate_sc_gradle() throws Exception {

        List<Dependency> dependencies = Lists.newArrayList(
                Dependency.builder().option("compile").group("org.springframework.boot").name("spring-boot-starter").build(),
                Dependency.builder().option("compile").group("com.google.guava").name("guava").version("20.0").build(),
                Dependency.builder().option("compile").group("org.apache.velocity").name("velocity-engine-core").version("2.0").build()
        );
        String name = "code-generator";
        Map<String, String> buildPropertiesGradle = new HashMap<>();
        buildPropertiesGradle.put("springBootVersion", "2.1.1.RELEASE");
        JenkinsConfig jenkinsConfig = JenkinsConfig.builder()
                .gitBranch("master")
                .gitUrl("https://gitee.com/cxxiong/"+name+".git")
                .scriptPath("Jenkinsfile")
                .description("test description")
                .build();
        ProjectDTO project = ProjectDTO.builder()
                .name(name)
                .group("cmb")
                .framework("sc")
                .layerPattern("microservice")
                .buildTool("gradle")
                .serviceType("bizservice")
                .dependencies(dependencies)
                .jenkinsConfig(jenkinsConfig)
                .gitUrl("https://gitee.com/cxxiong")
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

        File file = new File(path);
        assert file.exists();
    }

    @Test
    public void should_generate_sc_maven() throws Exception {

        List<Dependency> dependencies = Lists.newArrayList(
                Dependency.builder().option("compile").group("org.apache.velocity").name("velocity-engine-core").version("2.0").build()
        );

        Map<String, String> mavenPropertiesMaven = new HashMap<>();
        mavenPropertiesMaven.put("java.version", "1.8");

        JenkinsConfig jenkinsConfig = JenkinsConfig.builder()
                .gitBranch("master")
                .gitUrl("https://gitee.com/leHuer/codegenerator1.git")
                .scriptPath("Jenkinsfile")
                .description("test description")
                .build();
        ProjectDTO project = ProjectDTO.builder()
                .name("codegenerator1")
                .group("cmb")
                .framework("sc")
                .layerPattern("microservice")
                .buildTool("maven")
                .serviceType("bizservice")
                .dependencies(dependencies)
                .buildPropertiesMaven(mavenPropertiesMaven)
                .jenkinsConfig(jenkinsConfig)
                .gitUrl("https://gitee.com/leHuer")
                .bootVersion("2.1.1.RELEASE")
                .productId("productId")
                .serviceId("serviceId")
                .build();

        String content = objectMapper.writeValueAsString(project);
        MvcResult result = mockMvc.perform(post("/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andReturn();// 返回执行请求的结果

        String path = System.getProperty("user.dir") + File.separator + result.getResponse().getContentAsString();

        File file = new File(path);
        assert file.exists();
    }

    @Test
    public void should_generate_za23_maven() throws Exception {
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

        JenkinsConfig jenkinsConfig = JenkinsConfig.builder()
                .gitBranch("master")
                .gitUrl("https://gitee.com/leHuer/codegenerator.git")
                .scriptPath("Jenkinsfile")
                .description("test description")
                .build();
        ProjectDTO project = ProjectDTO.builder()
                .name("code-generator")
                .group("cmb")
                .framework("za23")
                .layerPattern("microservice")
                .buildTool("maven")
                .serviceType("bizservice")
                .jenkinsConfig(jenkinsConfig)
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

        File file = new File(path);
        assert file.exists();

    }

    @Test
    public void should_generate_with_params() throws Exception {

        ProjectDTO project = ProjectDTO.builder()
                .name("code-generator")
                .group("test")
                .buildTool("gradle")
                .build();

        String content = objectMapper.writeValueAsString(project);
        MvcResult result = mockMvc.perform(post("/easy-generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
        )
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andReturn();// 返回执行请求的结果

        String path = System.getProperty("user.dir") + File.separator + result.getResponse().getContentAsString();
        File file = new File(path);
        assert file.exists();

    }
}
