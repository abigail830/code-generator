package com.cmb.domain.processor.impl;

import com.cmb.domain.engine.Project;
import com.cmb.domain.processor.GenericProcessor;
import com.cmb.domain.processor.ProcessFile;
import com.cmb.domain.utls.Constant;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Component
public class GradleBuildCommonProcessor extends GenericProcessor {


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
        String source = Paths.get(
                Constant.TEMPLATE_BASE_FOLDER, Constant.TYPE_BUILD_COMMON, Constant.TYPE_GRADLE).toString();

        String upddateProjectName = project.getName().replaceAll("-", "");

        ProcessFile processFile = ProcessFile.builder()
                .sourcePath(source)
                .targetPath(upddateProjectName)
                .build();

        return Arrays.asList(processFile);
    }

    @Override
    protected void generate(List<ProcessFile> processFiles) {
        generateByCopy(processFiles);
    }

}
