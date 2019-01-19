package com.cmb.domain.generator.code;

import com.cmb.domain.engine.Project;
import com.cmb.domain.loader.FileTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DemoProjectFileGenerator extends ProjectFileGenerator {

    @Override
    protected Map<String, String> generateContents(String path, Project project, String defaultClazzName) {
        Map<String, String> result = new HashMap<>();
        result.put(defaultClazzName, super.getTemplateEngine().generate(path, getParameters(project)));
        return result;
    }

    @Override
    protected String getDefaultName(String templateName) {
        return super.getDefaultName(templateName.split("/")[1]);
    }

    @Override
    protected boolean canSupport(FileTemplate.model model) {
        return model == FileTemplate.model.demo;
    }

    private Map<String, Object> getParameters(Project project) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("group", project.getGroup());
        parameters.put("name", project.getName());
        return parameters;
    }

    @Override
    protected String getTargetPath(String folder, Project project, String templateName) {
        if (folder.startsWith("/resources") && !isJavaTemplate(templateName)) {
            return "src/main" + folder;
        }
        return super.getTargetPath(folder, project, templateName);
    }
}
