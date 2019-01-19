package com.cmb.domain.engine;

import com.cmb.domain.utls.FileUtils;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProjectBuilder {

    public String build(Project project) {

        String projectPath = createProjectDirectory(project);

        createProjectFiles(projectPath, project);

        return projectPath;
    }

    private void createProjectFiles(String projectPath, Project project) {
        project.getProjectFileList().stream()
                .map(f -> createProjectFile(projectPath, f))
                .collect(Collectors.toList());

    }

    private String createProjectFile(String projectPath, ProjectFile f) {
        final String path = projectPath + "/" + f.getTargetPath().replaceAll("-", "");
        String filePathName = path + "/" + f.getName();
        FileUtils.createDirectory(path);
        if (f.getContent() == null) {
            FileUtils.copyFile(f.getSourcePath(), filePathName);
        } else {
            FileUtils.writeTo(filePathName, f.getContent());
        }
        return filePathName;
    }

    private String createProjectDirectory(Project project) {
//    String prefix = dateTimeFormat.format(new Date()).toString();

        final String pathname = "./" + project.getName();

        FileUtils.createDirectory(pathname);

        return pathname;
    }

}
