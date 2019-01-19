package com.cmb.domain.engine;


import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class DependencyManagement {

    private String group;

    private String name;

    private String version;

    private String type;

    private String diy;

}
