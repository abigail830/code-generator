package com.cmb.domain.generator.code;

import com.cmb.domain.engine.Project;
import com.cmb.domain.loader.FileTemplate;
import com.cmb.domain.utls.FileUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BuildToolProjectGenerator extends ProjectFileGenerator {

    @Override
    protected Map<String, String> generateContents(String path, Project project, String defaultClazzName) {
        Map<String, String> clazzNameToContent = new HashMap<>();
        String contents = path;
        if (!FileUtils.isDiretory(path)) {
            contents = super.getTemplateEngine().generate(path, getParameters(project));
        }
        clazzNameToContent.put(defaultClazzName, contents);
        return clazzNameToContent;
    }

    @Override
    protected boolean canSupport(FileTemplate.model model) {
        return model == FileTemplate.model.buildTool;
    }

    protected Map<String, Object> getParameters(Project project) {
        Map<String, Object> paramaters = new HashMap<>();
        paramaters.put("name", project.getName());
        paramaters.put("group", project.getGroup());
        paramaters.put("description", project.getDecription());
        paramaters.put("dependencies", project.getDependencies());
        paramaters.put("dependencyManagements", project.getDependencyManagements());
        paramaters.put("buildPropertiesGradle", project.getBuildPropertiesGradle());
        paramaters.put("buildPropertiesMaven", project.getBuildPropertiesMaven());
        paramaters.put("jenkinsConfig", project.getJenkinsConfig());
        paramaters.put("mavenParent", project.getMavenParent());
        paramaters.put("bootVersion", project.getBootVersion());
        paramaters.put("productId", project.getProductId());
        paramaters.put("serviceId", project.getServiceId());
        return paramaters;
    }

}
