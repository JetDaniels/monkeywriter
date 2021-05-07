package org.doccreator.component.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name="create_documents_requests")
@SequenceGenerator(name = "create_documents_requests_id_seq", sequenceName = "create_documents_requests_id_seq", allocationSize = 1)
public class CreateDocumentsRequestDTO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "create_documents_requests_id_seq")
    private Integer id;

    @Column(name="request_id")
    private String requestId;

    @Column(name="outside_system")
    private String outsideSystem;

    @Column(name="connection")
    private String connection;

    @Column(name="docs_count")
    private Integer docsCount;

    @Column(name="xml_data")
    private String xmlData;

    @Column(name = "duration")
    private long duration;

    @Column(name="time_begin")
    private Timestamp timeBegin;
}
