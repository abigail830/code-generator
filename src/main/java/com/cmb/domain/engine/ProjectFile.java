package com.cmb.domain.engine;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class ProjectFile {

    private String name;
    private String targetPath;
    private String content;
    private String sourcePath;
}
