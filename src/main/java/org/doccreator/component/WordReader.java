package org.doccreator.component;

import org.doccreator.util.DOMOperationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Component
public class WordReader {
    private final static String OPERATING_ROOM = "D://JavaProjects//monkeywriter//operatingRoom";
    private final static String TEMPLATE_ROOM = "src//main//resources//templates";
    private Archiver archiver;

    @Autowired
    public WordReader(Archiver archiver){
        this.archiver = archiver;
    }

    public void searchLinks(File wordFile) throws Exception {
        //достаем из архива содержимое файла docx
        archiver.unzipTemplate(wordFile);
        String baseTemplate = wordFile.getName().replace(".docx","");

        //смотрим document.xml и ищем ссылки
        File documentXml = new File(OPERATING_ROOM.concat("//" + baseTemplate).concat("//word//document.xml"));
        Document baseDoc = DOMOperationUtil.createDocument(documentXml);
        Element baseBody = (Element) baseDoc.getElementsByTagName("w:body").item(0);
        List<String> links = new ArrayList<>();
        for(int i=0; i<baseBody.getChildNodes().getLength(); i++) {
            Element paragraph = (Element) baseBody.getChildNodes().item(i);
            String link = markParagraph(baseDoc, documentXml, paragraph);
            if(link != null) links.add(link);
            DOMOperationUtil.transform(baseDoc, documentXml);
        }

        //отрабатываем ссылки
        for(String link: links) {
            System.out.println(link);
            File childTemplateFile = new File(TEMPLATE_ROOM.concat("//" + link).concat(".docx"));
            archiver.unzipTemplate(childTemplateFile);

            WordInjector wordInjector = new WordInjector(baseTemplate, link);

            //Дополняем [Content_Types].xml
            wordInjector.injectContentTypesXml();

            //Вставляем ссылки сабдокумента
            wordInjector.renameInsertedReferences();
            wordInjector.injectReferences();

            //Вставляем медиа-файлы сабдокумента
            wordInjector.renameInsertedImageData();
            wordInjector.injectMedia();

            //Вставляем стили сабдокумента
            wordInjector.renameStyleInDocumentXml();
            wordInjector.renameInsertedStyleIdInStyleXml();
            wordInjector.injectStyles();

            //Вставляем параметры списков
            wordInjector.renameInsertedNumberingInNumberingXml();
            wordInjector.renameInsertedNumberingInDocumentXml();
            wordInjector.renameBaseNumberingInNumberingXml();
            wordInjector.injectNumbering();

            //Вставляем содержание сабдокумента
            wordInjector.injectParagraphs(link);

            //Удаляем параграф с ссылкой
            wordInjector.removeParagraph(link);
        }
        archiver.zipTemplate(new File(OPERATING_ROOM.concat("//" + baseTemplate)));
    }

    private String markParagraph(Document doc, File documentXml, Element paragraph) throws Exception {
        String mark;
        if((mark = searchMark(paragraph)) != null){
            paragraph.setAttribute("extId", mark);
            return mark;
            //возможно стоит склеивать
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
