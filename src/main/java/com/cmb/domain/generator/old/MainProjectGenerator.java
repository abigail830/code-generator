package com.cmb.domain.generator.old;

import com.cmb.domain.engine.Project;
import com.cmb.domain.loader.FileTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MainProjectGenerator extends ProjectFileGenerator {

    @Override
    protected Map<String, String> generateContents(String path, Project project, String defaultClazzName) {
        Map<String, String> clazzNameToContent = new HashMap<>();
        clazzNameToContent.put(defaultClazzName, super.getTemplateEngine().generate(path, getParameters(project)));
        return clazzNameToContent;
    }

    @Override
    protected boolean canSupport(FileTemplate.model model) {
        return model == FileTemplate.model.application;
    }

    private Map<String, Object> getParameters(Project project) {

        Map<String, Object> paramaters = new HashMap<>();

        paramaters.put("name", project.getName());
        paramaters.put("group", project.getGroup());

        return paramaters;
    }
}
