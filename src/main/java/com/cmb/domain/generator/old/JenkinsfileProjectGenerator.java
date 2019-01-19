package com.cmb.domain.generator.old;

import com.cmb.domain.engine.Project;
import com.cmb.domain.loader.FileTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JenkinsfileProjectGenerator extends ProjectFileGenerator {

    @Override
    protected Map<String, String> generateContents(String path, Project project, String defaultClazzName) {
        Map<String, String> clazzNameToContent = new HashMap<>();
        clazzNameToContent.put(defaultClazzName, super.getTemplateEngine().generate(path, getParameters(project)));
        return clazzNameToContent;
    }

    private Map<String, Object> getParameters(Project project) {
        Map<String, Object> params = new HashMap<>();
        params.put("isMavenProject","maven".equalsIgnoreCase(project.getBuildTool()));
        params.put("repository", "\"" + project.getGitUrl() + "/" + project.getName() + ".git\"");
        return params;
    }

    @Override
    protected boolean canSupport(FileTemplate.model model) {
        return model == FileTemplate.model.ci;
    }

}
