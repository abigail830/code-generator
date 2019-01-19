package com.cmb.domain.templateengine;

import java.util.Map;

public interface TemplateEngine {
  String generate(String templateFilePath, Map<String, Object> paramaters);
}
