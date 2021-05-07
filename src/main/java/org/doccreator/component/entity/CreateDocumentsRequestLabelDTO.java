package org.doccreator.component.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="create_documents_requests")
public class CreateDocumentsRequestLabelDTO {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name="request_id")
    private String requestId;

    @Column(name="outside_system")
    private String outsideSystem;

    @Column(name="connection")
    private String connection;

    @Column(name="docs_count")
    private Integer docsCount;

    @Column(name="time_begin")
    private Timestamp timeBegin;
}
