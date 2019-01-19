package com.cmb.apis.dto;

import com.cmb.domain.engine.Dependency;
import com.cmb.domain.engine.DependencyManagement;
import com.cmb.domain.engine.JenkinsConfig;
import io.swagger.annotations.ApiParam;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Validated
public class ProjectDTO {

  @ApiParam(required = true, name = "name", value = "项目名称")
  @NotBlank(message = "项目名称不能为空")
  private String name;

  @ApiParam(required = true, name = "group", value = "项目分组")
  @NotBlank(message = "项目分组不能为空")
  private String group;

  @ApiParam(required = true, name = "framework", value = "项目框架")
  @NotBlank(message = "项目框架不能为空")
  private String framework;

  @ApiParam(required = true, name = "layerPattern", value = "layerPattern")
  @NotBlank(message = "layerPattern不能为空")
  private String layerPattern;

  @ApiParam(required = true, name = "buildTool", value = "构建工具")
  @NotBlank(message = "构建工具不能为空")
  private String buildTool;

  private String serviceType;

  @ApiParam(required = true, name = "gitUrl", value = "git地址")
  @NotBlank(message = "git地址不能为空")
  private String gitUrl;

  @ApiParam(required = true, name = "swaggerUrl", value = "swagger文件地址")
  private String swaggerUrl;

  @ApiParam(required = true, name = "decription", value = "描述")
  private String decription;

  @ApiParam(required = true, name = "bootVersion", value = "springBoot版本")
  @NotBlank(message = "springBoot版本不能为空")
  private String bootVersion;

  private JenkinsConfig jenkinsConfig;

  private List<DependencyManagement> dependencyManagements;

  private List<Dependency> dependencies;

  private Map<String,String> buildPropertiesGradle;

  private Map<String,String> buildPropertiesMaven;

  private Dependency mavenParent;

  private String productId;

  private String serviceId;
}
