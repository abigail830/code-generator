package com.cmb.domain.generator.old;

import com.cmb.domain.engine.Project;
import com.cmb.domain.loader.FileTemplate;
import com.cmb.domain.utls.FormateUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TestProjectFileGenerator extends ProjectFileGenerator {
    @Override
    protected Map<String, String> generateContents(String path, Project project, String defaultClazzName) {
        Map<String, String> clazzNameToContent = new HashMap<>();
        defaultClazzName = FormateUtils.formateClassName(defaultClazzName);
        clazzNameToContent.put(defaultClazzName, super.getTemplateEngine().generate(path, getParameters(project, defaultClazzName)));
        return clazzNameToContent;
    }

    @Override
    protected boolean canSupport(FileTemplate.model model) {
        return model == FileTemplate.model.test;
    }

    @Override
    protected String getTargetPath(String folder, Project project, String templateName) {
        return String.format("src/test/java/com/%s/%s/" + folder, project.getGroup(), project.getName());
    }

    private Map<String, Object> getParameters(Project project, String defaultClazzName) {

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("name", project.getName());
        parameters.put("group", project.getGroup());
        parameters.put("classname", defaultClazzName);

        return parameters;
    }
}
