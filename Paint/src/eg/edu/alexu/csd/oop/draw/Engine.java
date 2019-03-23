package eg.edu.alexu.csd.oop.draw;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Engine implements DrawingEngine {
	private ArrayList<ArrayList<Shape>> shapesStatus = new ArrayList<>();
	private int pointer = -1;

	@Override
	public void refresh(Object canvas) {
		if (pointer != -1) {
			ArrayList<Shape> currentShapes = shapesStatus.get(pointer);
			for (int i = 0; i < currentShapes.size(); i++) {
				currentShapes.get(i).draw(canvas);
			}
		}
	}

	@Override
	public void addShape(Shape shape) {
		if (pointer == -1) {
			ArrayList<Shape> currentShapes = new ArrayList<>();
			shapesStatus.add(new ArrayList<>());
			pointer++;
			currentShapes.add(shape);
			shapesStatus.add(currentShapes);
		} else {
			@SuppressWarnings("unchecked")
			ArrayList<Shape> currentShapes = (ArrayList<Shape>) shapesStatus.get(pointer).clone();
			currentShapes.add(shape);
			if (pointer != shapesStatus.size() - 1) {
				for (int i = shapesStatus.size() - 1; i > pointer; i--) {
					shapesStatus.remove(i);
				}
			}
			shapesStatus.add(currentShapes);
		}
		pointer++;
		if (pointer > 20) {
			shapesStatus.remove(0);
			pointer = 19;
		}

	}

	@Override
	public void removeShape(Shape shape) {
		ArrayList<Shape> currentShapes = shapesStatus.get(pointer);
		@SuppressWarnings("unchecked")
		ArrayList<Shape> s = (ArrayList<Shape>) currentShapes.clone();
		for (int i = 0; i < currentShapes.size(); i++) {
			if (currentShapes.get(i).equals(shape)) {
				s.remove(i);
				break;
			}
		}
		if (pointer != shapesStatus.size() - 1) {
			for (int i = shapesStatus.size() - 1; i > pointer; i--) {
				shapesStatus.remove(i);
			}
		}
		shapesStatus.add(s);
		pointer++;
		if (pointer > 20) {
			shapesStatus.remove(0);
			pointer = 19;
		}
	}

	@Override
	public void updateShape(Shape oldShape, Shape newShape) {
		ArrayList<Shape> currentShapes = shapesStatus.get(pointer);
		@SuppressWarnings("unchecked")
		ArrayList<Shape> s = (ArrayList<Shape>) currentShapes.clone();
		for (int i = 0; i < currentShapes.size(); i++) {
			if (currentShapes.get(i).equals(oldShape)) {
				s.set(i, newShape);
				break;
			}
		}
		if (pointer != shapesStatus.size() - 1) {
			for (int i = shapesStatus.size() - 1; i > pointer; i--) {
				shapesStatus.remove(i);
			}
		}
		shapesStatus.add(s);
		pointer++;
		if (pointer > 20) {
			shapesStatus.remove(0);
			pointer = 19;
		}
	}

	@Override
	public Shape[] getShapes() {
		Shape[] shapes = new Shape[shapesStatus.get(pointer).size()];
		for (int i = 0; i < shapes.length; i++) {
			shapes[i] = shapesStatus.get(pointer).get(i);
		}
		return shapes;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Class<? extends Shape>> getSupportedShapes() {
		List<Class<? extends Shape>> supportedClasses = new LinkedList<>();
		String packageFolder = "eg/edu/alexu/csd/oop/draw";
		ClassLoader cl = ClassLoader.getSystemClassLoader();
		URL url = cl.getResource(packageFolder);
		String path = url.getPath();
		File[] files = new File(path).listFiles();
		for (int i=0;i<files.length;i++) {
			File f = files[i];
			String className = packageFolder + "/" + f.getName().substring(0, f.getName().length() - 6);
			Class<?> check = null;
			boolean flag = false;
			try {
				check = Class.forName(className.replace('/', '.'));
				flag = CShape.class.isAssignableFrom(check);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			if (flag && !f.getName().contains("CShape")) {
				supportedClasses.add((Class<? extends Shape>) check);
			}
		}
		try {
			URL[] urls = { new URL("jar:file:RoundRectangle.jar!/") };
			URLClassLoader urlCl = URLClassLoader.newInstance(urls);
			Class<?> cls = urlCl.loadClass(packageFolder.replace('/', '.') + ".RoundRectangle");
			supportedClasses.add((Class<? extends Shape>) cls);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return supportedClasses;
	}

	@Override
	public void undo() {
		if (pointer > 0) {
			pointer--;
		}
	}

	@Override
	public void redo() {
		if (pointer < shapesStatus.size() - 1 && pointer < 21) {
			pointer++;
		}
	}

	@Override
	public void save(String path) {
		String extention = path.substring(path.lastIndexOf(".") + 1);
		if (extention.length() > 3) {
			Json json = new Json();
			try {
				json.save(path, getShapes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Xml xml = new Xml();
			try {
				xml.save(path, getShapes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void load(String path) {
		String extention = path.substring(path.lastIndexOf(".") + 1);
		if (extention.length() > 3) {
			Json json = new Json();
			try {
				ArrayList<Shape> shapes = json.load(path);
				shapesStatus.clear();
				shapesStatus.add(shapes);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} else {
			Xml xml = new Xml();

			try {
				ArrayList<Shape> shapes = xml.load(path);
				shapesStatus.clear();
				shapesStatus.add(shapes);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		pointer = 0;
	}

}
