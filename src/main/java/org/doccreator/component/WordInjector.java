package org.doccreator.component;

import fr.opensagres.xdocreport.core.io.XDocArchive;
import lombok.Getter;
import org.doccreator.util.DOMOperationUtil;
import org.doccreator.util.DocFileStructure;
import org.doccreator.util.XPathParser;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.InputStream;
import java.util.Set;

@Getter
@Component
public class WordInjector {

    private Archiver archiver;
    private XDocArchive baseTemplateArchive;
    private String insertedTemplateName;
    private XDocArchive insertedTemplateArchive;

    public WordInjector(){
        this.archiver = new Archiver();
    }

    public WordInjector(XDocArchive baseTemplate, XDocArchive insertedTemplate, String insertedTemplateName) throws Exception {
        this.archiver = new Archiver();
        this.baseTemplateArchive = baseTemplate;
        this.insertedTemplateArchive = insertedTemplate;
        this.insertedTemplateName = insertedTemplateName;
    }

    //inject

    public void inject() throws Exception {
        System.out.println("start inject");

        //Дополняем [Content_Types].xml;
        injectContentTypesXml();

        //Вставляем ссылки сабдокумента
        renameInsertedReferences();
        injectReferences();

        //Вставляем медиа-файлы сабдокумента
        renameInsertedImageData();
        injectMedia(insertedTemplateName);

        //Вставляем стили сабдокумента
        renameStyleInDocumentXml();
        renameInsertedStyleIdInStyleXml();
        injectStyles();

        //Вставляем параметры списков
        renameInsertedNumberingInNumberingXml();
        renameInsertedNumberingInDocumentXml();
        //renameBaseNumberingInNumberingXml();
        injectNumbering();

        //Вставляем содержание сабдокумента
        injectParagraphs(insertedTemplateName);

        //Удаляем параграф с ссылкой
        removeParagraph(insertedTemplateName);
    }

    public void injectContentTypesXml() throws Exception{
        System.out.println("injectContentTypesXml");
        Document baseDoc = DOMOperationUtil.createDocument(archiver.extractFileForRead(baseTemplateArchive,DocFileStructure.contentTypes));
        Document insertedDoc = DOMOperationUtil.createDocument(archiver.extractFileForRead(insertedTemplateArchive,DocFileStructure.contentTypes));
        Node baseBody = baseDoc.getElementsByTagName("Types").item(0);
        Node insertedBody = insertedDoc.getElementsByTagName("Types").item(0);
        NodeList insertedTypes = insertedBody.getChildNodes();
        for(int i=0; i<insertedTypes.getLength(); i++){
            NamedNodeMap attrs = insertedTypes.item(i).getAttributes();
            Node name;
            if((attrs != null)
                    &&((((name = attrs.getNamedItem("Extension")) != null)||(name = attrs.getNamedItem("PartName")) != null))){
                String expression = String.format("//*[@Extension='%s'] | //*[@PartName='%s']", name.getNodeValue(), name.getNodeValue());
                NodeList leftNodes = XPathParser.getNodes(baseDoc, expression);
                if((leftNodes == null)||(leftNodes.getLength() <= 0)){
                    Node insertedNode = baseDoc.importNode(insertedTypes.item(i),true);
                    baseBody.appendChild(insertedNode);
                }
            }
        }
        Archiver archiver = new Archiver();
        InputStream baseContentTypesXml = DOMOperationUtil.transform(baseDoc, archiver.extractFileForTransform(baseTemplateArchive, DocFileStructure.contentTypes));
        archiver.putFile(baseTemplateArchive ,DocFileStructure.contentTypes ,baseContentTypesXml);
    }

    public void injectMedia(String mark) throws Exception {
        System.out.println("injectMedia");
        Set<ArchivedFile> mediaFolder = archiver.extractFilesForRead(insertedTemplateArchive, DocFileStructure.mediaFolder);
        for(ArchivedFile archivedFile: mediaFolder) {
            archiver.putFile(baseTemplateArchive
                    , archivedFile.getFileName().replace("media","media_" + mark)
                    , archivedFile.getInputStream());
        }
    }

