package org.doccreator.component;

import lombok.Getter;
import org.doccreator.util.DOMOperationUtil;
import org.doccreator.util.XPathParser;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.nio.file.Files;

@Getter
@Component
public class WordInjector {
    private final static String OPERATING_ROOM = "D://JavaProjects//monkeywriter//operatingRoom";
    private final static String TEMPLATE_ROOM = "src//main//resources//templates";

    private String baseZipFolder;
    private String baseTemplate;
    private File baseContentTypesXml;
    private File baseRefXml;
    private File baseDocumentXml;
    private File baseStylesXml;
    private File baseNumberingXml;

    private String insertedZipFolder;
    private String insertedTemplate;
    private File insertedContentTypesXml;
    private File insertedRefXml;
    private File insertedDocumentXml;
    private File insertedStylesXml;
    private File insertedNumberingXml;

    public WordInjector(){}

    public WordInjector(String baseTemplateName, String insertedTemplateName){
        this.baseTemplate = baseTemplateName;
        this.insertedTemplate = insertedTemplateName;
        this.baseZipFolder = OPERATING_ROOM.concat("//").concat(baseTemplateName);
        this.insertedZipFolder = OPERATING_ROOM.concat("//").concat(insertedTemplateName);
        this.baseContentTypesXml = new File(baseZipFolder.concat("//[Content_Types].xml"));
        this.insertedContentTypesXml = new File(insertedZipFolder.concat("//[Content_Types].xml"));
        this.baseRefXml = new File(baseZipFolder.concat("//word//_rels//document.xml.rels"));
        this.insertedRefXml = new File(insertedZipFolder.concat("//word//_rels//document.xml.rels"));
        this.baseStylesXml = new File(baseZipFolder.concat("//word//styles.xml"));
        this.insertedStylesXml = new File(insertedZipFolder.concat("//word//styles.xml"));
        this.baseDocumentXml = new File(baseZipFolder.concat("//word//document.xml"));
        this.insertedDocumentXml = new File(insertedZipFolder.concat("//word//document.xml"));
        this.baseNumberingXml = new File(baseZipFolder.concat("//word//numbering.xml"));
        this.insertedNumberingXml = new File(insertedZipFolder.concat("//word//numbering.xml"));
    }

    //inject

    public void injectContentTypesXml() throws Exception{
        Document baseDoc = DOMOperationUtil.createDocument(baseContentTypesXml);
        Document insertedDoc = DOMOperationUtil.createDocument(insertedContentTypesXml);
        Node baseBody = baseDoc.getElementsByTagName("Types").item(0);
        Node insertedBody = insertedDoc.getElementsByTagName("Types").item(0);
        NodeList baseTypes = baseBody.getChildNodes();
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
        DOMOperationUtil.transform(baseDoc, baseContentTypesXml);
    }

    public void injectMedia() throws Exception {
        if(new File(insertedZipFolder.concat("//word//media")).exists()) {
            File[] sourceFiles = new File(insertedZipFolder.concat("//word//media")).listFiles();
            File newMediaFolder = new File(String.format("%s//word//%s", baseZipFolder, "media_".concat(insertedTemplate)));
            newMediaFolder.mkdir();
            for (File sourceFile : sourceFiles) {
                Files.copy(sourceFile.toPath(),
                        new File(String.format("%s//word//%s//%s", baseZipFolder, "media_".concat(insertedTemplate), sourceFile.getName())).toPath());
            }
        }
    }

    public void injectReferences() throws Exception {
        Document baseDoc = DOMOperationUtil.createDocument(baseRefXml);
        Document insertedDoc = DOMOperationUtil.createDocument(insertedRefXml);
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
        DOMOperationUtil.transform(baseDoc, baseRefXml);
    }

