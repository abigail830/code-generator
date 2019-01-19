package com.cmb.domain.generator.old;


import com.cmb.Application;
import com.cmb.domain.engine.JenkinsConfig;
import com.cmb.domain.utls.JenkinsUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JenkinsUtilsTest {
    @Autowired
    private JenkinsUtils jenkinsUtils;

    @Test
    @Ignore
    public void should_create_a_jenkins_job() {
        JenkinsConfig jenkinsConfig = JenkinsConfig.builder()
                .gitBranch("master")
                .gitUrl("https://gitee.com/leHuer/codegenerator1.git")
                .scriptPath("Jenkinsfile")
                .description("test description")
                .build();

        String path = "./template/resources/jenkins-config.xml.vm";
        String config = jenkinsUtils.generateJenkinsConfigFromTemplate(path, jenkinsConfig);

        jenkinsUtils.createJob("test4", config);
    }
}
