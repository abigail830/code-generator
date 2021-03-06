package com.cmb.domain.processor;

import com.cmb.domain.project.Project;
import com.cmb.domain.templateengine.TemplateEngine;
import com.cmb.domain.utls.Constant;
import com.cmb.domain.utls.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GenericProcessor extends AbstractProcessor {

    private static Logger logger = LoggerFactory.getLogger(GenericProcessor.class);

    @Autowired
    public TemplateEngine templateEngine;


    protected List<ProcessFile> convertWithTemplateParser(Project project, Path sourcePath) {
        List<ProcessFile> processFiles = new ArrayList<>();
        try {
            processFiles = Files.walk(sourcePath)
                    .filter(path -> Files.isRegularFile(path))
                    .map(filePath -> ProcessFile.builder()
                            .name(filePath.getFileName().toString())
                            .content(templateEngine.generate(filePath.toString(), getParameters(project)))
                            .sourcePath(filePath.toString())
                            .targetPath(generateTargetPath(sourcePath.toString(), filePath.toString(), project))
                            .build())
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
        }
        return processFiles;
    }

    protected String generateTargetPath(String basePath, String sourcePath, Project project) {

        String targetPath = sourcePath;

        String updateProjectGroup = project.getGroup().replaceAll("-", "");
        targetPath = targetPath.replace(Constant.DEFAULT_GROUP, updateProjectGroup);

        String upddateProjectName = project.getName().replaceAll("-", "");
        targetPath = targetPath.replace(basePath, upddateProjectName);
        targetPath = targetPath.replace(Constant.DEFAULT_NAME, upddateProjectName);

        return targetPath;

    }

    protected void generateByCopy(List<ProcessFile> processFiles) {
        processFiles.stream().forEach(projectFile -> {

            try {
                FileUtils.copyFileOrFolder(projectFile.getSourcePath(), projectFile.getTargetPath());
            } catch (IOException e) {
                logger.warn("IOException happen when try to generate ProcessFile into Files.");
                e.printStackTrace();
            }
        });
    }

    protected void generateByWriteProjectFileContent(List<ProcessFile> processFiles) {
        processFiles.stream().forEach(projectFile -> {

            FileUtils.writeFileOrFolder(projectFile.getSourcePath(),
                    projectFile.getTargetPath(), projectFile.getContent());

        });
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
        paramaters.put("mavenParent", project.getMavenParent());
        paramaters.put("bootVersion", project.getBootVersion());
        paramaters.put("productId", project.getProductId());
        paramaters.put("serviceId", project.getServiceId());
        return paramaters;
    }
}