    public void injectStyles() throws Exception {
        Document baseDoc = DOMOperationUtil.createDocument(baseStylesXml);
        Document insertedDoc = DOMOperationUtil.createDocument(insertedStylesXml);
        Node baseBody = baseDoc.getElementsByTagName("w:styles").item(0);
        NodeList insertedStyles = insertedDoc.getElementsByTagName("w:style");
        for(int i=0; i<insertedStyles.getLength(); i++){
            Node insertedNode = baseDoc.importNode(insertedStyles.item(i),true);
            baseBody.appendChild(insertedNode);
        }
        DOMOperationUtil.transform(baseDoc, baseStylesXml);
    }

    public void injectNumbering() throws Exception {
        if((baseNumberingXml.exists())&&(insertedNumberingXml.exists())) {
            System.out.println("numbering exists");
            Document baseDoc = DOMOperationUtil.createDocument(baseNumberingXml);
            Document insertedDoc = DOMOperationUtil.createDocument(insertedNumberingXml);
            Node baseNumbering = baseDoc.getElementsByTagName("w:numbering").item(0);
            NodeList insertedAbstractNums = insertedDoc.getElementsByTagName("w:abstractNum");
            NodeList baseNums = baseDoc.getElementsByTagName("w:num");
            NodeList insertedNums = insertedDoc.getElementsByTagName("w:num");
            for (int i = 0; i < insertedAbstractNums.getLength(); i++) {
                Node insertedNode = baseDoc.importNode(insertedAbstractNums.item(i), true);
                baseNumbering.insertBefore(insertedNode, baseNums.item(0));
            }
            for (int i = 0; i < insertedNums.getLength(); i++) {
                Node insertedNode = baseDoc.importNode(insertedNums.item(i), true);
                baseNumbering.insertBefore(insertedNode, baseNums.item(0));
            }
            DOMOperationUtil.transform(baseDoc, baseNumberingXml);
        }else if(insertedNumberingXml.exists()){
            System.out.println("numbering not exists");
            Files.copy(insertedNumberingXml.toPath(), baseNumberingXml.toPath());
        }
    }

    public void injectParagraphs(String link) throws Exception {
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
        DOMOperationUtil.transform(baseDoc, baseDocumentXml);
    }

    public void removeParagraph(String link) throws Exception {
        Document baseDoc = DOMOperationUtil.createDocument(baseDocumentXml);
        Node baseBody = baseDoc.getElementsByTagName("w:body").item(0);
        Node removedNode = XPathParser.getNode(baseDoc ,String.format("//*[@*[local-name()='extId']='%s']", link));
        System.out.println("count: " + baseBody.getChildNodes().getLength());
        baseBody.removeChild(removedNode);
        System.out.println("count: " + baseBody.getChildNodes().getLength());
        DOMOperationUtil.transform(baseDoc, baseDocumentXml);
    }

    //rename, возможно стоит объеденить с inject

    public void renameInsertedImageData() throws Exception {
        Document documentDoc = DOMOperationUtil.createDocument(insertedDocumentXml);
        NodeList imagesData = XPathParser.getNodes(documentDoc,"//*[local-name()='imagedata']");
        for(int i=0; i<imagesData.getLength(); i++){
            NamedNodeMap attrs = imagesData.item(i).getAttributes();
            Node value;
            if((attrs != null)&&((value = attrs.getNamedItem("r:id")) != null)){
                value.setNodeValue(value.getNodeValue().concat("_").concat(insertedTemplate));
            }
        }
        DOMOperationUtil.transform(documentDoc, insertedDocumentXml);
    }

    public void renameInsertedReferences() throws Exception {
        Document refDoc = DOMOperationUtil.createDocument(insertedRefXml);
        NodeList refs = refDoc.getElementsByTagName("Relationship");
        for(int i=0; i<refs.getLength(); i++){
            NamedNodeMap attrs = refs.item(i).getAttributes();
            Node type;
            Node id;
            Node target;
            if((attrs != null)&&((id = attrs.getNamedItem("Id")) != null)&&((target = attrs.getNamedItem("Target")) != null)&&((type = attrs.getNamedItem("Type")) != null)){
                if(type.getNodeValue().contains("relationships/image")){
                    id.setNodeValue(id.getNodeValue().concat("_").concat(insertedTemplate));
                    target.setNodeValue(target.getNodeValue().replace("media", "media_".concat(insertedTemplate)));
                }else if(type.getNodeValue().contains("relationships/numbering")){
                    //сделать по принципу дополнения
                }
            }
        }
        DOMOperationUtil.transform(refDoc, insertedRefXml);
    }

