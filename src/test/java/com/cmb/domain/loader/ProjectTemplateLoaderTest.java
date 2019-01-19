package com.cmb.domain.loader;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ProjectTemplateLoaderTest {

    ProjectTemplateLoader loader;

    YamlLoader<ProjectTemplate> yamlLoader = new YamlLoader<>(ProjectTemplate.class);

    @Before
    public void setUp() throws Exception {
        loader = new ProjectTemplateLoader(yamlLoader);
    }

    @Test
    public void loadMavenManifests() {
        ProjectTemplate maven = loader.load("maven");
        assertEquals("./template/maven", maven.getTemplatePath());
    }

    @Test
    public void loadZA23Manifests() {
        ProjectTemplate za23 = loader.load("bizservice", "microservice", "za23");
        assertEquals("./template/bizservice/microservice/za23", za23.getTemplatePath());
    }

    @Test
    public void loadNotExistManifests() {
        ProjectTemplate projectTemplate = loader.load("notexist");
        assertNull(projectTemplate);
    }
}