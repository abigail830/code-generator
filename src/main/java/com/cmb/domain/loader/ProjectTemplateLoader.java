package com.cmb.domain.loader;

import com.cmb.domain.engine.Project;
import com.cmb.domain.utls.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class ProjectTemplateLoader {

    private static final String BASE_PATH = "./template/";
    private static final String FILE_NAME = "manifests.yml";

    private YamlLoader<ProjectTemplate> yamlLoader;

    private static Logger logger = LoggerFactory.getLogger(ProjectTemplateLoader.class);

    @Autowired
    public ProjectTemplateLoader(YamlLoader<ProjectTemplate> yamlLoader) {
        this.yamlLoader = yamlLoader;
    }


    public List<ProjectTemplate> load(Project project) {

        ProjectTemplate sourceCodeTemplate =
                load(project.getServiceType(), project.getLayerPattern(), project.getFramework());

        ProjectTemplate buildToolConfigTemplate =
                load(project.getServiceType(), project.getLayerPattern(), project.getFramework(), project.getBuildTool());

        ProjectTemplate commonBuildToolTemplate =
                load(project.getBuildTool());

        List<ProjectTemplate> fullList = Stream.of(sourceCodeTemplate, buildToolConfigTemplate, commonBuildToolTemplate)
                .filter(projectTemplate -> projectTemplate != null)
                .collect(Collectors.toList());

        return fullList;
    }


    protected ProjectTemplate load(String... subPaths) {

        String combinePath = Arrays.stream(subPaths).collect(Collectors.joining("/"));

        Path fullPath = Paths.get(BASE_PATH, combinePath);

        try {
            final Optional<Path> manifests = FileUtils.findFile(fullPath, FILE_NAME);

            if (manifests.isPresent()) {
                final ProjectTemplate projectTemplate = yamlLoader.parse(manifests);
                projectTemplate.setTemplatePath(fullPath.toString());
                return projectTemplate;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }

        return null;
    }


}
