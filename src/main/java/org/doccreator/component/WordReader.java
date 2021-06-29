package org.doccreator.component;

import org.doccreator.util.DOMOperationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

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

        //смотрим document.xml
        File documentXml = new File(OPERATING_ROOM.concat("//" + wordFile.getName().replace("docx","")).concat("//word//document.xml"));
        Document base = DOMOperationUtil.createDocument(documentXml);
        NodeList paragraphs = base.getElementsByTagName("w:p");
        for(int i=paragraphs.getLength()-1; i>=0; i--){
            Node paragraph = paragraphs.item(i);

            //ищем ссылку на сабдокументы/блоки в параграфе
            String link = searchLink(paragraph);
            if(link != null) {
                String childFolderName = link
                        .replace("MERGEFIELD  //@", "")
                        .replace("\\* MERGEFORMAT","")
                        .replace("«//@","")
                        .replace("»","")
                        .trim();
                System.out.println(childFolderName);
                File childTemplateFile = new File(TEMPLATE_ROOM.concat("//" + childFolderName).concat(".docx"));
                archiver.unzipTemplate(childTemplateFile);

                WordInjector wordInjector = new WordInjector(wordFile.getName().replace(".docx",""), childFolderName);

                //Дополняем [Content_Types].xml
                wordInjector.injectContentTypesXml();

                //Вставляем ссылки сабдокумента
                wordInjector.renameChildReferences();
                wordInjector.injectReferences();

                //Вставляем медиа-файлы сабдокумента
                wordInjector.renameChildImageData();
                wordInjector.injectMedia();

                //Вставляем стили сабдокумента
                wordInjector.renameChildStyleInDocumentXml();
                wordInjector.renameChildStyleIdInStyleXml();
                wordInjector.injectStyles();

                //Вставляем параметры списков
                wordInjector.renameChildNumberingInNumberingXml();
                wordInjector.renameChildNumberingInDocumentXml();
                wordInjector.renameBaseNumberingInNumberingXml();
                wordInjector.injectNumbering();

                //Вставляем содержание сабдокумента
                wordInjector.injectChildParagraphs(i);
            }
        }
    }

    public String searchLink(Node node){
        NodeList childNodes = node.getChildNodes();
        if(childNodes != null){
            for(int i=0; i<childNodes.getLength(); i++){
                Node currNode = childNodes.item(i);
                String value = currNode.getNodeValue();
                String link = null;
                if((value != null)&&(value.contains("//@block"))) {
                    link = value;
                }else link = searchLink(currNode);
                if(link != null) return link;
            }
        }
        return null;
    }
}
