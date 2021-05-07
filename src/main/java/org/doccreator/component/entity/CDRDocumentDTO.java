package org.doccreator.component.entity;

import lombok.Data;
import org.doccreator.component.entity.embedded.CDRDocumentId;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "cdr_documents")
public class CDRDocumentDTO {
    @EmbeddedId
    private CDRDocumentId id;
}
