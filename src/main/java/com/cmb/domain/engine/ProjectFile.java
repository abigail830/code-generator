package com.cmb.domain.engine;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@ToString
public class ProjectFile {

    private String name;
    private String targetPath;
    private String content;
    private String sourcePath;
}
