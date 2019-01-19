package com.cmb.domain.generator.code;

import com.cmb.domain.engine.Project;
import com.cmb.domain.engine.ProjectFile;
import com.cmb.domain.loader.FileTemplate;
import com.cmb.domain.loader.Template;
import com.cmb.domain.templateengine.TemplateEngine;
import com.cmb.domain.utls.FormateUtils;
import io.swagger.models.properties.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class ProjectFileGenerator {

    @Autowired
    private TemplateEngine templateEngine;

    public List<ProjectFile> generateProjectFile(String path, String folder, Template template, Project project) {

        List<ProjectFile> projectFiles = new ArrayList<>();
        String sourcePath = getPath(path, folder, template);
        String defaultName = getDefaultName(template.getName());
        Map<String, String> clazzNameToContent = generateContents(sourcePath, project, defaultName);
        for (String fileName : clazzNameToContent.keySet()) {
            if(isJavaTemplate(template.getName())){
                fileName = FormateUtils.formateClassName(fileName);
            }
            projectFiles.add(ProjectFile.builder()
                    .name(getName(fileName, template.getName()))
                    .targetPath(getTargetPath(folder, project, template.getName()))
                    .content(clazzNameToContent.get(fileName))
                    .sourcePath(sourcePath + defaultName)
                    .build());
        }

        return projectFiles;
    }

    private String getName(String clazzName, String templateName) {
        if (templateName.endsWith(".java.vm")) {
            return clazzName + ".java";
        } else {
            return clazzName;
        }
    }

    protected String getDefaultName(String templateName) {
        if (isJavaTemplate(templateName)) {
            return firstLetterToUpperCase(templateName.split("\\.")[0]);
        } else if (templateName.endsWith(".vm")) {
            return templateName.substring(0, templateName.length() - 3);
        } else {
            return templateName;
        }
    }

    protected abstract Map<String, String> generateContents(String path, Project project, String defaultClazzName);

    protected abstract boolean canSupport(FileTemplate.model model);

    protected String getPath(String path, String folder, Template template) {
        return path + "/" + template.getName();
    }

    protected TemplateEngine getTemplateEngine() {
        return this.templateEngine;
    }

    protected String getTargetPath(String folder, Project project, String templateName) {
        if (isJavaTemplate(templateName)) {
            return String.format("src/main/java/com/%s/%s/" + folder, project.getGroup(), project.getName());
        }
        return folder;
    }

    protected String getType(Property value, String suffix) {
        if (value instanceof LongProperty) {
            return "Long";
        }
        if (value instanceof IntegerProperty) {
            return "Integer";
        }
        if (value instanceof StringProperty) {
            return "String";
        }
        if (value instanceof BooleanProperty) {
            return "Boolean";
        }
        if (value instanceof DateTimeProperty) {
            return "java.time.LocalDateTime";
        }
        if (value instanceof RefProperty) {
            return ((RefProperty) value).getSimpleRef() + suffix;
        }
        if (value instanceof ArrayProperty) {
            return String.format("java.util.List<%s>", getType(((ArrayProperty) value).getItems(), suffix));
        }
        return null;
    }


    protected String getType(String value, Property property, String suffix) {
        if (value.equals("integer")) {
            return "Integer";
        }
        if (value.equals("string")) {
            return "String";
        }
        if (value.equals("array")) {
            return String.format("java.util.List<%s>", getType(property, suffix));
        }

        return null;
    }

    private String firstLetterToUpperCase(String templateName) {
        return templateName.substring(0, 1).toUpperCase() + templateName.substring(1);
    }

    protected boolean isJavaTemplate(String templateName) {
        return templateName.endsWith(".java.vm");
    }

}
