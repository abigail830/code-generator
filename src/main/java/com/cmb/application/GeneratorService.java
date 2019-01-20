package com.cmb.application;


import com.cmb.domain.engine.Project;
import com.cmb.domain.processor.FileUtils;
import com.cmb.domain.processor.ProcessorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneratorService {

    private static Logger logger = LoggerFactory.getLogger(GeneratorService.class);

    @Autowired
    ProcessorFactory processorFactory;


    public String generate(Project project) throws Exception {
        try {
            processorFactory.getProcessorList().stream()
                    .forEach(processor -> processor.process(project));

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            String updateName = project.getName().replace("-", "");
            FileUtils.delete(updateName);
            throw new Exception("生成代码失败!", e);
        }

        return null;
    }


}
