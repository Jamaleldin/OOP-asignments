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

public class Json {
	public void save(String path, Shape[] shapes) throws IOException {
		File f = new File(path);
		FileWriter fw = new FileWriter(f);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("{");
		bw.newLine();
		bw.write("\"shapes\" : [");
		bw.newLine();
		for (int i = 0; i < shapes.length; i++) {
			bw.write("{\"className\" : " + "\"" + shapes[i].getClass().getName() + "\",");
			bw.newLine();
			if (shapes[i].getPosition() != null) {
				Point position = (Point) shapes[i].getPosition();
				bw.write("\"x\" : " + "\"" + position.x + "\",");
				bw.newLine();
				bw.write("\"y\" : " + "\"" + position.y + "\",");
				bw.newLine();
			} else {
				bw.write("\"x\" : \"-1\",");
				bw.newLine();
				bw.write("\"y\" : \"-1\",");
				bw.newLine();
			}
			
			HashMap<String, Double> pro = (HashMap<String, Double>) shapes[i].getProperties();
			if (pro!=null) {
				Set<String> keys = pro.keySet();
				for (String s : keys) {
					bw.write("\"" + s + "\" : \"" + pro.get(s) + "\",");
					bw.newLine();
				}
			} else {
				bw.write("\"key\" : \"-1\",");
				bw.newLine();
			}
			if(shapes[i].getColor()!=null) {
				Color color = (Color) shapes[i].getColor();
				bw.write("\"color\" : " + "\"" + color.getRGB() + "\",");
				bw.newLine();
			}else {
				bw.write("\"color\" : \"-1\",");
				bw.newLine();
			}
			if(shapes[i].getFillColor()!=null) {
				Color color = (Color) shapes[i].getFillColor();
				bw.write("\"fillcolor\" : " + "\"" + color.getRGB() + "\",");
				bw.newLine();
			}else {
				bw.write("\"color\" : \"-1\",");
				bw.newLine();
			}
			bw.write("}");
			if (i != shapes.length - 1) {
				bw.write(",");
			}
			bw.newLine();
		}
		bw.write("]");
		bw.newLine();
		bw.write("}");
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
		br.readLine();
		String s;
		String[] ss;
		while ((s = br.readLine()) != null) {
			ss = s.split("\"");
			File file = new File("eg/edu/alexu/csd/oop/draw");
			URL url = file.toURL();
			URL[] urls = new URL[] { url };
			ClassLoader cl = new URLClassLoader(urls);
			Class<?> cls = cl.loadClass(ss[3]);
			Shape shape = (Shape) cls.newInstance();
			Point position = new Point();
			HashMap<String, Double> pro = new HashMap<>();
			s = br.readLine();
			ss = s.split("\"");
			position.x = Integer.parseInt(ss[3]);
			s = br.readLine();
			ss = s.split("\"");
			position.y = Integer.parseInt(ss[3]);
			shape.setPosition(position);
			while(!(s=br.readLine()).contains("color")) {
				ss = s.split("\"");
				String key = ss[1];
				Double value = Double.valueOf(ss[3]);
				pro.put(key, value);
			}
			shape.setProperties(pro);
			ss = s.split("\"");
			Color color = new Color(Integer.parseInt(ss[3]));
			shape.setColor(color);
			s = br.readLine();
			ss = s.split("\"");
			Color fillColor = new Color(Integer.parseInt(ss[3]));
			shape.setFillColor(fillColor);
			sh.add(shape);
			if(!br.readLine().contains(",")) {
				break;
			}
		}
		br.close();
		fr.close();
		return sh;
	}
}
