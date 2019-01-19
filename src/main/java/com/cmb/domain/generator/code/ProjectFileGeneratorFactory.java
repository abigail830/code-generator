package com.cmb.domain.generator.code;

import com.cmb.domain.loader.FileTemplate;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProjectFileGeneratorFactory implements InitializingBean {

    @Autowired
    private BuildToolProjectGenerator buildToolProjectGenerator;

    @Autowired
    private MainProjectGenerator mainProjectGenerator;

    @Autowired
    private ModelProjectGenerator modelProjectGenerator;

    @Autowired
    private ControllerProjectFileGenerator controllerProjectFileGenerator;

    @Autowired
    private JenkinsfileProjectGenerator jenkinsfileProjectGenerator;

    @Autowired
    private DemoProjectFileGenerator demoProjectFileGenerator;

    @Autowired
    private CopyProjectFileGenerator copyProjectFileGenerator;

    @Autowired
    private TestProjectFileGenerator testProjectFileGenerator;

    private static List<ProjectFileGenerator> generators;

    @Override
    public void afterPropertiesSet() {
        generators = Lists.newArrayList(buildToolProjectGenerator,
                mainProjectGenerator, modelProjectGenerator, copyProjectFileGenerator,
                controllerProjectFileGenerator, jenkinsfileProjectGenerator,
                demoProjectFileGenerator, testProjectFileGenerator);
    }

    public ProjectFileGenerator getProjectFileGenerator(String type) {
        if (Strings.isNullOrEmpty(type)) {
            return demoProjectFileGenerator;
        }
        for (ProjectFileGenerator generator : generators) {
            if (generator.canSupport(FileTemplate.model.valueOf(type))) {
                return generator;
            }
        }
        return demoProjectFileGenerator;
    }
}
