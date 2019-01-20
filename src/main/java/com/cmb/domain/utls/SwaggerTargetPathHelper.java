package com.cmb.domain.utls;

import com.cmb.domain.project.Project;

import java.nio.file.Paths;

public class SwaggerTargetPathHelper {

    public static String getTargetPath(String type, Project project) {

        if (project.getServiceType().equals("bizservice") &&
                project.getLayerPattern().equals("microservice") &&
                project.getFramework().equals("za23") &&
                type.equals("DTO")) {

            String updatedProjectName = project.getName().replaceAll("-", "");

            return Paths.get(updatedProjectName, "src", "main", "java", "com",
                    project.getGroup(), updatedProjectName, "api", "dto").toString();
        }

        if (project.getServiceType().equals("bizservice") &&
                project.getLayerPattern().equals("microservice") &&
                project.getFramework().equals("za23") &&
                type.equals("CONTROLLER")) {

            String updatedProjectName = project.getName().replaceAll("-", "");

            return Paths.get(updatedProjectName, "src", "main", "java", "com",
                    project.getGroup(), updatedProjectName, "api").toString();
        }


        return "";
    }
}
