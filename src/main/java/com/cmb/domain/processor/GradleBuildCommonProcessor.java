package com.cmb.domain.processor;

import com.cmb.domain.engine.Project;
import com.cmb.domain.engine.ProjectFile;
import com.cmb.domain.utls.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Component
public class GradleBuildCommonProcessor extends GenericProcessor {

    private static Logger logger = LoggerFactory.getLogger(GradleBuildCommonProcessor.class);


    @Override
    protected Boolean isValidToProceed(Project project) {
        if (Constant.TYPE_GRADLE.equals(project.getBuildTool())) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    protected List<ProjectFile> convertFromTemplate(Project project) {
        String source = Paths.get(
                Constant.TEMPLATE_BASE_FOLDER, Constant.TYPE_BUILD_COMMON, Constant.TYPE_GRADLE).toString();

        ProjectFile projectFile = ProjectFile.builder()
                .sourcePath(source)
                .targetPath(project.getName())
                .build();

        return Arrays.asList(projectFile);
    }

}
