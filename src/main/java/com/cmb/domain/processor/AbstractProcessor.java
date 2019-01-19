package com.cmb.domain.processor;

import com.cmb.domain.engine.Project;
import com.cmb.domain.engine.ProjectFile;

import java.util.List;

public abstract class AbstractProcessor {

    public void process(Project project) {

        if (isValidToProceed(project)) {
            List<ProjectFile> projectFiles = convertFromTemplate(project);
            generate(projectFiles);
        }

    }

    protected Boolean isValidToProceed(Project project) {
        return Boolean.TRUE;
    }

    protected List<ProjectFile> convertFromTemplate(Project project) {
        return null;
    }

    protected void generate(List<ProjectFile> projectFiles) {
    }


}
