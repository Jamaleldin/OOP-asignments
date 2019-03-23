package eg.edu.alexu.csd.oop.db.cs15_cs18_cs49_cs39;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eg.edu.alexu.csd.oop.db.Query;

public class CQuery implements Query {
	private XMLForTable xmlWriter;

	public CQuery() {
		xmlWriter = new XMLForTable();
	}

	@Override
	public boolean createDataBase(String databaseName) {
		File file;
		if (databaseName.matches("^((\\\\w[:(\\\\|\\/)(\\w+)]([\\\\|\\/]\\w+)*?)|(\\w+([\\\\|\\/]\\w+)+))$")) {
			file = new File(databaseName);
		} else {
			file = new File("DB" + System.getProperty("file.separator") + databaseName);
		}
		if (!file.exists()) {
			file.mkdirs();
		}
		return true;
	}

	@Override
	public boolean createTable(String databaseName, String tableName, String[] columns, String[] dataTypes)
			throws SQLException {
		String path;
		if (databaseName.matches("^((\\w[:(\\\\|\\/)(\\w+)]([\\\\|\\/]\\w+)*?)|(\\w+([\\\\|\\/]\\w+)+))$")) {
			path = databaseName + System.getProperty("file.separator") + tableName + ".xml";
		} else {
			path = "DB" + System.getProperty("file.separator") + databaseName + System.getProperty("file.separator")
					+ tableName + ".xml";
		}
		if (new File(path).exists()) {
			return false;
		}
		if (!new File(path.substring(0, path.lastIndexOf(System.getProperty("file.separator")))).exists()) {
			throw new SQLException();
		}
		xmlWriter.writeDTD(databaseName, tableName, columns, dataTypes);
		xmlWriter.writeXmlForTable(databaseName, tableName);
		return true;
	}

	@Override
	public void insertIntoTable(String databaseName, String tableName, String[] columns, String[] values)
			throws ParserConfigurationException, SAXException, IOException, TransformerException, SQLException {
		if (values.length != columns.length && !columns[0].equals("all")) {
			throw new SQLException();
		} else {
			String[] allCols = xmlWriter.readDTDCols(databaseName, tableName);
			String[] allTypes = xmlWriter.readDTDTypes(databaseName, tableName);
			String[] allValues = new String[allCols.length];
			if (columns[0].equals("all")) {
				if (values.length != allCols.length) {
					throw new SQLException();
				}
				allValues = values;
			} else {
				for (int i = 0; i < allCols.length; i++) {
					if (allTypes[i].equals("int")) {
						allValues[i] = "0";
					} else {
						allValues[i] = "'null'";
					}
				}
				int counter = 0;
				for (int i = 0; i < columns.length; i++) {
					for (int j = 0; j < allCols.length; j++) {
						if (columns[i].equals(allCols[j])) {
							counter++;
						}
					}
				}
				if (counter != columns.length) {
					throw new SQLException();
				}
				for (int i = 0; i < columns.length; i++) {
					for (int j = 0; j < allCols.length; j++) {
						if (columns[i].equals(allCols[j])) {
							if (allTypes[j].equals("int")) {
								for (int k = 0; k < values[i].length(); k++) {
									Character c = values[i].charAt(k);
									if (c < '0' || c > '9') {
										throw new SQLException();
									}
								}
								allValues[j] = values[i];
							} else if (allTypes[j].equals("varchar")) {
								if (values[i].charAt(0) != '\'' || values[i].charAt(values[i].length() - 1) != '\'') {
									throw new SQLException();
								}
								allValues[j] = values[i];
							}
						}
					}
				}
			}
			Document document = xmlWriter.getDocument(databaseName, tableName);
			Element root = document.getDocumentElement();
			Element newRow = document.createElement("row");
			for (int i = 0; i < allCols.length; i++) {
				Element newCol = document.createElement(allCols[i]);
				newCol.appendChild(document.createTextNode(allValues[i]));
				newCol.setAttribute("type", allTypes[i]);
				newRow.appendChild(newCol);
			}
			root.appendChild(newRow);
			xmlWriter.transformer(document, databaseName, tableName);
		}
	}

