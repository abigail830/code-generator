package com.cmb.domain.processor;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@ToString
public class ProcessFile {

    private String name;
    private String targetPath;
    private String content;
    private String sourcePath;
}
