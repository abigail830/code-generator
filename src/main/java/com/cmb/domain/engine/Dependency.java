package com.cmb.domain.engine;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
