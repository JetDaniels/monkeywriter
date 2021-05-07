package org.doccreator.component.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "connections")
public class ConnectionDTO {
    @Id
    @Column(name = "connection_name")
    private String connectionName;

    @Column(name = "connection_type")
    private String type;

    @Column(name = "connection_url")
    private String url;
}
