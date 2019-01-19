package com.cmb.application;


import com.cmb.domain.engine.Project;
import com.cmb.domain.processor.FileUtils;
import com.cmb.domain.processor.GradleBuildCommonProcessor;
import com.cmb.domain.processor.MavenBuildCommonProcessor;
import com.cmb.domain.processor.SourceProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneratorService {

    private static Logger logger = LoggerFactory.getLogger(GeneratorService.class);

    @Autowired
    GradleBuildCommonProcessor gradleBuildCommonProcessor;

    @Autowired
    MavenBuildCommonProcessor mavenBuildCommonProcessor;

    @Autowired
    SourceProcessor sourceProcessor;


    public String generate(Project project) throws Exception {
        try {
            sourceProcessor.process(project);
            gradleBuildCommonProcessor.process(project);
            mavenBuildCommonProcessor.process(project);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FileUtils.delete(project.getName());
            throw new Exception("生成代码失败!", e);
        }

        return null;
    }


}
