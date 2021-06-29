package org.doccreator.component;

import org.doccreator.CreateDocumentsRequest;
import org.doccreator.component.entity.CreateDocumentsRequestDTO;
import org.doccreator.service.CreateDocumentsService;
import org.doccreator.service.PutDocumentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.List;

@Component
public class Operator{

    private CreateDocumentsService cds;
    private PutDocumentsService pds;
    private WordInjector injector;

    @Autowired
    public Operator(CreateDocumentsService cds, WordInjector injector, PutDocumentsService pds){
        this.cds = cds;
        this.pds = pds;
    }

    public void CreateAndPutDocuments(CreateDocumentsRequestDTO request) throws Exception {
        cds.setBufferDir("D://JavaProjects//monkeywriter//buffer");
        cds.createDocuments(request);
        //pds.putDocuments(request, cds.getBufferDir());
    }
}
