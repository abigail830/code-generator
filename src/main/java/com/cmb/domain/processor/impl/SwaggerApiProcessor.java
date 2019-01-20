package com.cmb.domain.processor.impl;

import com.cmb.domain.processor.GenericProcessor;
import com.cmb.domain.processor.ProcessFile;
import com.cmb.domain.project.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SwaggerApiProcessor extends GenericProcessor {

    private static Logger logger = LoggerFactory.getLogger(GenericProcessor.class);

    @Override
    protected Boolean isValidToProceed(Project project) {
        if (project.getSwagger() != null) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    protected List<ProcessFile> convertFromTemplate(Project project) {

        return null;
    }
}
