package org.doccreator.util;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class DOMOperationUtil {
    public static Document createDocument(File file) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();
        return document;
    }


    public static void transform(Document base, File file) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        base.getDocumentElement().normalize();
        DOMSource source = new DOMSource(base);
        StreamResult result = new StreamResult(file);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
    }
}
