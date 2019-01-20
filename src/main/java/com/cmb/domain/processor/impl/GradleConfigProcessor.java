package com.cmb.domain.processor.impl;

import com.cmb.domain.processor.GenericProcessor;
import com.cmb.domain.processor.ProcessFile;
import com.cmb.domain.project.Project;
import com.cmb.domain.utls.Constant;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Component
public class GradleConfigProcessor extends GenericProcessor {

    @Override
    protected Boolean isValidToProceed(Project project) {
        if (Constant.TYPE_GRADLE.equals(project.getBuildTool())) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    protected List<ProcessFile> convertFromTemplate(Project project) {

        Path templatePath = Paths.get(Constant.TEMPLATE_BASE_FOLDER,
                project.getServiceType(), project.getLayerPattern(), project.getFramework(), Constant.TYPE_GRADLE);

        List<ProcessFile> processFiles = convertWithTemplateParser(project, templatePath);
        return processFiles;
    }


    @Override
    protected void generate(List<ProcessFile> processFiles) {
        generateByWriteProjectFileContent(processFiles);
    }
}
