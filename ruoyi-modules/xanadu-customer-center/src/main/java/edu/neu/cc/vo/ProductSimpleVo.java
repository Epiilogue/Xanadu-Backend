package edu.neu.cc.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public     class ProductSimpleVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;

    private String name;

    private String firstName;
}
