package com.cmb.domain.processor;

import com.cmb.domain.project.Project;

import java.util.List;

public abstract class AbstractProcessor {

    public void process(Project project) {

        if (isValidToProceed(project)) {
            List<ProcessFile> processFiles = convertFromTemplate(project);
            generate(processFiles);
        }

    }

    protected Boolean isValidToProceed(Project project) {
        return Boolean.TRUE;
    }

    protected List<ProcessFile> convertFromTemplate(Project project) {
        return null;
    }

    protected void generate(List<ProcessFile> processFiles) {
    }


}