	@Override
	public int deleteFromTable(String databaseName, String tableName, String condition) throws SQLException {
		int count = 0;
		String con = null;
		String operand = null;
		String value = null;
		if (condition != null) {
			Pattern p = Pattern.compile("(\\w+)([<>=])('((?!'|,).*)?'|\\d+)");
			Matcher m = p.matcher(condition);
			if (m.find()) {
				con = m.group(1);
				operand = m.group(2);
				value = m.group(3);
			}
		}
		try {
			if (condition == null) {
				Document document = xmlWriter.getDocument(databaseName, tableName);
				Element root = document.getDocumentElement();
				NodeList rows = document.getElementsByTagName("row");
				for (int i = 0; i < rows.getLength(); i++) {
					root.removeChild(rows.item(i));
					count++;
					i--;
				}
				try {
					xmlWriter.transformer(document, databaseName, tableName);
				} catch (TransformerException e) {
					throw new SQLException();
				}
			} else {
				Document document = xmlWriter.getDocument(databaseName, tableName);
				Element root = document.getDocumentElement();
				NodeList columns = document.getElementsByTagName(con);
				NodeList rows = document.getElementsByTagName("row");
				for (int i = 0; i < columns.getLength(); i++) {
					Node column = columns.item(i);
					if (column.getNodeType() == Node.ELEMENT_NODE) {
						Element co = (Element) column;
						if (operand.equals("=")) {
							if (co.getTextContent().equals(value)) {
								root.removeChild(rows.item(i));
								count++;
								i--;
							}
						} else if (operand.equals("<")) {
							if (co.getTextContent().compareTo(value) < 0) {
								root.removeChild(rows.item(i));
								count++;
								i--;
							}
						} else if (operand.equals(">")) {
							if (co.getTextContent().compareTo(value) > 0) {
								root.removeChild(rows.item(i));
								count++;
								i--;
							}
						}
					}
				}
				try {
					xmlWriter.transformer(document, databaseName, tableName);
				} catch (TransformerException e) {
					throw new SQLException();
				}
			}
		} catch (ParserConfigurationException e2) {
			throw new SQLException();
		} catch (SAXException e2) {
			throw new SQLException();
		}
		return count;

	}

	@Override
	public boolean dropDataBase(String databaseName) {
		File folder;
		if (databaseName.matches("^((\\w[:(\\\\|\\/)(\\w+)]([\\\\|\\/]\\w+)*?)|(\\w+([\\\\|\\/]\\w+)+))$")) {
			folder = new File(databaseName);
		} else {
			folder = new File("DB" + System.getProperty("file.separator") + databaseName);
		}
		if (!folder.exists()) {
			return false;
		}
		for (File c : folder.listFiles()) {
			c.delete();
			if (c.isDirectory()) {
				dropDataBase(c.getPath());
			}
		}
		folder.delete();
		return true;
	}

	@Override
	public boolean dropTable(String databaseName, String tableName) {
		File file;
		if (databaseName.matches("^((\\w[:(\\\\|\\/)(\\w+)]([\\\\|\\/]\\w+)*?)|(\\w+([\\\\|\\/]\\w+)+))$")) {
			file = new File(databaseName + System.getProperty("file.separator") + tableName + ".xml");
		} else {
			file = new File("DB" + System.getProperty("file.separator") + databaseName
					+ System.getProperty("file.separator") + tableName + ".xml");
		}
		if (!file.exists()) {
			return false;
		}
		file.delete();
		if (databaseName.matches("^((\\w[:(\\\\|\\/)(\\w+)]([\\\\|\\/]\\w+)*?)|(\\w+([\\\\|\\/]\\w+)+))$")) {
			file = new File(databaseName + "/" + tableName + ".DTD");
		} else {
			file = new File("DB" + System.getProperty("file.separator") + databaseName
					+ System.getProperty("file.separator") + tableName + ".DTD");
		}
		file.delete();
		return true;
	}

