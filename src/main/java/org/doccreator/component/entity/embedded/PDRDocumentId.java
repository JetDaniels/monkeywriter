package org.doccreator.component.entity.embedded;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class PDRDocumentId implements Serializable {
    private static final long serialVersionUID = 3L;

    @Column(name = "pdr_id")
    private Integer pdrId;

    @Column(name = "document_name")
    private String documentName;
}
