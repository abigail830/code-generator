package com.cmb.application;


import com.cmb.domain.engine.Project;
import com.cmb.domain.engine.ProjectBuilder;
import com.cmb.domain.engine.ProjectFile;
import com.cmb.domain.generator.old.ProjectFileGenerator;
import com.cmb.domain.generator.old.ProjectFileGeneratorFactory;
import com.cmb.domain.loader.ProjectTemplate;
import com.cmb.domain.loader.ProjectTemplateLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GeneratorService {

    private static Logger logger = LoggerFactory.getLogger(GeneratorService.class);

    @Autowired
    private ProjectBuilder projectBuilder;

    @Autowired
    private ProjectFileGeneratorFactory projectFileGeneratorFactory;

    @Autowired
    private ProjectTemplateLoader projectTemplateLoader;

//    @Autowired
//    private GitUtils gitUtils;
//
//    @Autowired
//    private JenkinsUtils jenkinsUtils;

    public String generate(Project project) throws Exception {
        String gradleFile = null;
        try {

            List<ProjectTemplate> fullListTemplate = projectTemplateLoader.load(project);

            List<ProjectFile> projectFiles = convertTemplateToProjectFile(fullListTemplate, project);
            project.setProjectFileList(projectFiles);

            gradleFile = projectBuilder.build(project);

            //TODO: To be confirm if below part should be remove from this service
//            gitUtils.pushProjectToGitRepo(project.getName());
//
//            jenkinsUtils.publishJenkinsPipeline(project.getName(),
//                    "./template/jenkins-config.xml.vm",
//                    project.getJenkinsConfig());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new Exception("生成代码失败!", e);
        } finally {
//            org.apache.commons.io.FileUtils.deleteDirectory(new File(gradleFile));
        }

        return gradleFile;
    }

    private List<ProjectFile> convertTemplateToProjectFile(List<ProjectTemplate> projectTemplates, Project project) {

        List<ProjectFile> projectFiles = new ArrayList<>();

        projectTemplates.stream().forEach(
                projectTemplate -> projectTemplate.getFileTemplateList().forEach(fileTemplate -> {
                    fileTemplate.getTemplates().stream().forEach(template -> {

                        ProjectFileGenerator projectFileGenerator =
                                projectFileGeneratorFactory.getProjectFileGenerator(template.getType());

                        List<ProjectFile> result = projectFileGenerator.generateProjectFile(
                                projectTemplate.getTemplatePath(), fileTemplate.getFolder(), template, project);

                        projectFiles.addAll(result);

                    });
                })
        );
        return projectFiles;
    }

}
