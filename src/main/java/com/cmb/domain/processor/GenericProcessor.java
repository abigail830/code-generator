package com.cmb.domain.processor;

import com.cmb.domain.engine.ProjectFile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class GenericProcessor extends AbstractProcessor {

    private static Logger logger = LoggerFactory.getLogger(GenericProcessor.class);

    @Override
    protected void generate(List<ProjectFile> projectFiles) {

        projectFiles.stream().forEach(projectFile -> {
            try {

                String fullSourcePath = projectFile.getSourcePath();
                String fullTargetPath = projectFile.getTargetPath();

                if (!StringUtils.isBlank(projectFile.getName())) {
                    fullSourcePath = Paths.get(fullSourcePath, projectFile.getName()).toString();
                    fullTargetPath = Paths.get(fullTargetPath, projectFile.getName()).toString();
                }

                FileUtils.copyFileOrFolder(fullSourcePath, fullTargetPath);

            } catch (IOException e) {
                logger.warn("IOException happen when try to generate ProjectFile into Files.");
                e.printStackTrace();
            }
        });


    }
}
