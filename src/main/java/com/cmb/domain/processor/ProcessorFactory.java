package com.cmb.domain.processor;

import com.cmb.domain.processor.impl.*;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProcessorFactory implements InitializingBean {

    private static List<GenericProcessor> processorList;
    @Autowired
    GradleBuildCommonProcessor gradleBuildCommonProcessor;
    @Autowired
    MavenBuildCommonProcessor mavenBuildCommonProcessor;
    @Autowired
    SourceProcessor sourceProcessor;
    @Autowired
    MavenConfigProcessor mavenConfigProcessor;
    @Autowired
    GradleConfigProcessor gradleConfigProcessor;
    @Autowired
    SwaggerToDtoProcessor swaggerToDtoProcessor;
    @Autowired
    SwaggerToControllerProcessor swaggerToControllerProcessor;

    @Override
    public void afterPropertiesSet() throws Exception {
        processorList = Lists.newArrayList(
                gradleBuildCommonProcessor,
                mavenBuildCommonProcessor,
                sourceProcessor,
                mavenConfigProcessor,
                gradleConfigProcessor,
                swaggerToDtoProcessor,
                swaggerToControllerProcessor);
    }

    public List<GenericProcessor> getProcessorList() {
        return processorList;
    }
}
