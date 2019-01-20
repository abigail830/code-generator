package com.cmb.domain.processor.impl;

import com.cmb.domain.processor.ProcessFile;
import com.cmb.domain.project.Project;
import com.cmb.domain.utls.Constant;
import com.cmb.domain.utls.FormatUtils;
import com.cmb.domain.utls.SwaggerTargetPathHelper;
import com.cmb.domain.utls.SwaggerUtil;
import io.swagger.models.*;
import io.swagger.models.parameters.*;
import org.springframework.stereotype.Component;

import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SwaggerToControllerProcessor extends SwaggerGenericProcessor {

    private static final String SUFFIX = "DTO";

    private static final String FILE_SUFFIX = ".java";

    private static final String SOURCE_PATH = Paths.get(Constant.TEMPLATE_BASE_FOLDER,
            Constant.TYPE_SWAGGER, "controller.java.vm").toString();

    @Override
    protected List<ProcessFile> convertFromTemplate(Project project) {

        Map<String, Path> paths = project.getSwagger().getPaths();
        Map<String, Map<String, Path>> groupedPaths = groupUrlPaths(paths);

        List<ProcessFile> processFiles = new ArrayList<>();

        groupedPaths.keySet().stream().forEach(path -> {
            String className = path.substring(0, 1).toUpperCase() + path.substring(1) + "Controller";
            className = FormatUtils.formatClassName(className);
            Map<String, Object> parameters = getParameters(project, groupedPaths.get(path), className);

            String fileName = className + FILE_SUFFIX;
            String content = templateEngine.generate(SOURCE_PATH, parameters);
            String targetPath = Paths.get(SwaggerTargetPathHelper.getTargetPath("CONTROLLER", project), fileName).toString();

            processFiles.add(ProcessFile.builder().name(fileName)
                    .sourcePath(SOURCE_PATH)
                    .targetPath(targetPath)
                    .content(content)
                    .build());
        });

        return processFiles;

    }

    private Map<String, Map<String, Path>> groupUrlPaths(Map<String, Path> urlPaths) {

        Map<Map.Entry<String, Path>, String> mapped = urlPaths.entrySet().stream()
                .collect(Collectors.toMap(e -> e, e -> e.getKey().split("/")[1]));

        Map<String, List<Map.Entry<Map.Entry<String, Path>, String>>> groupedMap =
                mapped.entrySet().stream().collect(Collectors.groupingBy(Map.Entry::getValue));

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
                .map(entry -> entry.getValue().getOperationMap().entrySet().stream()
                        .map(op -> (createControllerMapping(entry.getKey(), op.getKey(), op.getValue())))
                        .collect(Collectors.toList()))
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
                return String.format("@RequestBody %s %s", ((RefModel) schema).getSimpleRef() + SUFFIX, parameter.getName());
            }
            if (schema instanceof ArrayModel) {
                return String.format("@RequestBody java.util.List<%s> %s",
                        SwaggerUtil.getType(((ArrayModel) schema).getType(), ((ArrayModel) schema).getItems(), SUFFIX), parameter.getName());
            }
            return "notRef";
        }
        if (parameter instanceof PathParameter) {
            return String.format("@PathVariable %s %s",
                    SwaggerUtil.getType(((PathParameter) parameter).getType(), null, SUFFIX), parameter.getName());
        }
        if (parameter instanceof QueryParameter) {
            return String.format("@RequestParam(required = %s) %s %s", parameter.getRequired(),
                    SwaggerUtil.getType(((QueryParameter) parameter).getType(), ((QueryParameter) parameter).getItems(), SUFFIX), parameter.getName());
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
