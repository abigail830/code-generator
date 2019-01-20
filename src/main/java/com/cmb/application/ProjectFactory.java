package com.cmb.application;

import com.cmb.domain.project.Project;
import com.cmb.domain.utls.SwaggerDownloadUtils;
import io.swagger.models.Swagger;
import io.swagger.parser.SwaggerParser;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProjectFactory {
    private SwaggerParser swaggerParser = new SwaggerParser();

    @Autowired
    private SwaggerDownloadUtils swaggerDownloadUtils;

    public Project setSwaggerIfURLProvided(Project project, String swaggerUrl) {
        Swagger swagger;

        if (!Strings.isBlank(swaggerUrl)) {
            //下载swagger文件
            byte[] swaggerData;
            //数字类型,表示附件id，否则为附件uri
            if(swaggerUrl.matches("^[0-9]*$")){
                swaggerData = swaggerDownloadUtils.downLoadSwagger(Long.parseLong(swaggerUrl));
            }else{
                swaggerData = swaggerDownloadUtils.downLoadSwagger(swaggerUrl);
            }

            swagger = swaggerParser.readWithInfo(new String(swaggerData)).getSwagger();

            project.setSwagger(swagger);
        }

        return project;
    }

}
