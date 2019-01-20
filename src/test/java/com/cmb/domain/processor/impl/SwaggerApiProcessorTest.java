package com.cmb.domain.processor.impl;

import com.cmb.domain.project.Project;
import io.swagger.models.Swagger;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SwaggerApiProcessorTest {

    SwaggerApiProcessor swaggerApiProcessor;

    Project project;

    Swagger swagger;

    @Before
    public void setUp() throws Exception {
        swaggerApiProcessor = new SwaggerApiProcessor();
        project = mock(Project.class);
        swagger = mock(Swagger.class);
    }

    @Test
    public void testIsValidToProceedWhenSwaggerNull() {
        when(project.getSwagger()).thenReturn(null);
        assertFalse(swaggerApiProcessor.isValidToProceed(project));
    }

    @Test
    public void testIsValidToProceedWhenSwaggerNotNull() {
        when(project.getSwagger()).thenReturn(swagger);
        assertTrue(swaggerApiProcessor.isValidToProceed(project));
    }
}