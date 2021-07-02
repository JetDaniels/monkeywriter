package org.doccreator.component;

import fr.opensagres.xdocreport.core.io.XDocArchive;
import org.doccreator.util.DOMOperationUtil;
import org.doccreator.util.DocFileStructure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class WordReader {

    private final static String TEMPLATES_FOLDER = "src//main//resources//templates";
    private Archiver archiver;

    @Autowired
    public WordReader(Archiver archiver){
        this.archiver = archiver;
    }

    public void searchLinks(File wordFile) throws Exception {
        //смотрим document.xml и ищем ссылки
        XDocArchive baseWordArchive = archiver.getArchive(wordFile);
        InputStream documentXml = archiver.extractFileForRead(baseWordArchive, DocFileStructure.documentXml);
        Document baseDoc = DOMOperationUtil.createDocument(documentXml);
        Element baseBody = (Element) baseDoc.getElementsByTagName("w:body").item(0);
        List<String> links = new ArrayList<>();
        for(int i=0; i<baseBody.getChildNodes().getLength(); i++) {
            Element paragraph = (Element) baseBody.getChildNodes().item(i);
            String link = markParagraph(paragraph);
            if(link != null) links.add(link);
        }
        documentXml = DOMOperationUtil.transform(baseDoc, archiver.extractFileForTransform(baseWordArchive, DocFileStructure.documentXml));
        archiver.putFile(baseWordArchive, DocFileStructure.documentXml, documentXml);

        //отрабатываем ссылки
        for(String link: links) {
            System.out.println(link);
            XDocArchive insertedWordArchive = archiver.getArchive(new File(TEMPLATES_FOLDER.concat("//" + link + ".docx")));
            WordInjector wordInjector = new WordInjector(baseWordArchive, insertedWordArchive, link);
            wordInjector.inject();
        }
        archiver.writeArchive(baseWordArchive, wordFile.getName());
    }

    private String markParagraph(Element paragraph) throws Exception {
        String mark;
        if((mark = searchMark(paragraph)) != null){
            paragraph.setAttribute("extId", mark);
            return mark;
        }
        return null;
    }

    private String searchMark(Node node){
        NodeList childNodes = node.getChildNodes();
        if(childNodes != null){
            for(int i=0; i<childNodes.getLength(); i++){
                Node currNode = childNodes.item(i);
                String value = currNode.getTextContent();
                String link = null;
                if((value != null)&&(value.contains("//@block"))) {
                    link = value.replace("//@","") .trim();
                }else link = searchMark(currNode);
                if(link != null) return link;
            }
        }
        return null;
    }
}
