package eg.edu.alexu.csd.oop.db.cs15_cs18_cs49_cs39;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckSyntax {
	private String[] queries = new String[] {
			/*done*/"^(CREATE\\s+DATABASE\\s+)(((\\w+)|(\\w[:(\\\\|\\/)(\\w+)]([\\\\|\\/]\\w+)*?))|(\\w+([\\\\|\\/]\\w+)+))(\\s+)?$",
			/*done*/"^(CREATE\\s+TABLE\\s+)(\\w+)(\\s*?)([(](\\s*?)(\\w+)(\\s+)(int|varchar)(\\s*?)(,(\\s*?)(\\w+)(\\s+)(int|varchar)(\\s*?))*?(\\s*?)[)])(\\s*?)$",
			/*done*/"^(INSERT\\s+INTO\\s+)(\\w+)(\\s*)(([(](\\s*)(\\w+)(\\s*)(,(\\s*)\\w+(\\s*))*?[)])?(\\s+)VALUES(\\s*)[(](\\s*)('((?!'|,).)*?'|(-)?\\d+)(\\s*)(,(\\s*)('((?!'|,).)*?'|(-)?\\d+)(\\s*))*?(\\s*)[)])(\\s*)$",
			/*done*/"^(DELETE\\s+FROM\\s+)(\\w+)(\\s+WHERE\\s+\\w+(\\s*)[=<>](\\s*)('((?!'|,).*)?'|(-)?\\d+))?(\\s*)$",
			/*done*/"^(DROP\\s+DATABASE\\s+)(((\\w+)|(\\w[:(\\\\|\\/)(\\w+)]([\\\\|\\/]\\w+)*?))|(\\w+([\\\\|\\/]\\w+)+))(\\s+)?$",
			/*done*/"^(UPDATE\\s+)(\\w+)(\\s+SET\\s+)(\\w+(\\s*)[=](\\s*)('((?!'|,).)*?'|(-)?\\d+)(\\s*)(,(\\s*?)\\w+(\\s*)[=](\\s*)('((?!'|,).)*?'|(-)?\\d+)\\s*)*?)(\\s+(WHERE)((\\s+)\\w+(\\s*)[=<>](\\s*)('((?!'|,).)*?'|(-)?\\d+)))?(\\s*)$",
			"^(SELECT\\s+)([*]|\\w+)(\\s+FROM\\s+)(\\w+)((\\s+)WHERE(\\s+)\\w+(\\s*?)[=<>](\\s*?)('((?!'|,).)*?'|(-)?\\d+))?(\\s*?)$",
			/*done*/"^(DROP\\s+TABLE\\s+)(\\w+)(\\s*?)$" };

	public int checkSyntax(String query) {
		for (int i = 0; i < queries.length; i++) {
			String pattern = queries[i];
			Pattern p = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
			Matcher regexMatcher = p.matcher(query);
			if (regexMatcher.matches()) {
				return i;
			}
		}
		return -1;
	}

	public String getDataBaseName(String query) {
		if (checkSyntax(query) != -1) {
			Pattern p = Pattern.compile(queries[checkSyntax(query)], Pattern.CASE_INSENSITIVE);
			Matcher regexMatcher = p.matcher(query);
			regexMatcher.find();
			String[] ss = regexMatcher.group(0).split("\\s+");
			String s = ss[0] + " " + ss[1];
			if (s.equalsIgnoreCase("CREATE DATABASE")) {
				return ss[2];
			} else if (s.equalsIgnoreCase("DROP DATABASE")) {
				return ss[2];
			}
		}
		return null;
	}

	public String getTableName(String query) {
		if (checkSyntax(query) != -1) {
			Pattern p = Pattern.compile(queries[checkSyntax(query)], Pattern.CASE_INSENSITIVE);
			Matcher regexMatcher = p.matcher(query);
			regexMatcher.find();
			String[] ss = regexMatcher.group(0).split("\\s+");
			if (ss[0].equalsIgnoreCase("CREATE") && ss[1].equalsIgnoreCase("TABLE")) {
				return regexMatcher.group(2);
			} else if (ss[0].equalsIgnoreCase("INSERT") && ss[1].equalsIgnoreCase("INTO")) {
				return regexMatcher.group(2);
			} else if (ss[0].equalsIgnoreCase("DELETE") && ss[1].equalsIgnoreCase("FROM")) {
				return regexMatcher.group(2);
			} else if (ss[0].equalsIgnoreCase("UPDATE")) {
				return regexMatcher.group(2);
			} else if (ss[0].equalsIgnoreCase("SELECT")) {
				return regexMatcher.group(4);
			} else if (ss[0].equalsIgnoreCase("DROP") && ss[1].equalsIgnoreCase("TABLE")) {
				return regexMatcher.group(2);
			}
		}
		return null;
	}

	public String[] getColumnsName(String query) {
		if (checkSyntax(query) != -1) {
			Pattern p = Pattern.compile(queries[checkSyntax(query)], Pattern.CASE_INSENSITIVE);
			Matcher regexMatcher = p.matcher(query);
			regexMatcher.find();
			String[] ss = regexMatcher.group(0).split("\\s+");
			if (ss[0].equalsIgnoreCase("CREATE") && ss[1].equalsIgnoreCase("TABLE")) {
				String s = regexMatcher.group(4).substring(1, regexMatcher.group(4).length()-1);
				String[] columns = s.split(",");
				String[] columnsName = new String[columns.length];
				for (int i = 0; i < columns.length; i++) {
					columns[i] = columns[i].trim();
					columnsName[i] = columns[i].split("\\s+")[0].toLowerCase();
				}
				return columnsName;
			} else if (ss[0].equalsIgnoreCase("SELECT")) {
				if (regexMatcher.group(2).equals("*")) {
					return new String[] { "all" };
				} else {
					return new String[] { regexMatcher.group(2) };
				}
			} else if (ss[0].equalsIgnoreCase("INSERT") && ss[1].equalsIgnoreCase("INTO")) {
				String[] columns = regexMatcher.group(4).split("(?i)VALUES");
				if (columns[0].trim().equalsIgnoreCase("")) {
					return new String[] { "all" };
				} else {
					String[] columnsName = columns[0].split(",");
					for(int i=0;i<columnsName.length;i++) {
						columnsName[i] = columnsName[i].trim().toLowerCase();
					}
					columnsName[0] = columnsName[0].substring(1, columnsName[0].length()).trim();
					columnsName[columnsName.length - 1] = columnsName[columnsName.length - 1].substring(0,
							columnsName[columnsName.length - 1].length() - 1).trim();
					return columnsName;
				}
			} else if (ss[0].equalsIgnoreCase("UPDATE")) {
				String[] columns = regexMatcher.group(4).split(",");
				String[] columnsName = new String[columns.length];
				for (int i = 0; i < columnsName.length; i++) {
					columnsName[i] = columns[i].split("=")[0].trim().toLowerCase();
				}
				return columnsName;
			}else if(ss[0].equalsIgnoreCase("DELETE") && ss[1].equalsIgnoreCase("FROM")) {
				String[] q = query.split("\\s+");
				if(q.length > 4) {
					String[] columns = regexMatcher.group(3).split("(?i)WHERE")[1].split("=");
					String columnName = columns[0].trim().toLowerCase();
					return new String[] {columnName};
				}else {
					return new String[] {"all"};
				}
				
			}
		}
		return null;
	}

	public String[] getDataType(String query) {
		if (checkSyntax(query) != -1) {
			Pattern p = Pattern.compile(queries[checkSyntax(query)], Pattern.CASE_INSENSITIVE);
			Matcher regexMatcher = p.matcher(query);
			regexMatcher.find();
			String[] ss = regexMatcher.group(0).split("\\s+");
			if (ss[0].equalsIgnoreCase("CREATE") && ss[1].equalsIgnoreCase("TABLE")) {
				String s = regexMatcher.group(4).substring(1, regexMatcher.group(4).length()-1);
				String[] types = s.split(",");
				String[] dataTypes = new String[types.length];
				for (int i = 0; i < types.length; i++) {
					types[i] = types[i].trim();
					dataTypes[i] = types[i].split("\\s+")[1].toLowerCase();
				}
				return dataTypes;
			}
		}
		return null;
	}
	
	public String[] getValues(String query) {
		Pattern p = Pattern.compile(queries[checkSyntax(query)], Pattern.CASE_INSENSITIVE);
		Matcher regexMatcher = p.matcher(query);
		regexMatcher.find();
		String[] ss = regexMatcher.group(0).split("\\s+");
		if(ss[0].equalsIgnoreCase("INSERT") && ss[1].equalsIgnoreCase("INTO")) {
			String[] vals = regexMatcher.group(4).split("(?i)VALUES");
			String v = vals[vals.length-1].trim();
			v = v.substring(1, v.length()-1);
			String[] values = v.split(",");
			for(int i=0;i<values.length;i++) {
				values[i] = values[i].trim().toLowerCase();
			}
			return values;
		}else if(ss[0].equalsIgnoreCase("UPDATE")) {
			String[] vals = regexMatcher.group(4).split(",");
			String[] values = new String[vals.length];
			for(int i=0;i<values.length;i++) {
				values[i] = vals[i].split("=")[1].trim().toLowerCase();
			}
			return values;
		}
		return null;
		
	}
	
	public String getCondition(String query) {
		Pattern p = Pattern.compile(queries[checkSyntax(query)], Pattern.CASE_INSENSITIVE);
		Matcher regexMatcher = p.matcher(query);
		regexMatcher.find();
		String[] ss = regexMatcher.group(0).split("\\s+");
		if(ss[0].equalsIgnoreCase("UPDATE")) {
			try {
				String condition = regexMatcher.group(20).trim();
				condition = condition.trim();
				condition = condition.replaceAll("\\s+", "");
				return condition.toLowerCase();
			} catch (Exception e) {
				return null;
			}
		}else if(ss[0].equalsIgnoreCase("DELETE") && ss[1].equalsIgnoreCase("FROM")) {
			try {
				String condition = regexMatcher.group(3).trim();
				condition = condition.substring(5, condition.length()).trim();
				condition = condition.replaceAll("\\s+", "");
				condition = condition.replaceAll("\\s+", "");
				return condition.toLowerCase();
			} catch (Exception e) {
				return null;
			}
		}else if(ss[0].equalsIgnoreCase("SELECT")) {
			try {
				String condition = regexMatcher.group(5).trim();
				condition = condition.substring(5, condition.length()).trim();
				condition = condition.replaceAll("\\s+", "");
				condition = condition.replaceAll("\\s+", "");
				return condition.toLowerCase();
			} catch (Exception e) {
				return null;
			}
		}
		return null;
	}

}
