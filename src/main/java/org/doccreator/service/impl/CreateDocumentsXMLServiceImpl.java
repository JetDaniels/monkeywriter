package org.doccreator.service.impl;

import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import freemarker.ext.dom.NodeModel;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.doccreator.component.entity.*;
import org.doccreator.service.*;
import org.doccreator.util.DTOGenerator;
import org.doccreator.util.DocumentCorrector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

@Service
public class CreateDocumentsXMLServiceImpl implements CreateDocumentsService {

    private final  String TEMPLATE_DIR = "templates";
    private final DocumentsService documentsService;
    private final CDRDocumentsService cdrDocumentsService;
    private final CreateDocumentsRequestsStepsService createDocumentsRequestsStepsService;
    private final PutDocumentsRequestService putDocumentsRequestService;
    private final PDRDocumentsService pdrDocumentsService;
    private String bufferDir;

    @Autowired
    public CreateDocumentsXMLServiceImpl(DocumentsService documentsService, CDRDocumentsService cdrDocumentsService,
                                         CreateDocumentsRequestsStepsService createDocumentsRequestsStepsService,
                                         PutDocumentsRequestService putDocumentsRequestService, PDRDocumentsService pdrDocumentsService){
        this.documentsService = documentsService;
        this.cdrDocumentsService = cdrDocumentsService;
        this.createDocumentsRequestsStepsService = createDocumentsRequestsStepsService;
        this.putDocumentsRequestService = putDocumentsRequestService;
        this.pdrDocumentsService = pdrDocumentsService;
    }

    @Override
    public void createDocuments(CreateDocumentsRequestDTO request) throws Exception {
        createDocumentsRequestsStepsService.save(DTOGenerator.getCDRStepDTO(request, 1, "Create documents",
                "Start to creating documents..."));
        PutDocumentsRequestDTO savePdr = putDocumentsRequestService.save(DTOGenerator.getPDRDTO(request));
        List<CDRDocumentDTO> documents = cdrDocumentsService.findById_CdrId(request.getId());
        for(CDRDocumentDTO document: documents) {
            DocumentDTO doc = documentsService.findOne(document.getId().getDocumentName());
            String fileLink = createDocument(request, doc);
            pdrDocumentsService.save(DTOGenerator.getPDRDocumentDTO(savePdr, document.getId().getDocumentName(), fileLink));
        }
        createDocumentsRequestsStepsService.save(DTOGenerator.getCDRStepDTO(request, 3, "Create documents",
                "Create document success"));
    }

    @Override
    public String createDocument(CreateDocumentsRequestDTO request, DocumentDTO document) throws Exception {
        String currentDate = new SimpleDateFormat("yyyyMMddHHmmssSSSSSSSSS").format(Calendar.getInstance().getTime());

        String templatePath = Objects.requireNonNull(
                getClass().getClassLoader().getResource(String.format("%s//%s.docx", TEMPLATE_DIR, document.getTemplate()))).getFile();
        FileInputStream in = new FileInputStream(new File(URLDecoder.decode(templatePath,"UTF-8")));
        IXDocReport report = XDocReportRegistry.getRegistry().loadReport(in,
                TemplateEngineKind.Freemarker);

        IContext context = report.createContext();
        InputStream xmlDataIn = new ByteArrayInputStream(request.getXmlData().getBytes(StandardCharsets.UTF_8));
        InputSource xmlDataSource = new InputSource(xmlDataIn);
        NodeModel data = NodeModel.parse(xmlDataSource);
        context.put("doc", data);

        File dirtyFile = new File(String.format("%s//%s_%s_dirty.docx", bufferDir, request.getRequestId(), currentDate));
        FileOutputStream out = new FileOutputStream(dirtyFile);
        report.process(context, out);

        XWPFDocument doc = new XWPFDocument(OPCPackage.open(String.format("%s//%s_%s_dirty.docx", bufferDir, request.getRequestId(), currentDate)));
        DocumentCorrector.removeMarks(doc);
        File cleanFile = new File(String.format("%s//%s_%s_out.docx", bufferDir, request.getRequestId(), currentDate));
        //File doneFile = new File(String.format("%s//%s_%s_out.done", bufferDir, request.getRequestId(), currentDate));
        FileOutputStream fosDoc = new FileOutputStream(cleanFile);
        //FileOutputStream fosDone = new FileOutputStream(doneFile);
        doc.write(fosDoc);
        doc.close();
        fosDoc.close();
        //fosDone.close();

        Files.delete(Paths.get(String.format("%s//%s_%s_dirty.docx", bufferDir, request.getRequestId(), currentDate)));
        return String.format("%s//%s_%s_out.docx", bufferDir, request.getRequestId(), currentDate);
    }

    @Override
    public void setBufferDir(String bufferDir) {
        this.bufferDir = bufferDir;
    }

    @Override
    public String getBufferDir() {
        return this.bufferDir;
    }
}
