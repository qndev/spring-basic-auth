package io.github.qndev.springbasicauth.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserRoleModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String userId;

    private String roleId;

    private String roleName;

}
