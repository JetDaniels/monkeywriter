package org.doccreator.component;

import org.doccreator.CreateDocumentsRequest;
import org.doccreator.component.entity.CreateDocumentsRequestDTO;
import org.doccreator.service.CreateDocumentsService;
import org.doccreator.service.PutDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Operator{

    private CreateDocumentsService cds;
    private PutDocumentsService pds;

    @Autowired
    public Operator(CreateDocumentsService cds, PutDocumentsService pds){
        this.cds = cds;
        this.pds = pds;
    }

    public void CreateAndPutDocuments(CreateDocumentsRequestDTO request) throws Exception {
        cds.setBufferDir("D://JavaProjects//monkeywriter//buffer");
        cds.createDocuments(request);
        pds.putDocuments(request, cds.getBufferDir());
    }
}
