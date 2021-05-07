package org.doccreator.component.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "outside_systems")
public class OutsideSystemDTO {
    @Id
    @Column(name = "system_name")
    private String systemName;

    @Column(name = "link")
    private String link;

    @Column(name = "system_type")
    private String systemType;
}
