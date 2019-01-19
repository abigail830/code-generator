package com.cmb.domain.loader;


import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class FileTemplate {
    private String folder;
    private List<Template> templates;

    public enum model {
        application, ci, controller, copy, dto, buildTool, model, demo, test
    }
}

