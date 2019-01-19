package com.cmb.domain.generator.old;

import com.cmb.domain.engine.Project;
import com.cmb.domain.loader.FileTemplate;
import com.cmb.domain.loader.Template;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CopyProjectFileGenerator extends ProjectFileGenerator {

    @Override
    protected Map<String, String> generateContents(String path, Project project, String defaultClazzName) {
        Map<String, String> clazzNameToContent = new HashMap<>();
        clazzNameToContent.put(defaultClazzName, null);
        return clazzNameToContent;
    }

    @Override
    protected boolean canSupport(FileTemplate.model model) {
        return model == FileTemplate.model.copy;
    }

    @Override
    protected String getPath(String path, String folder, Template template) {
        return "." + template.getPath() + folder;
    }

}
