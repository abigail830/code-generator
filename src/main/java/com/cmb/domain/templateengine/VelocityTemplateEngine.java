package com.cmb.domain.templateengine;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Map;

public class VelocityTemplateEngine implements TemplateEngine {

    private VelocityEngine velocityEngine;

    public VelocityTemplateEngine(VelocityEngine velocityEngine) {

        this.velocityEngine = velocityEngine;
    }


    @Override
    public String generate(String templateFilePath, Map<String, Object> paramaters) {

        Template t = velocityEngine.getTemplate(templateFilePath);

        VelocityContext ctx = new VelocityContext(paramaters);

        StringWriter sw = new StringWriter();

        t.merge(ctx, sw);

        return sw.toString();
    }
}
