package org.doccreator.component;

import lombok.Getter;
import org.doccreator.util.DOMOperationUtil;
import org.doccreator.util.XPathParser;
import org.springframework.beans.factory.annotation.Value;
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

    private String childZipFolder;
    private String childTemplate;
    private File childContentTypesXml;
    private File childRefXml;
    private File childDocumentXml;
    private File childStylesXml;
    private File childNumberingXml;

    public WordInjector(){}

    public WordInjector(String baseTemplateName, String childTemplateName){
        this.baseTemplate = baseTemplateName;
        this.childTemplate = childTemplateName;
        this.baseZipFolder = OPERATING_ROOM.concat("//").concat(baseTemplateName);
        this.childZipFolder = OPERATING_ROOM.concat("//").concat(childTemplateName);
        this.baseContentTypesXml = new File(baseZipFolder.concat("//[Content_Types].xml"));
        this.childContentTypesXml = new File(childZipFolder.concat("//[Content_Types].xml"));
        this.baseRefXml = new File(baseZipFolder.concat("//word//_rels//document.xml.rels"));
        this.childRefXml = new File(childZipFolder.concat("//word//_rels//document.xml.rels"));
        this.baseStylesXml = new File(baseZipFolder.concat("//word//styles.xml"));
        this.childStylesXml = new File(childZipFolder.concat("//word//styles.xml"));
        this.baseDocumentXml = new File(baseZipFolder.concat("//word//document.xml"));
        this.childDocumentXml = new File(childZipFolder.concat("//word//document.xml"));
        this.baseNumberingXml = new File(baseZipFolder.concat("//word//numbering.xml"));
        this.childNumberingXml = new File(childZipFolder.concat("//word//numbering.xml"));
    }

    //inject

    public void injectContentTypesXml() throws Exception{
        Document baseDoc = DOMOperationUtil.createDocument(baseContentTypesXml);
        Document childDoc = DOMOperationUtil.createDocument(childContentTypesXml);
        Node baseBody = baseDoc.getElementsByTagName("Types").item(0);
        Node childBody = childDoc.getElementsByTagName("Types").item(0);
        NodeList baseTypes = baseBody.getChildNodes();
        NodeList childTypes = childBody.getChildNodes();
        for(int i=0; i<childTypes.getLength(); i++){
            NamedNodeMap attrs = childTypes.item(i).getAttributes();
            Node name;
            if((attrs != null)
                    &&((((name = attrs.getNamedItem("Extension")) != null)||(name = attrs.getNamedItem("PartName")) != null))){
                String expression = String.format("//*[@Extension='%s'] | //*[@PartName='%s']", name.getNodeValue(), name.getNodeValue());
                NodeList leftNodes = XPathParser.getNodes(baseDoc, expression);
                if((leftNodes == null)||(leftNodes.getLength() <= 0)){
                    Node insertedNode = baseDoc.importNode(childTypes.item(i),true);
                    baseBody.appendChild(insertedNode);
                }
            }
        }
        DOMOperationUtil.transform(baseDoc, baseContentTypesXml);
    }

    public void injectMedia() throws Exception {
        if(new File(childZipFolder.concat("//word//media")).exists()) {
            File[] sourceFiles = new File(childZipFolder.concat("//word//media")).listFiles();
            File newMediaFolder = new File(String.format("%s//word//%s", baseZipFolder, "media_".concat(childTemplate)));
            newMediaFolder.mkdir();
            for (File sourceFile : sourceFiles) {
                Files.copy(sourceFile.toPath(),
                        new File(String.format("%s//word//%s//%s", baseZipFolder, "media_".concat(childTemplate), sourceFile.getName())).toPath());
            }
        }
    }

    public void injectReferences() throws Exception {
        Document baseDoc = DOMOperationUtil.createDocument(baseRefXml);
        Document insertedDoc = DOMOperationUtil.createDocument(childRefXml);
        Node baseBody = baseDoc.getElementsByTagName("Relationships").item(0);
        NodeList childRefs = insertedDoc.getElementsByTagName("Relationship");
        for(int i=0; i<childRefs.getLength(); i++){
            NamedNodeMap attrs = childRefs.item(i).getAttributes();
            Node type;
            Node id;
            if((attrs != null)&&((id = attrs.getNamedItem("Id")) != null)&&((type = attrs.getNamedItem("Type")) != null)){
                if(id.getNodeValue().contains("block")){
                    Node insertedNode = baseDoc.importNode(childRefs.item(i),true);
                    baseBody.appendChild(insertedNode);
                }
            }
        }
        DOMOperationUtil.transform(baseDoc, baseRefXml);
    }

    public void injectStyles() throws Exception {
        Document baseDoc = DOMOperationUtil.createDocument(baseStylesXml);
        Document insertedDoc = DOMOperationUtil.createDocument(childStylesXml);
        Node baseBody = baseDoc.getElementsByTagName("w:styles").item(0);
        NodeList childStyles = insertedDoc.getElementsByTagName("w:style");
        for(int i=0; i<childStyles.getLength(); i++){
            Node insertedNode = baseDoc.importNode(childStyles.item(i),true);
            baseBody.appendChild(insertedNode);
        }
        DOMOperationUtil.transform(baseDoc, baseStylesXml);
    }

    public void injectNumbering() throws Exception {
        if((baseNumberingXml.exists())&&(childNumberingXml.exists())) {
            System.out.println("numbering exists");
            Document baseDoc = DOMOperationUtil.createDocument(baseNumberingXml);
            Document insertedDoc = DOMOperationUtil.createDocument(childNumberingXml);
            Node baseBody = baseDoc.getElementsByTagName("w:numbering").item(0);
            NodeList childNumbering = insertedDoc.getElementsByTagName("w:numbering").item(0).getChildNodes();
            for (int i = 0; i < childNumbering.getLength(); i++) {
                Node insertedNode = baseDoc.importNode(childNumbering.item(i), true);
                baseBody.appendChild(insertedNode);
            }
            DOMOperationUtil.transform(baseDoc, baseNumberingXml);
        }else if(childNumberingXml.exists()){
            System.out.println("numbering not exists");
            Files.copy(childNumberingXml.toPath(), baseNumberingXml.toPath());
        }
    }

    public void injectChildParagraphs(Integer pos) throws Exception {
        Document baseDoc = DOMOperationUtil.createDocument(baseDocumentXml);
        Document insertedDoc = DOMOperationUtil.createDocument(childDocumentXml);
        Node baseBody = baseDoc.getElementsByTagName("w:body").item(0);
        Node childBody = insertedDoc.getElementsByTagName("w:body").item(0);
        NodeList bodyParagraphs = baseBody.getChildNodes();
        NodeList childParagraphs = childBody.getChildNodes();
        Node referenceNode = bodyParagraphs.item(pos);
        for(int i=0; i<childParagraphs.getLength(); i++){
            Node insertedNode = baseDoc.importNode(childParagraphs.item(i),true);
            baseBody.insertBefore(insertedNode, referenceNode);
        }
        baseBody.removeChild(referenceNode);
        DOMOperationUtil.transform(baseDoc, baseDocumentXml);
    }

    //rename, возможно стоит объеденить с inject

    public void renameChildImageData() throws Exception {
        Document documentDoc = DOMOperationUtil.createDocument(childDocumentXml);
        NodeList imagesData = XPathParser.getNodes(documentDoc,"//*[local-name()='imagedata']");
        for(int i=0; i<imagesData.getLength(); i++){
            NamedNodeMap attrs = imagesData.item(i).getAttributes();
            Node value;
            if((attrs != null)&&((value = attrs.getNamedItem("r:id")) != null)){
                value.setNodeValue(value.getNodeValue().concat("_").concat(childTemplate));
            }
        }
        DOMOperationUtil.transform(documentDoc, childDocumentXml);
    }

    public void renameChildReferences() throws Exception {
        Document refDoc = DOMOperationUtil.createDocument(childRefXml);
        NodeList refs = refDoc.getElementsByTagName("Relationship");
        for(int i=0; i<refs.getLength(); i++){
            NamedNodeMap attrs = refs.item(i).getAttributes();
            Node type;
            Node id;
            Node target;
            if((attrs != null)&&((id = attrs.getNamedItem("Id")) != null)&&((target = attrs.getNamedItem("Target")) != null)&&((type = attrs.getNamedItem("Type")) != null)){
                if(type.getNodeValue().contains("relationships/image")){
                    id.setNodeValue(id.getNodeValue().concat("_").concat(childTemplate));
                    target.setNodeValue(target.getNodeValue().replace("media", "media_".concat(childTemplate)));
                }else if(type.getNodeValue().contains("relationships/numbering")){
                    //сделать по принципу дополнения
                }
            }
        }
        DOMOperationUtil.transform(refDoc, childRefXml);
    }

    public void renameChildStyleInDocumentXml() throws Exception {
        Document documentDoc = DOMOperationUtil.createDocument(childDocumentXml);
        NodeList styles = XPathParser.getNodes(documentDoc, "//*[local-name()='pStyle'] | //*[local-name()='tblStyle']");
        for(int i=0; i<styles.getLength(); i++){
            NamedNodeMap attrs = styles.item(i).getAttributes();
            Node value;
            if((attrs != null)&&((value = attrs.getNamedItem("w:val")) != null)){
                value.setNodeValue(value.getNodeValue().concat("_").concat(childTemplate));
            }
        }
        DOMOperationUtil.transform(documentDoc, childDocumentXml);
    }

    public void renameChildStyleIdInStyleXml() throws Exception {
        Document styleDoc = DOMOperationUtil.createDocument(childStylesXml);
        NodeList styles = styleDoc.getElementsByTagName("w:style");
        for(int i=0; i<styles.getLength(); i++){
            NamedNodeMap attrs = styles.item(i).getAttributes();
            Node styleId;
            if((attrs != null)&&((styleId = attrs.getNamedItem("w:styleId")) != null)){
                styleId.setNodeValue(styleId.getNodeValue().concat("_").concat(childTemplate));
            }
        }
        DOMOperationUtil.transform(styleDoc, childStylesXml);
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

    public void renameChildNumberingInNumberingXml() throws Exception {
        if(childNumberingXml.exists()) {
            Document numberingDoc = DOMOperationUtil.createDocument(childNumberingXml);
            NodeList abstractNums = numberingDoc.getElementsByTagName("w:abstractNum");
            for (int i = 0; i < abstractNums.getLength(); i++) {
                NamedNodeMap attrs = abstractNums.item(i).getAttributes();
                Node abstractNumId;
                if ((attrs != null) && ((abstractNumId = attrs.getNamedItem("w:abstractNumId")) != null)) {
                    abstractNumId.setNodeValue(childTemplate.replace("block","").concat(abstractNumId.getNodeValue()));
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
                        abstractNumId.setNodeValue(childTemplate.replace("block","").concat(abstractNumId.getNodeValue()));
                    }
                    numId.setNodeValue(childTemplate.replace("block","").concat(numId.getNodeValue()));
                }
            }
            DOMOperationUtil.transform(numberingDoc, childNumberingXml);
        }
    }

    public void renameChildNumberingInDocumentXml() throws Exception {
        if(childNumberingXml.exists()) {
            Document documentDoc = DOMOperationUtil.createDocument(childDocumentXml);
            String expression = "//*[local-name()='p']/*[local-name()='pPr']/*[local-name()='numPr']/*[local-name()='numId']/@*[local-name()='val']";
            NodeList numberingIds = XPathParser.getNodes(documentDoc, expression);
            System.out.println(numberingIds.getLength());
            for(int i=0; i<numberingIds.getLength(); i++){
                System.out.println(1);
                Node numberingId = numberingIds.item(i);
                numberingId.setNodeValue(childTemplate.replace("block","").concat(numberingId.getNodeValue()));
            }
            DOMOperationUtil.transform(documentDoc, childDocumentXml);
        }
    }
}
