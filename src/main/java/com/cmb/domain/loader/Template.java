package com.cmb.domain.loader;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class Template {
    private String name;
    private String path;
    private String type;
}