	@Override
	public String[][] selectFromTable(String dataBaseName, String tableName, String column, String condition)
			throws SQLException {

		String con = null;
		String operand = null;
		String value = null;
		boolean flag = false;
		if (condition != null) {
			Pattern p = Pattern.compile("(\\w+)([<>=])('((?!'|,).*)?'|\\d+)");
			Matcher m = p.matcher(condition);
			if (m.find()) {
				con = m.group(1);
				operand = m.group(2);
				value = m.group(3);
			}
			for (int i = 0; i < value.length(); i++) {
				Character c = value.charAt(i);
				if (c > '0' && c < '9') {
					flag = true;
					continue;
				} else {
					flag = false;
					break;
				}
			}
		}
		LinkedList<String[]> a = new LinkedList<String[]>();
		try {

			Document document = xmlWriter.getDocument(dataBaseName, tableName);
			document.getDocumentElement().normalize();
			NodeList nList = document.getElementsByTagName("row");

			if (column.equals("all") && condition == null) {
				if (readAllTable(dataBaseName, tableName).equals(null)) {
					return null;
				} else {
					return linkedToArray(readAllTable(dataBaseName, tableName));

				}
			}
			for (int i = 0; i < nList.getLength(); i++) {
				Node node = nList.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					if (condition == null) {
						String s[] = new String[1];
						Element e = (Element) eElement.getElementsByTagName(column).item(0);
						s[0] = e.getTextContent();
						a.add(s);
					} else {
						if (column.equals("all")) {

							if (operand.equals("=")) {
								Element e = (Element) eElement.getElementsByTagName(con);
								if (e.getTextContent().equals(value)) {

									NodeList cList = eElement.getChildNodes();
									String[] s = new String[cList.getLength()];
									for (int j = 0; j < cList.getLength(); j++) {
										Node c = cList.item(j);
										if (c.getNodeType() == Node.ELEMENT_NODE) {
											Element col = (Element) c;
											s[j] = col.getTextContent();
										}
									}
									a.add(s);

								}
							} else if (operand.equals(">")) {
								Element e = (Element) eElement.getElementsByTagName(con).item(0);
								if (flag) {
									if (Integer.parseInt(e.getTextContent()) > Integer.parseInt(value)) {

										NodeList cList = eElement.getChildNodes();
										String[] s = new String[cList.getLength()];
										for (int j = 0; j < cList.getLength(); j++) {
											Node c = cList.item(j);
											if (c.getNodeType() == Node.ELEMENT_NODE) {
												Element col = (Element) c;
												s[j] = col.getTextContent();
											}
										}
										a.add(s);

									}
								} else {
									if (e.getTextContent().compareTo(value) > 0) {

										NodeList cList = eElement.getChildNodes();
										String[] s = new String[cList.getLength()];
										for (int j = 0; j < cList.getLength(); j++) {
											Node c = cList.item(j);
											if (c.getNodeType() == Node.ELEMENT_NODE) {
												Element col = (Element) c;
												s[j] = col.getTextContent();
											}
										}
										a.add(s);

									}
								}
							} else {
								Element e = (Element) eElement.getElementsByTagName(con).item(0);
								if (flag) {
									if (Integer.parseInt(e.getTextContent()) < Integer.parseInt(value)) {

										NodeList cList = eElement.getChildNodes();
										String[] s = new String[cList.getLength()];
										for (int j = 0; j < cList.getLength(); j++) {
											Node c = cList.item(j);
											if (c.getNodeType() == Node.ELEMENT_NODE) {
												Element col = (Element) c;
												s[j] = col.getTextContent();
											}
										}
										a.add(s);
									}
								} else {
									if (e.getTextContent().compareTo(value) < 0) {

										NodeList cList = eElement.getChildNodes();
										String[] s = new String[cList.getLength()];
										for (int j = 0; j < cList.getLength(); j++) {
											Node c = cList.item(j);
											if (c.getNodeType() == Node.ELEMENT_NODE) {
												Element col = (Element) c;
												s[j] = col.getTextContent();
											}
										}
										a.add(s);
									}
								}
							}

						} else {
							if (operand.equals("=")) {
								Element e = (Element) eElement.getElementsByTagName(con).item(0);
								if (e.getTextContent().equals(value)) {

									String[] s = new String[1];
									Element e1 = (Element) eElement.getElementsByTagName(column).item(0);
									s[0] = e1.getTextContent();
									a.add(s);

								}
							} else if (operand.equals(">")) {
								Element e = (Element) eElement.getElementsByTagName(con).item(0);
								if (flag) {
									if (Integer.parseInt(e.getTextContent()) > Integer.parseInt(value)) {

										String[] s = new String[1];
										Element e1 = (Element) eElement.getElementsByTagName(column).item(0);
										s[0] = e1.getTextContent();
										a.add(s);

									}
								} else {
									if (e.getTextContent().compareTo(value) > 0) {

										String[] s = new String[1];
										Element e1 = (Element) eElement.getElementsByTagName(column).item(0);
										s[0] = e1.getTextContent();
										a.add(s);

									}
								}

							} else {
								Element e = (Element) eElement.getElementsByTagName(con).item(0);
								if (flag) {
									if (Integer.parseInt(e.getTextContent()) < Integer.parseInt(value)) {

										String[] s = new String[1];
										Element e1 = (Element) eElement.getElementsByTagName(column).item(0);
										s[0] = e1.getTextContent();
										a.add(s);
									}
								} else {
									if (e.getTextContent().compareTo(value) < 0) {

										String[] s = new String[1];
										Element e1 = (Element) eElement.getElementsByTagName(column).item(0);
										s[0] = e1.getTextContent();
										a.add(s);
									}

								}

							}
						}
					}
				}

			}

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return linkedToArray(a);

	}

	private String[][] linkedToArray(LinkedList<String[]> s) {
		String[] row = s.get(0);
		String[][] a = new String[s.size()][row.length];
		for (int i = 0; i < s.size(); i++) {
			row = s.get(i);
			for (int j = 0; j < row.length; j++) {
				a[i][j] = row[j];
			}
		}
		return a;
	}

