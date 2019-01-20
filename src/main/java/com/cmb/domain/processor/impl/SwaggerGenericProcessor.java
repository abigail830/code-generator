package com.cmb.domain.processor.impl;

import com.cmb.domain.processor.GenericProcessor;
import com.cmb.domain.processor.ProcessFile;
import com.cmb.domain.project.Project;
import io.swagger.models.properties.*;

import java.util.List;

public class SwaggerGenericProcessor extends GenericProcessor {

    @Override
    protected Boolean isValidToProceed(Project project) {
        if (project.getSwagger() != null) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    protected static String getType(Property value, String suffix) {

        if (value instanceof LongProperty) {
            return "Long";
        }
        if (value instanceof IntegerProperty) {
            return "Integer";
        }
        if (value instanceof StringProperty) {
            return "String";
        }
        if (value instanceof BooleanProperty) {
            return "Boolean";
        }
        if (value instanceof DateTimeProperty) {
            return "java.time.LocalDateTime";
        }
        if (value instanceof RefProperty) {
            return ((RefProperty) value).getSimpleRef() + suffix;
        }
        if (value instanceof ArrayProperty) {
            return String.format("java.util.List<%s>", getType(((ArrayProperty) value).getItems(), suffix));
        }
        return null;
    }

    @Override
    protected void generate(List<ProcessFile> processFiles) {
        generateByWriteProjectFileContent(processFiles);
    }

}
