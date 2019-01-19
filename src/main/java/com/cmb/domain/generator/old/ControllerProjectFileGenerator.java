package com.cmb.domain.generator.old;

import com.cmb.domain.engine.Project;
import com.cmb.domain.loader.FileTemplate;
import com.cmb.domain.utls.FormateUtils;
import io.swagger.models.*;
import io.swagger.models.parameters.*;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ControllerProjectFileGenerator extends ProjectFileGenerator {

    private String suffix = "Dto";

    @Override
    public Map<String, String> generateContents(String path, Project project, String defaultClazzName) {
        Map<String, String> contents = new HashMap<>();
        Map<String, Path> urlPaths = project.getSwagger().getPaths();
        Map<String, Map<String, Path>> groupedUrlPaths = groupUrlPaths(urlPaths);

        for (String baseUrl : groupedUrlPaths.keySet()) {
            String className = baseUrl.substring(0, 1).toUpperCase() + baseUrl.substring(1) + "Controller";
            className = FormateUtils.formateClassName(className);
            Map<String, Object> parameters = getParameters(project, groupedUrlPaths.get(baseUrl), className);
            contents.put(className, super.getTemplateEngine().generate(path, parameters));
        }
        return contents;
    }

    @Override
    protected boolean canSupport(FileTemplate.model model) {
        return model == FileTemplate.model.controller;
    }

    private Map<String, Map<String, Path>> groupUrlPaths(Map<String, Path> urlPaths) {
        Map<Map.Entry<String, Path>, String> mapped = urlPaths.entrySet().stream()
                .collect(Collectors.toMap(e -> e, e -> e.getKey().split("/")[1]));
        Map<String, List<Map.Entry<Map.Entry<String, Path>, String>>> groupedMap = mapped.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue));

        Map<String, Map<String, Path>> results = new HashMap<>();
        for (String key : groupedMap.keySet()) {
            List<Map.Entry<String, Path>> groupedList = groupedMap.get(key).stream().map(Map.Entry::getKey).collect(Collectors.toList());
            Map<String, Path> collect3 = groupedList.stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            results.put(key, collect3);
        }
        return results;
    }

    private Map<String, Object> getParameters(Project project, Map<String, Path> urlPaths, String clazzName) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("mappings", extract(urlPaths));
        parameters.put("classname", clazzName);
        parameters.put("group", project.getGroup());
        parameters.put("name", project.getName());

        return parameters;
    }

    private List<ControllerMapping> extract(Map<String, Path> urlPaths) {

        return urlPaths.entrySet().stream()
                .map(entry -> entry.getValue().getOperationMap().entrySet().stream().map(op -> (createControllerMapping(entry.getKey(), op.getKey(), op.getValue()))).collect(Collectors.toList()))
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(ControllerMapping::getPath))
                .collect(Collectors.toList());
    }

    private ControllerMapping createControllerMapping(String key, HttpMethod method, Operation operation) {
        String params = operation.getParameters().stream().map(this::generateParam).filter(Objects::nonNull).distinct().collect(Collectors.joining(", "));


        return ControllerMapping.builder()
                .method(getHttpMethod(method))
                .path(key)
                .description(operation.getSummary())
                .functionName(operation.getOperationId())
                .responseType("ResponseEntity")
                .params(params)
                .build();
    }

    private String generateParam(Parameter parameter) {
        if (parameter instanceof BodyParameter) {
            Model schema = ((BodyParameter) parameter).getSchema();
            if (schema instanceof RefModel) {
                return String.format("@RequestBody %s %s", ((RefModel) schema).getSimpleRef() + suffix, parameter.getName());
            }
            if (schema instanceof ArrayModel) {
                return String.format("@RequestBody java.util.List<%s> %s", getType(((ArrayModel) schema).getType(), ((ArrayModel) schema).getItems(), suffix), parameter.getName());
            }
            return "notRef";
        }
        if (parameter instanceof PathParameter) {
            return String.format("@PathVariable %s %s", getType(((PathParameter) parameter).getType(), null, suffix), parameter.getName());
        }
        if (parameter instanceof QueryParameter) {
            return String.format("@RequestParam(required = %s) %s %s", parameter.getRequired(), getType(((QueryParameter) parameter).getType(), ((QueryParameter) parameter).getItems(), suffix), parameter.getName());
        }
        if (parameter instanceof FormParameter) {
            return "MultiValueMap paramMap";
        }
        return null;
    }

    private String getHttpMethod(HttpMethod method) {
        String name = method.name();
        return name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
    }
}