    public void renameStyleInDocumentXml() throws Exception {
        Document documentDoc = DOMOperationUtil.createDocument(insertedDocumentXml);
        NodeList styles = XPathParser.getNodes(documentDoc, "//*[local-name()='pStyle'] | //*[local-name()='tblStyle']");
        for(int i=0; i<styles.getLength(); i++){
            NamedNodeMap attrs = styles.item(i).getAttributes();
            Node value;
            if((attrs != null)&&((value = attrs.getNamedItem("w:val")) != null)){
                value.setNodeValue(value.getNodeValue().concat("_").concat(insertedTemplate));
            }
        }
        DOMOperationUtil.transform(documentDoc, insertedDocumentXml);
    }

    public void renameInsertedStyleIdInStyleXml() throws Exception {
        Document styleDoc = DOMOperationUtil.createDocument(insertedStylesXml);
        NodeList styles = styleDoc.getElementsByTagName("w:style");
        for(int i=0; i<styles.getLength(); i++){
            NamedNodeMap attrs = styles.item(i).getAttributes();
            Node styleId;
            if((attrs != null)&&((styleId = attrs.getNamedItem("w:styleId")) != null)){
                styleId.setNodeValue(styleId.getNodeValue().concat("_").concat(insertedTemplate));
            }
        }
        DOMOperationUtil.transform(styleDoc, insertedStylesXml);
    }

    public void renameBaseNumberingInNumberingXml() throws Exception {
        if(baseNumberingXml.exists()) {
            Document numberingDoc = DOMOperationUtil.createDocument(baseDocumentXml);
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
            DOMOperationUtil.transform(numberingDoc, baseDocumentXml);
        }
    }

    public void renameInsertedNumberingInNumberingXml() throws Exception {
        if(insertedNumberingXml.exists()) {
            Document numberingDoc = DOMOperationUtil.createDocument(insertedNumberingXml);
            NodeList abstractNums = numberingDoc.getElementsByTagName("w:abstractNum");
            for (int i = 0; i < abstractNums.getLength(); i++) {
                NamedNodeMap attrs = abstractNums.item(i).getAttributes();
                Node abstractNumId;
                if ((attrs != null) && ((abstractNumId = attrs.getNamedItem("w:abstractNumId")) != null)) {
                    abstractNumId.setNodeValue(insertedTemplate.replace("block","").concat(abstractNumId.getNodeValue()));
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
                        abstractNumId.setNodeValue(insertedTemplate.replace("block","").concat(abstractNumId.getNodeValue()));
                    }
                    numId.setNodeValue(insertedTemplate.replace("block","").concat(numId.getNodeValue()));
                }
            }
            DOMOperationUtil.transform(numberingDoc, insertedNumberingXml);
        }
    }

    public void renameInsertedNumberingInDocumentXml() throws Exception {
        if(insertedNumberingXml.exists()) {
            Document documentDoc = DOMOperationUtil.createDocument(insertedDocumentXml);
            String expression = "//*[local-name()='p']/*[local-name()='pPr']/*[local-name()='numPr']/*[local-name()='numId']/@*[local-name()='val']";
            NodeList numberingIds = XPathParser.getNodes(documentDoc, expression);
            for(int i=0; i<numberingIds.getLength(); i++){
                Node numberingId = numberingIds.item(i);
                numberingId.setNodeValue(insertedTemplate.replace("block","").concat(numberingId.getNodeValue()));
            }
            DOMOperationUtil.transform(documentDoc, insertedDocumentXml);
        }
    }

}
