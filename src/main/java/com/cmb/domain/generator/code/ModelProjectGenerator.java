package com.cmb.domain.generator.code;

import com.cmb.domain.engine.Project;
import com.cmb.domain.loader.FileTemplate;
import com.cmb.domain.utls.FormateUtils;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class ModelProjectGenerator extends ProjectFileGenerator {

    private String suffix = "Dto";

    private Map<String, Object> getParameters(Project project, Model model, String clazzName) {

        Map<String, Object> properties = extract(model);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("properties", properties);
        parameters.put("classname", clazzName);
        parameters.put("group", project.getGroup());
        parameters.put("name", project.getName());
        return parameters;
    }

    private Map<String, Object> extract(Model model) {
        Map<String, Object> values = new LinkedHashMap<>();
        Map<String, Property> properties = model.getProperties();
        for (Map.Entry<String, Property> entry : properties.entrySet()) {
            values.put(entry.getKey(), getType(entry.getValue(), suffix));
        }
        return values;
    }


    @Override
    protected Map<String, String> generateContents(String path, Project project, String defaultClazzName) {
        Map<String, String> contents = new HashMap<>();
        for (String clazzName : project.getSwagger().getDefinitions().keySet()) {
            Model model = project.getSwagger().getDefinitions().get(clazzName);
            String clazzFullName = FormateUtils.formateClassName(clazzName + suffix);
            Map<String, Object> parameters = getParameters(project, model, clazzFullName);
            contents.put(clazzFullName, super.getTemplateEngine().generate(path, parameters));
        }
        return contents;
    }

    @Override
    protected boolean canSupport(FileTemplate.model model) {
        return model == FileTemplate.model.dto;
    }

}