    private void injectReferences() throws Exception {
        System.out.println("injectReferences");
        Document baseDoc = DOMOperationUtil.createDocument(archiver.extractFileForRead(baseTemplateArchive,DocFileStructure.refsXml));
        Document insertedDoc = DOMOperationUtil.createDocument(archiver.extractFileForRead(insertedTemplateArchive,DocFileStructure.refsXml));
        Node baseBody = baseDoc.getElementsByTagName("Relationships").item(0);
        NodeList insertedRefs = insertedDoc.getElementsByTagName("Relationship");
        for(int i=0; i<insertedRefs.getLength(); i++){
            NamedNodeMap attrs = insertedRefs.item(i).getAttributes();
            Node type;
            Node id;
            if((attrs != null)&&((id = attrs.getNamedItem("Id")) != null)&&((type = attrs.getNamedItem("Type")) != null)){
                if(id.getNodeValue().contains("block")){
                    Node insertedNode = baseDoc.importNode(insertedRefs.item(i),true);
                    baseBody.appendChild(insertedNode);
                }
            }
        }
        InputStream baseRefXml = DOMOperationUtil.transform(baseDoc, archiver.extractFileForTransform(baseTemplateArchive, DocFileStructure.refsXml));
        archiver.putFile(baseTemplateArchive ,DocFileStructure.refsXml ,baseRefXml);
    }

    public void injectStyles() throws Exception {
        System.out.println("injectStyles");
        Document baseDoc = DOMOperationUtil.createDocument(archiver.extractFileForRead(baseTemplateArchive, DocFileStructure.stylesXml));
        Document insertedDoc = DOMOperationUtil.createDocument(archiver.extractFileForRead(insertedTemplateArchive, DocFileStructure.stylesXml));
        Node baseBody = baseDoc.getElementsByTagName("w:styles").item(0);
        NodeList insertedStyles = insertedDoc.getElementsByTagName("w:style");
        for(int i=0; i<insertedStyles.getLength(); i++){
            Node insertedNode = baseDoc.importNode(insertedStyles.item(i),true);
            baseBody.appendChild(insertedNode);
        }
        InputStream baseStylesXml = DOMOperationUtil.transform(baseDoc, archiver.extractFileForTransform(baseTemplateArchive, DocFileStructure.stylesXml));
        archiver.putFile(baseTemplateArchive ,DocFileStructure.stylesXml ,baseStylesXml);
    }

    public void injectNumbering() throws Exception {
        System.out.println("injectNumbering");
        InputStream baseNumberingXml = archiver.extractFileForRead(baseTemplateArchive, DocFileStructure.numberingXml);
        InputStream insertedNumberingXml = archiver.extractFileForRead(insertedTemplateArchive, DocFileStructure.numberingXml);
        if((baseNumberingXml != null)&&(insertedNumberingXml != null)) {
            Document baseDoc = DOMOperationUtil.createDocument(baseNumberingXml);
            Node baseNumbering = baseDoc.getElementsByTagName("w:numbering").item(0);
            NodeList baseNums = baseDoc.getElementsByTagName("w:num");

            Document insertedDoc = DOMOperationUtil.createDocument(insertedNumberingXml);
            NodeList insertedAbstractNums = insertedDoc.getElementsByTagName("w:abstractNum");
            NodeList insertedNums = insertedDoc.getElementsByTagName("w:num");
            for (int i = 0; i < insertedAbstractNums.getLength(); i++) {
                Node insertedNode = baseDoc.importNode(insertedAbstractNums.item(i), true);
                baseNumbering.insertBefore(insertedNode, baseNums.item(0));
            }
            for (int i = 0; i < insertedNums.getLength(); i++) {
                Node insertedNode = baseDoc.importNode(insertedNums.item(i), true);
                baseNumbering.insertBefore(insertedNode, baseNums.item(0));
            }
            InputStream newBaseNumberingXml = DOMOperationUtil.transform(baseDoc, archiver.extractFileForTransform(baseTemplateArchive, DocFileStructure.numberingXml));
            archiver.putFile(baseTemplateArchive ,DocFileStructure.numberingXml ,newBaseNumberingXml);
        }else if(insertedNumberingXml != null){
            //нужно сделать это дерьмище
            //Files.copy(insertedNumberingXml.toPath(), baseNumberingXml.toPath());
        }
    }

