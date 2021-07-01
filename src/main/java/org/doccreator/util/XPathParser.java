package org.doccreator.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.*;

public class XPathParser {

    public static NodeList getNodes(Document source, String expression) throws Exception {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression xPathExpression = xPath.compile(expression);
        return (NodeList) xPathExpression.evaluate(source, XPathConstants.NODESET);
    }

    public static Node getNode(Document source, String expression) throws Exception {
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();
        XPathExpression xPathExpression = xPath.compile(expression);
        NodeList result = (NodeList) xPathExpression.evaluate(source, XPathConstants.NODESET);
        return result.item(0);
    }

}

