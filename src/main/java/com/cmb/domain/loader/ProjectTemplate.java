package com.cmb.domain.loader;

import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@ToString
public class ProjectTemplate {
  private String templatePath;
  private List<FileTemplate> fileTemplateList;
}
