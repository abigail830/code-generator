package com.cmb.domain.project;


import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class Dependency{

    private String option;

    private String group;

    private String name;

    private String version;

    private String type;

    private String scope;

    private String diy;
}
