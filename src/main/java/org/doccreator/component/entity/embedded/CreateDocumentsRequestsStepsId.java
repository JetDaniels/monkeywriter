package org.doccreator.component.entity.embedded;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class CreateDocumentsRequestsStepsId implements Serializable {
    private static final long serialVersionUID = 1L;

    @Column(name = "cdr_id")
    private Integer cdrId;

    @Column(name = "step")
    private Integer step;
}
