package org.doccreator.component.entity;

import lombok.Data;
import org.doccreator.component.entity.embedded.CreateDocumentsRequestsStepsId;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="create_documents_requests_steps")
public class CreateDocumentsRequestsStepsDTO {
    @EmbeddedId
    private CreateDocumentsRequestsStepsId id;

    @Column(name = "stage")
    private String stage;

    @Column(name = "message")
    private String message;

    @Column(name = "time_begin")
    private Timestamp timeBegin;

}

