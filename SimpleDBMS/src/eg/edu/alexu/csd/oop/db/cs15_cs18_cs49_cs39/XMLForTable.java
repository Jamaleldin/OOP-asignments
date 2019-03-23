package eg.edu.alexu.csd.oop.db.cs15_cs18_cs49_cs39;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class XMLForTable {
	public void writeDTD(String databaseName, String tableName, String[] columns, String[] types) {
		StringBuilder s = new StringBuilder();
		s.append("<!ELEMENT " + tableName + " (row)*>\n");
		s.append("<!ELEMENT row (");
		for (int i = 0; i < columns.length; i++) {
			s.append(columns[i]);
			if (i != columns.length - 1) {
				s.append(",");
			}
		}
		s.append(")>\n");
		s.append("<!ATTLIST row num ID #REQUIRED>\n");
		for (int i = 0; i < columns.length; i++) {
			s.append("<!ELEMENT " + columns[i] + " (#PCDATA)>\n");
			s.append("<!ATTLIST " + columns[i] + " type CDATA #FIXED " + "\"" + types[i] + "\"" + ">\n");
		}
		try {
			BufferedWriter writer;
			if (databaseName.matches("^((\\w[:(\\\\|\\/)(\\w+)]([\\\\|\\/]\\w+)*?)|(\\w+([\\\\|\\/]\\w+)+))$")) {
				writer = new BufferedWriter(new FileWriter(
						new File(databaseName + System.getProperty("file.separator") + tableName + ".DTD")));
			} else {
				writer = new BufferedWriter(new FileWriter(new File("DB" + System.getProperty("file.separator")
						+ databaseName + System.getProperty("file.separator") + tableName + ".DTD")));
			}

			writer.write(s.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void writeXmlForTable(String databaseName, String tableName) {
		DocumentBuilderFactory factroy = DocumentBuilderFactory.newInstance();

		try {
			DocumentBuilder builder = factroy.newDocumentBuilder();
			Document document = builder.newDocument();
			Element element = document.createElement(tableName);
			document.appendChild(element);
			DOMSource source = new DOMSource(document);
			TransformerFactory transformFactory = TransformerFactory.newInstance();
			Transformer transformer = transformFactory.newTransformer();
			StreamResult result;
			if (databaseName.matches("^((\\w[:(\\\\|\\/)(\\w+)]([\\\\|\\/]\\w+)*?)|(\\w+([\\\\|\\/]\\w+)+))$")) {
				result = new StreamResult(new File(databaseName + "/" + tableName + ".XML"));
			} else {
				result = new StreamResult(new File("DB/" + databaseName + "/" + tableName + ".XML"));
			}
			DOMImplementation domImpl = document.getImplementation();
			DocumentType doctype = domImpl.createDocumentType("doctype", "", tableName + ".dtd");
			transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
			transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
			transformer.transform(source, result);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Document getDocument(String databaseName, String tableName)
			throws ParserConfigurationException, SAXException, SQLException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document;
		if (databaseName.matches("^((\\w[:(\\\\|\\/)(\\w+)]([\\\\|\\/]\\w+)*?)|(\\w+([\\\\|\\/]\\w+)+))$")) {
			try {
				document = documentBuilder.parse(databaseName + "/" + tableName + ".xml");
			} catch (IOException e) {
				throw new SQLException();
			}
			return document;
		} else {
			try {
				document = documentBuilder.parse("DB/" + databaseName + "/" + tableName + ".xml");
			} catch (IOException e) {
				throw new SQLException();
			}
			return document;

		}

	}

	public void transformer(Document doc, String databaseName, String tableName) throws TransformerException {
		DOMSource source = new DOMSource(doc);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		StreamResult result;
		if (databaseName.matches("^((\\w[:(\\\\|\\/)(\\\\w+)]([\\\\|\\/]\\\\w+)*?)|(\\w+([\\\\|\\/]\\w+)+))$")) {
			result = new StreamResult(new File(databaseName + "/" + tableName + ".xml"));
		} else {
			result = new StreamResult(new File("DB/" + databaseName + "/" + tableName + ".xml"));
		}
		DOMImplementation domImpl = doc.getImplementation();
		DocumentType doctype = domImpl.createDocumentType("doctype", "", tableName + ".dtd");
		transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, doctype.getPublicId());
		transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
		transformer.transform(source, result);
	}

	public String[] readDTDCols(String databaseName, String tableName) throws SQLException {
		LinkedList<String> elements = new LinkedList<String>();
		String path;
		if (databaseName.matches("^((\\w[:(\\\\|\\/)(\\w+)]([\\\\|\\/]\\w+)*?)|(\\w+([\\\\|\\/]\\w+)+))$")) {
			path = databaseName + System.getProperty("file.separator") + tableName + ".DTD";
		} else {
			path = "DB" + System.getProperty("file.separator") + databaseName + System.getProperty("file.separator")
					+ tableName + ".DTD";
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));

			String line;
			while ((line = br.readLine()) != null) {
				String regex = "^<!ELEMENT\\s(\\w+)\\s\\(#PCDATA\\)>$";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(line);
				if (m.find()) {
					elements.add(m.group(1));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			throw new SQLException();
		} catch (IOException e) {
			throw new SQLException();
		}
		String[] array = new String[elements.size()];
		for (int i = 0; i < elements.size(); i++) {
			array[i] = elements.get(i);
		}
		return array;
	}

	public String[] readDTDTypes(String databaseName, String tableName) throws SQLException {
		LinkedList<String> elements = new LinkedList<String>();
		String path;
		if (databaseName.matches("^((\\w[:(\\\\|\\/)(\\w+)]([\\\\|\\/]\\\\w+)*?)|(\\w+([\\\\|\\/]\\w+)+))$")) {
			path = databaseName + System.getProperty("file.separator") + tableName + ".DTD";
		} else {
			path = "DB" + System.getProperty("file.separator") + databaseName + System.getProperty("file.separator")
					+ tableName + ".DTD";
		}
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(path)));

			String line;
			while ((line = br.readLine()) != null) {
				String regex = "<!ATTLIST (\\w+) type CDATA #FIXED \"(int|varchar)\">$";
				Pattern p = Pattern.compile(regex);
				Matcher m = p.matcher(line);
				if (m.find()) {
					elements.add(m.group(2));
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			throw new SQLException();
		} catch (IOException e) {
			throw new SQLException();
		}
		String[] array = new String[elements.size()];
		for (int i = 0; i < elements.size(); i++) {
			array[i] = elements.get(i);
		}
		return array;
	}

}
