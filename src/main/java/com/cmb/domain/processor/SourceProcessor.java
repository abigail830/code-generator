package com.cmb.domain.processor;

import com.cmb.domain.engine.Project;
import com.cmb.domain.engine.ProjectFile;
import com.cmb.domain.utls.Constant;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class SourceProcessor extends GenericProcessor {

    @Override
    protected List<ProjectFile> convertFromTemplate(Project project) {

        Path templatePath = Paths.get(Constant.TEMPLATE_BASE_FOLDER,
                project.getServiceType(), project.getLayerPattern(), project.getFramework(), Constant.TYPE_SOURCE);

        List<ProjectFile> projectFiles = convertWithTemplateParser(project, templatePath);
        return projectFiles;
    }


    @Override
    protected void generate(List<ProjectFile> projectFiles) {
        generateByWriteProjectFileContent(projectFiles);
    }


}
