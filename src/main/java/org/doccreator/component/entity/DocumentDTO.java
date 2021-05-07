package org.doccreator.component.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "documents")
public class DocumentDTO {
    @Id
    @Column(name = "document_name")
    private String documentName;

    @Column(name = "template")
    private String template;
}
