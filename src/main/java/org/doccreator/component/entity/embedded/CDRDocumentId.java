package org.doccreator.component.entity.embedded;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class CDRDocumentId implements Serializable {
    private static final long serialVersionUID = 2L;

    @Column(name = "cdr_id")
    private Integer cdrId;

    @Column(name = "document_name")
    private String documentName;
}
