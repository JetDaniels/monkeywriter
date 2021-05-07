package org.doccreator.component.entity;

import lombok.Data;
import org.doccreator.component.entity.embedded.PDRDocumentId;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "pdr_documents")
public class PDRDocumentDTO {
    @EmbeddedId
    private PDRDocumentId id;

    @Column(name = "document_source")
    private String documentSource;
}