    public void injectParagraphs(String link) throws Exception {
        InputStream baseDocumentXml = archiver.extractFileForRead(baseTemplateArchive, DocFileStructure.documentXml);
        InputStream insertedDocumentXml = archiver.extractFileForRead(insertedTemplateArchive, DocFileStructure.documentXml);
        Document baseDoc = DOMOperationUtil.createDocument(baseDocumentXml);
        Document insertedDoc = DOMOperationUtil.createDocument(insertedDocumentXml);
        Node baseBody = baseDoc.getElementsByTagName("w:body").item(0);
        Node insertedBody = insertedDoc.getElementsByTagName("w:body").item(0);
        NodeList insertedParagraphs = insertedBody.getChildNodes();
        Node referenceNode = XPathParser.getNode(baseDoc ,String.format("//*[@*[local-name()='extId']='%s']", link));
        for(int i=0; i<insertedParagraphs.getLength(); i++){
            Node insertedNode = baseDoc.importNode(insertedParagraphs.item(i),true);
            baseBody.insertBefore(insertedNode, referenceNode);
        }
        InputStream newBaseDocumentXml = DOMOperationUtil.transform(baseDoc, archiver.extractFileForTransform(baseTemplateArchive, DocFileStructure.documentXml));
        archiver.putFile(baseTemplateArchive ,DocFileStructure.documentXml ,newBaseDocumentXml);
    }

    public void removeParagraph(String link) throws Exception {
        System.out.println("removeParagraph");
        InputStream baseDocumentXml = archiver.extractFileForRead(baseTemplateArchive, DocFileStructure.documentXml);
        Document baseDoc = DOMOperationUtil.createDocument(baseDocumentXml);
        Node baseBody = baseDoc.getElementsByTagName("w:body").item(0);
        Node removedNode = XPathParser.getNode(baseDoc ,String.format("//*[@*[local-name()='extId']='%s']", link));
        baseBody.removeChild(removedNode);
        InputStream newBaseDocumentXml = DOMOperationUtil.transform(baseDoc, archiver.extractFileForTransform(baseTemplateArchive, DocFileStructure.documentXml));
        archiver.putFile(baseTemplateArchive ,DocFileStructure.documentXml ,newBaseDocumentXml);
    }

    //rename, возможно стоит объеденить с inject

    public void renameInsertedImageData() throws Exception {
        Document documentDoc = DOMOperationUtil.createDocument(archiver.extractFileForRead(insertedTemplateArchive, DocFileStructure.documentXml));
        NodeList imagesData = XPathParser.getNodes(documentDoc,"//*[local-name()='imagedata']");
        for(int i=0; i<imagesData.getLength(); i++){
            NamedNodeMap attrs = imagesData.item(i).getAttributes();
            Node value;
            if((attrs != null)&&((value = attrs.getNamedItem("r:id")) != null)){
                value.setNodeValue(value.getNodeValue().concat("_").concat(insertedTemplateName));
            }
        }
        InputStream insertedDoc = DOMOperationUtil.transform(documentDoc, archiver.extractFileForTransform(insertedTemplateArchive, DocFileStructure.documentXml));
        archiver.putFile(insertedTemplateArchive ,DocFileStructure.documentXml ,insertedDoc);
    }

