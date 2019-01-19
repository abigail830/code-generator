package com.cmb.domain.generator.old;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class ControllerMapping {
    private String method;
    private String path;
    private String description;
    private String functionName;
    private String responseType;
    private String params;
}
