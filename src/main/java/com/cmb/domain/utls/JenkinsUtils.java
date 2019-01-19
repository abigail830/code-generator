package com.cmb.domain.utls;

import com.cmb.domain.engine.JenkinsConfig;
import com.cmb.domain.templateengine.TemplateEngine;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties("jenkins")
@Getter
@Setter
public class JenkinsUtils {

    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TemplateEngine templateEngine;

    private String auth;

    private HttpHeaders generateHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + Base64.getEncoder().encodeToString(auth.getBytes()));
        return httpHeaders;
    }

    public void publishJenkinsPipeline(String projectName,
                                       String templatePath,
                                       JenkinsConfig jenkinsConfig) {

        String configFromTemplate = generateJenkinsConfigFromTemplate(templatePath, jenkinsConfig);
        createJob(projectName, configFromTemplate);

    }


    public String generateJenkinsConfigFromTemplate(String path, JenkinsConfig jenkinsConfig) {
        Map<String, Object> paramaters = new HashMap<>();
        paramaters.put("jenkinsConfig", jenkinsConfig);
        return templateEngine.generate(path, paramaters);
    }

    public void createJob(String jobName, String config) {

        String url = baseUrl + "/createItem?name=" + jobName;

        HttpHeaders httpHeaders = generateHeader();
        httpHeaders.setContentType(MediaType.APPLICATION_XML);

        HttpEntity request = new HttpEntity<>(config.getBytes(), httpHeaders);

        restTemplate.postForEntity(url, request, String.class);
        buildJob(jobName);
    }

    private void buildJob(String jobName) {
        String url = baseUrl + "/job/" + jobName + "/build";

        HttpHeaders httpHeaders = generateHeader();
        HttpEntity request = new HttpEntity<>(httpHeaders);

        restTemplate.postForObject(url, request, String.class);

    }



}