    private void renameInsertedReferences() throws Exception {
        System.out.println("renameInsertedReferences");
        Document refDoc = DOMOperationUtil.createDocument(archiver.extractFileForRead(insertedTemplateArchive,DocFileStructure.refsXml));
        NodeList refs = refDoc.getElementsByTagName("Relationship");
        for(int i=0; i<refs.getLength(); i++){
            NamedNodeMap attrs = refs.item(i).getAttributes();
            Node type;
            Node id;
            Node target;
            if((attrs != null)&&((id = attrs.getNamedItem("Id")) != null)&&((target = attrs.getNamedItem("Target")) != null)&&((type = attrs.getNamedItem("Type")) != null)){
                if(type.getNodeValue().contains("relationships/image")){
                    id.setNodeValue(id.getNodeValue().concat("_").concat(insertedTemplateName));
                    target.setNodeValue(target.getNodeValue().replace("media", "media_".concat(insertedTemplateName)));
                }else if(type.getNodeValue().contains("relationships/numbering")){
                    //сделать по принципу дополнения
                }
            }
        }
        InputStream insertedRefXml = DOMOperationUtil.transform(refDoc, archiver.extractFileForTransform(insertedTemplateArchive, DocFileStructure.refsXml));
        archiver.putFile(insertedTemplateArchive ,DocFileStructure.refsXml ,insertedRefXml);
    }

    public void renameStyleInDocumentXml() throws Exception {
        System.out.println("renameStyleInDocumentXml");
        Document documentDoc = DOMOperationUtil.createDocument(archiver.extractFileForRead(insertedTemplateArchive, DocFileStructure.documentXml));
        NodeList styles = XPathParser.getNodes(documentDoc, "//*[local-name()='pStyle'] | //*[local-name()='tblStyle']");
        for(int i=0; i<styles.getLength(); i++){
            NamedNodeMap attrs = styles.item(i).getAttributes();
            Node value;
            if((attrs != null)&&((value = attrs.getNamedItem("w:val")) != null)){
                value.setNodeValue(value.getNodeValue().concat("_").concat(insertedTemplateName));
            }
        }
        InputStream insertedStyles = DOMOperationUtil.transform(documentDoc, archiver.extractFileForTransform(insertedTemplateArchive, DocFileStructure.documentXml));
        archiver.putFile(insertedTemplateArchive ,DocFileStructure.documentXml ,insertedStyles);
    }

    public void renameInsertedStyleIdInStyleXml() throws Exception {
        System.out.println("renameInsertedStyleIdInStyleXml");
        Document styleDoc = DOMOperationUtil.createDocument(archiver.extractFileForRead(insertedTemplateArchive, DocFileStructure.stylesXml));
        NodeList styles = styleDoc.getElementsByTagName("w:style");
        for(int i=0; i<styles.getLength(); i++){
            NamedNodeMap attrs = styles.item(i).getAttributes();
            Node styleId;
            if((attrs != null)&&((styleId = attrs.getNamedItem("w:styleId")) != null)){
                styleId.setNodeValue(styleId.getNodeValue().concat("_").concat(insertedTemplateName));
            }
        }
        InputStream insertedStyles = DOMOperationUtil.transform(styleDoc, archiver.extractFileForTransform(insertedTemplateArchive, DocFileStructure.stylesXml));
        archiver.putFile(insertedTemplateArchive ,DocFileStructure.stylesXml ,insertedStyles);
    }

    public void renameBaseNumberingInNumberingXml() throws Exception {
        InputStream baseNumberingXml = archiver.extractFileForRead(baseTemplateArchive, DocFileStructure.numberingXml);
        if(baseNumberingXml != null) {
            Document numberingDoc = DOMOperationUtil.createDocument(archiver.extractFileForRead(baseTemplateArchive, DocFileStructure.numberingXml));
            NodeList abstractNums = numberingDoc.getElementsByTagName("w:abstractNum");
            for (int i = 0; i < abstractNums.getLength(); i++) {
                NamedNodeMap attrs = abstractNums.item(i).getAttributes();
                Node abstractNumId;
                if ((attrs != null) && ((abstractNumId = attrs.getNamedItem("w:abstractNumId")) != null)) {
                    abstractNumId.setNodeValue("0".concat(abstractNumId.getNodeValue()));
                }
            }
            NodeList nums = numberingDoc.getElementsByTagName("w:num");
            for (int i = 0; i < nums.getLength(); i++) {
                NamedNodeMap attrsNum = nums.item(i).getAttributes();
                Node numId;
                if ((attrsNum != null) && ((numId = attrsNum.getNamedItem("w:numId")) != null)) {
                    Node abstractNumId = XPathParser.getNodes(numberingDoc
                            , String.format("//*[local-name()='num' and @*[local-name()='numId'] = '%s']/*[local-name()='abstractNumId']/@*[local-name()='val']"
                                    , numId.getNodeValue())
                    ).item(0);
                    if (abstractNumId != null) {
                        abstractNumId.setNodeValue("0".concat(abstractNumId.getNodeValue()));
                    }
                    numId.setNodeValue("0".concat(numId.getNodeValue()));
                }
            }
            InputStream baseDocumentXml = DOMOperationUtil.transform(numberingDoc, archiver.extractFileForTransform(baseTemplateArchive, DocFileStructure.numberingXml));
            archiver.putFile(baseTemplateArchive ,DocFileStructure.numberingXml ,baseDocumentXml);
        }
    }

