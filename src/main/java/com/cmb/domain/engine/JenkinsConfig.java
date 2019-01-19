package com.cmb.domain.engine;


import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class JenkinsConfig {

    private String description;

    private String gitUrl;

    private String gitBranch;

    private String scriptPath;
}
