package org.ka3wo.xpath;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class XMLUtil {

    private Document loadDocument(String resourcePath) throws Exception {
        File file = new File(resourcePath);
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
        dbf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        dbf.setNamespaceAware(false);
        dbf.setExpandEntityReferences(false);
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");

        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(file);
    }

    public List<String> extractAll(String resourcePath, String expression, String username) throws Exception {
        Document doc = loadDocument(resourcePath);

        XPathFactory xpf = XPathFactory.newInstance();
        XPath xPath = xpf.newXPath();

        xPath.setXPathVariableResolver(qName -> {
            if (qName == null) return null;
            return "username".equals(qName.getLocalPart()) ? username : null;
        });

        XPathExpression expr = xPath.compile(expression);
        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

        List<String> results = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node userNode = nodes.item(i);
            String user = xPath.evaluate("username", userNode);
            String password = xPath.evaluate("password", userNode);
            String role = xPath.evaluate("role", userNode);
            results.add("<Username: " + user + ", password: " + password + ", role: " + role + ">");
        }
        return results;
    }

    public List<String> extractAll(String resourcePath, String expression) throws Exception {
        Document doc = loadDocument(resourcePath);

        XPathFactory xpf = XPathFactory.newInstance();
        XPath xPath = xpf.newXPath();

        XPathExpression expr = xPath.compile(expression);

        NodeList nodes = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);

        List<String> results = new ArrayList<>();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node userNode = nodes.item(i);
            String user = xPath.evaluate("username", userNode);
            String password = xPath.evaluate("password", userNode);
            String role = xPath.evaluate("role", userNode);
            results.add("<Username: " + user + ", password: " + password + ", role: " + role + ">");
        }
        return results;
    }
}