	private LinkedList<String[]> readAllTable(String dataBaseName, String tableName) throws SQLException {
		String[] columnsName = xmlWriter.readDTDCols(dataBaseName, tableName);
		LinkedList<String[]> elements = new LinkedList<String[]>();
		try {
			Document document = xmlWriter.getDocument(dataBaseName, tableName);
			NodeList rows = document.getElementsByTagName("row");
			for (int i = 0; i < rows.getLength(); i++) {
				Node row = rows.item(i);
				String[] s = new String[columnsName.length];
				if (row.getNodeType() == Node.ELEMENT_NODE) {
					Element ro = (Element) row;
					NodeList columns = ro.getChildNodes();
					for (int j = 0; j < columns.getLength(); j++) {
						Node column = columns.item(j);
						if (column.getNodeType() == Node.ELEMENT_NODE) {
							Element col = (Element) column;
							s[j] = col.getTextContent();
						}
					}
					elements.add(s);
				}
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return elements;
	}

	@Override
	public int updateTable(String dataBaseName, String tableName, String[] columns, String[] vlaues, String condition)
			throws ParserConfigurationException, SAXException, TransformerException, SQLException {
		String[] allCols = xmlWriter.readDTDCols(dataBaseName, tableName);
		String[] allTypes = xmlWriter.readDTDTypes(dataBaseName, tableName);
		for (int i = 0; i < columns.length; i++) {
			for (int j = 0; j < allCols.length; j++) {
				if (columns[i].equals(allCols[j])) {
					if (vlaues[i].contains("'")) {
						if (allTypes[j].equals("varchar")) {
							continue;
						} else {
							throw new SQLException();
						}
					} else {
						if (allTypes[j].equals("int")) {
							continue;
						} else {
							throw new SQLException();
						}
					}
				}
			}
		}
		Document document = xmlWriter.getDocument(dataBaseName, tableName);
		document.getDocumentElement().normalize();
		NodeList rows = document.getElementsByTagName("row");
		int count = 0;
		if (condition == null) {

			for (int i = 0; i < rows.getLength(); i++) {
				Element row = null;
				for (int j = 0; j < columns.length; j++) {
					row = (Element) rows.item(i);
					Node col = row.getElementsByTagName(columns[j]).item(0);
					col.setTextContent(vlaues[j]);
				}
				count++;
			}
		} else {

			String[] cond1 = condition.split("[=<>]");
			String colName = cond1[0];
			String condValue = cond1[1];

			for (int j = 0; j < allCols.length; j++) {
				if (allCols[j].equals(colName)) {
					if (condValue.contains("'")) {
						if (allTypes[j].equals("varchar")) {
							continue;
						} else {
							throw new SQLException();
						}
					} else {
						if (allTypes[j].equals("int")) {
							continue;
						} else {
							throw new SQLException();
						}
					}
				}
			}

			if (condition.matches("\\w+[=]('((?!'|,).*)?'|\\d+)")) {
				for (int i = 0; i < rows.getLength(); i++) {
					Element row = null;
					row = (Element) rows.item(i);
					Node cond = row.getElementsByTagName(colName).item(0);
					if (cond.getTextContent().equals(condValue)) {
						for (int j = 0; j < columns.length; j++) {
							Node col = row.getElementsByTagName(columns[j]).item(0);
							col.setTextContent(vlaues[j]);
						}
						count++;
					}
				}
			} else if (condition.matches("\\w+[<]('((?!'|,).*)?'|\\d+)")) {
				for (int i = 0; i < rows.getLength(); i++) {
					Element row = null;
					row = (Element) rows.item(i);
					Node cond = row.getElementsByTagName(colName).item(0);
					if (cond.getTextContent().compareTo(condValue) < 0) {
						for (int j = 0; j < columns.length; j++) {
							Node col = row.getElementsByTagName(columns[j]).item(0);
							col.setTextContent(vlaues[j]);
						}
						count++;
					}
				}
			} else if (condition.matches("\\w+[>]('((?!'|,).*)?'|\\d+)")) {
				for (int i = 0; i < rows.getLength(); i++) {
					Element row = null;
					row = (Element) rows.item(i);
					Node cond = row.getElementsByTagName(colName).item(0);
					if (cond.getTextContent().compareTo(condValue) > 0) {
						for (int j = 0; j < columns.length; j++) {
							Node col = row.getElementsByTagName(columns[j]).item(0);
							col.setTextContent(vlaues[j]);
						}
						count++;
					}
				}
			}
		}
		xmlWriter.transformer(document, dataBaseName, tableName);
		return count;

	}

}
