package com.cmb.domain.processor.impl;

import com.cmb.domain.engine.Project;
import com.cmb.domain.engine.ProjectFile;
import com.cmb.domain.processor.GenericProcessor;
import com.cmb.domain.utls.Constant;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class MavenConfigProcessor extends GenericProcessor {

    @Override
    protected Boolean isValidToProceed(Project project) {
        if (Constant.TYPE_MAVEN.equals(project.getBuildTool())) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    protected List<ProjectFile> convertFromTemplate(Project project) {

        Path templatePath = Paths.get(Constant.TEMPLATE_BASE_FOLDER,
                project.getServiceType(), project.getLayerPattern(), project.getFramework(), Constant.TYPE_MAVEN);

        List<ProjectFile> projectFiles = convertWithTemplateParser(project, templatePath);
        return projectFiles;
    }


    @Override
    protected void generate(List<ProjectFile> projectFiles) {
        generateByWriteProjectFileContent(projectFiles);
    }
}
