package com.cmb.domain.processor.impl;

import com.cmb.domain.processor.GenericProcessor;
import com.cmb.domain.processor.ProcessFile;
import com.cmb.domain.project.Project;

import java.util.List;

public class SwaggerGenericProcessor extends GenericProcessor {

    @Override
    protected Boolean isValidToProceed(Project project) {
        if (project.getSwagger() != null) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    protected void generate(List<ProcessFile> processFiles) {

        generateByWriteProjectFileContent(processFiles);
    }


}
