package org.doccreator.component.entity;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "put_documents_requests")
@SequenceGenerator(name = "put_documents_requests_id_seq", sequenceName = "put_documents_requests_id_seq", allocationSize = 1)
public class PutDocumentsRequestDTO {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "put_documents_requests_id_seq")
    private Integer id;

    @Column(name = "cdr_id")
    private Integer cdrId;

    @Column(name = "xml_data")
    private String xmlData;

    @Column(name = "time_begin")
    private Timestamp timeBegin;
}