    public void renameInsertedNumberingInNumberingXml() throws Exception {
        System.out.println("renameInsertedNumberingInNumberingXml");
        InputStream insertedNumberingXml = archiver.extractFileForRead(insertedTemplateArchive, DocFileStructure.numberingXml);
        if(insertedNumberingXml != null) {
            Document numberingDoc = DOMOperationUtil.createDocument(insertedNumberingXml);
            NodeList abstractNums = numberingDoc.getElementsByTagName("w:abstractNum");
            for (int i = 0; i < abstractNums.getLength(); i++) {
                NamedNodeMap attrs = abstractNums.item(i).getAttributes();
                Node abstractNumId;
                if ((attrs != null) && ((abstractNumId = attrs.getNamedItem("w:abstractNumId")) != null)) {
                    abstractNumId.setNodeValue(insertedTemplateName.replace("block","").concat(abstractNumId.getNodeValue()));
                }
            }
            NodeList nums = numberingDoc.getElementsByTagName("w:num");
            for (int i = 0; i < nums.getLength(); i++) {
                NamedNodeMap attrsNum = nums.item(i).getAttributes();
                Node numId;
                if ((attrsNum != null) && ((numId = attrsNum.getNamedItem("w:numId")) != null)) {
                    Node abstractNumId = XPathParser.getNodes(numberingDoc
                            , String.format("//*[local-name()='num' and @*[local-name()='numId'] = '%s']/*[local-name()='abstractNumId']/@*[local-name()='val']"
                                    , numId.getNodeValue())
                    ).item(0);
                    if (abstractNumId != null) {
                        abstractNumId.setNodeValue(insertedTemplateName.replace("block","").concat(abstractNumId.getNodeValue()));
                    }
                    numId.setNodeValue(insertedTemplateName.replace("block","").concat(numId.getNodeValue()));
                }
            }
            InputStream insertedNumbering = DOMOperationUtil.transform(numberingDoc, archiver.extractFileForTransform(insertedTemplateArchive, DocFileStructure.numberingXml));
            archiver.putFile(insertedTemplateArchive ,DocFileStructure.numberingXml ,insertedNumbering);
        }
    }

    public void renameInsertedNumberingInDocumentXml() throws Exception {
        System.out.println("renameInsertedNumberingInDocumentXml");
        InputStream insertedNumberingXml = archiver.extractFileForRead(insertedTemplateArchive, DocFileStructure.numberingXml);
        if(insertedNumberingXml != null) {
            Document documentDoc = DOMOperationUtil.createDocument(archiver.extractFileForRead(insertedTemplateArchive, DocFileStructure.documentXml));
            String expression = "//*[local-name()='p']/*[local-name()='pPr']/*[local-name()='numPr']/*[local-name()='numId']/@*[local-name()='val']";
            NodeList numberingIds = XPathParser.getNodes(documentDoc, expression);
            for(int i=0; i<numberingIds.getLength(); i++){
                Node numberingId = numberingIds.item(i);
                numberingId.setNodeValue(insertedTemplateName.replace("block","").concat(numberingId.getNodeValue()));
            }
            InputStream insertedNumbering = DOMOperationUtil.transform(documentDoc, archiver.extractFileForTransform(insertedTemplateArchive, DocFileStructure.documentXml));
            archiver.putFile(insertedTemplateArchive ,DocFileStructure.documentXml ,insertedNumbering);
        }
    }

}
