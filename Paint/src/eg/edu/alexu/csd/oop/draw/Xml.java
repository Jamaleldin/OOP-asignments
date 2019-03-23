package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Xml {
	public void save(String path, Shape[] shapes) throws IOException {
		File f = new File(path);
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("<paint>");
		bw.newLine();
		for (int i = 0; i < shapes.length; i++) {
			bw.write("<shape id=\"" + shapes[i].getClass().getName() + "\">");
			bw.newLine();
			if (shapes[i].getPosition() != null) {
				Point position = (Point) shapes[i].getPosition();
				bw.write("<x>" + position.x + "</x>");
				bw.newLine();
				bw.write("<y>" + position.y + "</y>");
				bw.newLine();
			} else {
				bw.write("<x>" + -1 + "</x>");
				bw.newLine();
				bw.write("<y>" + -1 + "</y>");
				bw.newLine();
			}
			bw.write("<map>");
			bw.newLine();
			HashMap<String, Double> pro = (HashMap<String, Double>) shapes[i].getProperties();
			if (pro!=null) {
				Set<String> keys = pro.keySet();
				for (String s : keys) {
					bw.write("<" + s + ">" + pro.get(s) + "</" + s + ">");
					bw.newLine();
				}
			} else {
				bw.write("<key>" + -1 + "</key>");
				bw.newLine();
			}
			bw.write("</map>");
			bw.newLine();
			if (shapes[i].getColor() != null) {
				Color color = (Color) shapes[i].getColor();
				bw.write("<color>" + color.getRGB() + "</color>");
				bw.newLine();
			} else {
				bw.write("<color>" + -1 + "</color>");
				bw.newLine();
			}
			if (shapes[i].getFillColor() != null) {
				Color color = (Color) shapes[i].getFillColor();
				bw.write("<fillcolor>" + color.getRGB() + "</fillcolor>");
				bw.newLine();
			} else {
				bw.write("<fillcolor>" + -1 + "</fillcolor>");
				bw.newLine();
			}
			bw.write("</shape>");
			bw.newLine();
		}
		bw.write("</paint>");
		bw.close();
		fw.close();
	}

	@SuppressWarnings({ "deprecation", "resource" })
	public ArrayList<Shape> load(String path)
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		ArrayList<Shape> sh = new ArrayList<>();
		File f = new File(path);
		FileReader fr = new FileReader(f);
		BufferedReader br = new BufferedReader(fr);
		br.readLine();
		String s;
		while ((s = br.readLine()) != null && !s.equals("</paint>")) {
			File file = new File("eg/edu/alexu/csd/oop/draw");
			URL url = file.toURL();
			URL[] urls = new URL[] { url };
			ClassLoader cl = new URLClassLoader(urls);
			Class<?> cls = cl.loadClass(s.substring(s.indexOf("\"") + 1, s.lastIndexOf("\"")));
			Shape shape = (Shape) cls.newInstance();
			Point position = new Point();
			HashMap<String, Double> pro = new HashMap<>();
			s = br.readLine();
			s = s.substring(3, s.length() - 4);
			position.x = Integer.parseInt(s);
			s = br.readLine();
			s = s.substring(3, s.length() - 4);
			position.y = Integer.parseInt(s);
			shape.setPosition(position);
			br.readLine();
			while (!(s = br.readLine()).equals("</map>")) {
				String key = s.substring(1, s.indexOf(">"));
				Double value = Double.valueOf(s.substring(s.indexOf(">") + 1, s.lastIndexOf("<")));
				pro.put(key, value);
			}
			shape.setProperties(pro);
			s = br.readLine();
			Color color = new Color(Integer.parseInt(s.substring(s.indexOf(">") + 1, s.lastIndexOf("<"))));
			shape.setColor(color);
			s = br.readLine();
			Color fillColor = new Color(Integer.parseInt(s.substring(s.indexOf(">") + 1, s.lastIndexOf("<"))));
			shape.setFillColor(fillColor);
			sh.add(shape);
			br.read();
			br.readLine();
		}
		br.close();
		fr.close();
		return sh;
	}
}
