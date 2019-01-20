package com.cmb.domain.processor.impl;

import com.cmb.domain.processor.ProcessFile;
import com.cmb.domain.project.Project;
import com.cmb.domain.utls.Constant;
import com.cmb.domain.utls.FormatUtils;
import com.cmb.domain.utls.TargetPathHelper;
import io.swagger.models.Model;
import io.swagger.models.properties.Property;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.*;

@Component
public class SwaggerToDtoProcessor extends SwaggerGenericProcessor {

    private static final String SUFFIX = "DTO";

    private static final String FILE_SUFFIX = ".java";

    private static final String SOURCE_PATH = Paths.get(Constant.TEMPLATE_BASE_FOLDER,
            Constant.TYPE_SWAGGER, "dto.java.vm").toString();


    protected List<ProcessFile> convertFromTemplate(Project project) {

        List<ProcessFile> processFiles = new ArrayList<>();

        project.getSwagger().getDefinitions().keySet().forEach(className -> {

            Model model = project.getSwagger().getDefinitions().get(className);
            String clazzFullName = FormatUtils.formateClassName(className + SUFFIX);
            Map<String, Object> parameters = getParameters(project, model, clazzFullName);
            String content = templateEngine.generate(SOURCE_PATH, parameters);

            String fileName = clazzFullName + FILE_SUFFIX;
            String targetPath = Paths.get(TargetPathHelper.getTargetPath(SUFFIX, project), fileName).toString();

            processFiles.add(ProcessFile.builder().content(content)
                    .name(fileName)
                    .sourcePath(SOURCE_PATH)
                    .targetPath(targetPath)
                    .build());
        });
        return processFiles;
    }


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
            values.put(entry.getKey(), getType(entry.getValue(), SUFFIX));
        }
        return values;
    }
}
