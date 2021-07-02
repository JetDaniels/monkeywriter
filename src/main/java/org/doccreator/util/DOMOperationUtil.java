package org.doccreator.util;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;

public class DOMOperationUtil {
    public static Document createDocument(InputStream file) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(file);
        document.getDocumentElement().normalize();
        return document;
    }

    public static InputStream transform(Document base, OutputStream xml) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        base.getDocumentElement().normalize();
        DOMSource source = new DOMSource(base);
        StreamResult result = new StreamResult(xml);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, result);
        ByteArrayOutputStream os = (ByteArrayOutputStream) result.getOutputStream();
        return new ByteArrayInputStream(os.toByteArray());
    }

}
