package com.uc4.ara.collabnet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.UnexpectedException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.uc4.ara.collabnet.JArgs.CmdLineParser;
import com.uc4.ara.collabnet.Logging.Logger;
import com.uc4.ara.rm.ImportExportServiceSoap;
import com.uc4.ara.rm.Result;

public abstract class AbstractFeature {

	public abstract int execute(CmdLineParser parser);
	
	public abstract void setParams(CmdLineParser parser);
	
	protected Result handleResult(Result result, ImportExportServiceSoap service)
			throws Exception {

		while (result.getStatus() == 0) {
			// The request is getting processed, wait a bit and ask again
			Thread.sleep(500);
			result = service.getStatus(result.getToken());
		}

		if (result.getErrors() != null
				&& result.getErrors().length > 0) {
			for (com.uc4.ara.rm.Error error : result.getErrors())
				Logger.logMsg("ReleaseManager-Error: "
						+ error.getErrorCode() + ", " + error.getErrorDetail());
			// throw an exception with the first errorcode
			throw new UnexpectedException(result.getErrors()[0].getErrorCode() + ": " + result.getErrors()[0].getErrorTitle() + " - " + result.getErrors()[0].getErrorDetail());
		}

		return result;
	}
	
	protected String getProperty(String name, String value) {
		return getProperty(name, value, false);
	}
	
	protected String getProperty(String name, String value, boolean isIdentity) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("<Property name=\"");
		builder.append(name);
		if(isIdentity)
			builder.append("\" isIdentity=\"true\">");
		else 
			builder.append("\">");
		builder.append("<Value>");
		builder.append(value);
		builder.append("</Value>");
		builder.append("</Property>");
		
		return builder.toString();
	}

	
	public static Document createDocumentFromResult(Result result)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		String fileName = System.getProperty("user.dir") + File.separator
				+ "test_import.xml";
		File newFile = new File(fileName);
		FileWriter writer = new FileWriter(newFile);
		writer.write(result.getData());
		writer.close();
		Document document = builder.parse(newFile);
		newFile.delete();
		return document;
	}
	
	public static Node findNodeByAttribute(Document document, String tagName,
			String attributeName, String attributeValue) throws Exception {
		Node foundNode = null;
		NodeList nodes = document.getElementsByTagName(tagName);
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			for (int j = 0; j < node.getAttributes().getLength(); j++) {
				Node attribute = node.getAttributes().item(j);
				if (attribute.getNodeName().equals(attributeName)
						&& attribute.getNodeValue().equals(attributeValue)) {
					foundNode = node;
					break;
				}
			}
			if (foundNode != null)
				break;
		}
		
		if(foundNode == null)
			throw new Exception("Node '" + tagName + "' with Attribute '" + attributeName + "' = '" + attributeValue + "' was not found.");
		
		return foundNode;
	}

	public static Node findNodeByTagName(Document document, String tagName) throws Exception {
		Node foundNode = null;
		NodeList nodes = document.getElementsByTagName(tagName);
		if (nodes.getLength() > 0)
			foundNode = nodes.item(0);
		
		if(foundNode == null)
			throw new Exception("Node '" + tagName + "' was not found.");
		
		return foundNode;
	}
	
	protected String getNodeValue(Result result, String tagName, String attributeName, String attributeValue) throws Exception {
		Document componentXml= createDocumentFromResult(result);
		Node componentNode = findNodeByAttribute(componentXml, tagName, attributeName, attributeValue);
		String nodeText = componentNode.getTextContent();
		
		if(nodeText == null)
			throw new Exception("Node '" + tagName + "' with Attribute '" + attributeName + "' = '" + attributeValue + "' was not found or has no value.");
		
		return nodeText;
	}
}
