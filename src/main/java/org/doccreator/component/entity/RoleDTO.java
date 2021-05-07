package org.doccreator.component.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "roles")
public class RoleDTO {
    @Id
    @Column(name = "role_name")
    private String roleName;
}
