package com.cmb.domain.engine;

import io.swagger.models.Swagger;
import lombok.*;

import java.util.List;
import java.util.Map;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class Project {

    private String name;
    private String group;
    private String framework;
    private String layerPattern;
    private String buildTool;
    private String serviceType;
    private Swagger swagger;
    private String gitUrl;
    private String decription;

    @Builder.Default
    private String javaVersion = "1.8";

    private List<DependencyManagement> dependencyManagements;

    private List<Dependency> dependencies;

    private Map<String, String> buildPropertiesGradle;

    private Map<String, String> buildPropertiesMaven;

    private JenkinsConfig jenkinsConfig;

    private Dependency mavenParent;

    private String bootVersion;

    private String productId;

    private String serviceId;
}
