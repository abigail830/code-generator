package com.cmb.domain.utls;

import lombok.Getter;
import lombok.Setter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.lib.StoredConfig;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "git")
@Getter
@Setter
public class GitUtils {

    private static Logger logger = LoggerFactory.getLogger(GitUtils.class);

    private RestTemplate restTemplate;

    private String url;

    // TODO: 2018-12-18 gitee oauth2
    private String accessToken;

    private String username;

    private String password;

    @Autowired
    public GitUtils(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void pushProjectToGitRepo(String name) throws Exception {
        try {
            String remoteRepository = createRemoteRepository(name);

            Git git = Git.init().setDirectory(new File(name)).call();
            git.add().addFilepattern(".").call();
            git.commit().setMessage("pushProjectToGitRepo").call();
            // TODO: 2018-12-18 use username & password? or ssh rsa?
            CredentialsProvider cp = new UsernamePasswordCredentialsProvider(username, password);

            StoredConfig config = git.getRepository().getConfig();
            config.setString("remote", "origin", "url", remoteRepository);
            config.save();

            PushCommand pushCommand = git.push();
            pushCommand.setRemote("origin");
            pushCommand.setRefSpecs(new RefSpec("master:master"));
            pushCommand.setCredentialsProvider(cp);
            pushCommand.call();
            git.close();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Exception("上传git代码失败!", e);
        }
    }

    private String createRemoteRepository(String name) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("access_token", accessToken);
        map.add("name", name);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.POST, request, new ParameterizedTypeReference<Map<String, Object>>() {
        });
        return response.getBody().get("html_url").toString();
    }

    public String getUsername() {
        return username;
    }
}

