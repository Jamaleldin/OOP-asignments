package eg.edu.alexu.csd.oop.draw;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.JComboBox;

public class Controller {
	@SuppressWarnings("deprecation")
	public Shape createShape(String shape, DrawingEngine engine, JComboBox<String> shapes)
			throws InstantiationException, IllegalAccessException {
		java.util.List<Class<? extends Shape>> supportedShapes = engine.getSupportedShapes();
		for (int i = 0; i < supportedShapes.size(); i++) {
			Class<? extends Shape> cls = supportedShapes.get(i);
			String name = cls.getName().substring(cls.getName().lastIndexOf(".") + 1);
			if (shape.equals(name)) {
				return cls.newInstance();
			} else if (shapes.getSelectedItem() != null && shapes.getSelectedItem().equals(name)) {
				return cls.newInstance();
			}
		}
		return null;
	}

	public Shape getSelectedShape(Point click, DrawingEngine engine, Graphics g) {
		Shape[] shapes = engine.getShapes();
		for (int i = shapes.length - 1; i >= 0; i--) {
			Point position = (Point) shapes[i].getPosition();
			HashMap<String, Double> properties = (HashMap<String, Double>) shapes[i].getProperties();
			if (shapes[i].getClass().getName().contains("RoundRectangle")) {
				int minX = position.x;
				int minY = position.y;
				int maxX = (int) (position.x + properties.get("Width"));
				int maxY = (int) (position.y + properties.get("Length"));
				if (click.x >= minX && click.y >= minY && click.x <= maxX && click.y <= maxY) {
					return shapes[i];
				}
			} else {
				try {
					int minX = properties.get("topLeftMinX").intValue();
					int minY = properties.get("topLeftMinY").intValue();
					if (click.x >= minX && click.y >= minY && click.x <= minX + properties.get("width").intValue()
							&& click.y <= minY + properties.get("hight").intValue()) {
						return shapes[i];
					}
				} catch (Exception e) {
					if (properties.get("length") != (null)) {
						int minX = properties.get("topLeftMinX").intValue();
						int minY = properties.get("topLeftMinY").intValue();
						if (click.x >= minX && click.y >= minY && click.x <= minX + properties.get("length").intValue()
								&& click.y <= minY + properties.get("length").intValue()) {
							return shapes[i];
						}
					} else {
						int minX = Math.min(position.x, properties.get("x").intValue());
						int minY = Math.min(position.y, properties.get("y").intValue());
						int maxX = Math.max(position.x, properties.get("x").intValue());
						int maxY = Math.max(position.y, properties.get("y").intValue());
						minX = Math.min(minX, properties.get("x1").intValue());
						minY = Math.min(minY, properties.get("y1").intValue());
						maxX = Math.max(maxX, properties.get("x1").intValue());
						maxY = Math.max(maxY, properties.get("y1").intValue());
						if (click.x >= minX && click.y >= minY && click.x <= maxX && click.y <= maxY) {
							return shapes[i];
						}
					}
				}
			}

		}
		return null;
	}

	public Shape getShapeUpdated(MouseEvent e, Shape selectedShape, Point click) throws CloneNotSupportedException {
		double diffx = click.x - e.getX();
		double diffy = click.y - e.getY();
		if (selectedShape.getClass().getName().contains("RoundRectangle")) {
			Point newPosition = (Point) selectedShape.getPosition();
			newPosition.x = (int) (newPosition.x - diffx);
			newPosition.y = (int) (newPosition.y - diffy);
			selectedShape.setPosition(newPosition);
			click.x = e.getX();
			click.y = e.getY();
		} else {
			HashMap<String, Double> properties = new HashMap<>(selectedShape.getProperties());
			try {
				properties.put("topLeftMinX", (double) properties.get("topLeftMinX") - diffx);
				properties.put("topLeftMinY", (double) properties.get("topLeftMinY") - diffy);
				Point newPosition = (Point) selectedShape.getPosition();
				newPosition.x = (int) (newPosition.x - diffx);
				newPosition.y = (int) (newPosition.y - diffy);
				selectedShape.setProperties(properties);
				selectedShape.setPosition(newPosition);
				click.x = e.getX();
				click.y = e.getY();
			} catch (Exception e2) {
				properties.put("x", (double) properties.get("x") - diffx);
				properties.put("y", (double) properties.get("y") - diffy);
				properties.put("x1", (double) properties.get("x1") - diffx);
				properties.put("y1", (double) properties.get("y1") - diffy);
				Point newPosition = (Point) selectedShape.getPosition();
				newPosition.x = (int) (newPosition.x - diffx);
				newPosition.y = (int) (newPosition.y - diffy);
				selectedShape.setProperties(properties);
				selectedShape.setPosition(newPosition);
				click.x = e.getX();
				click.y = e.getY();
			}
		}
		return selectedShape;
	}

	public Shape getShapeResized(MouseEvent e, Shape selectedShape, Double ArcWidth, Double ArcHight)
			throws CloneNotSupportedException {
		Point position = (Point) selectedShape.getPosition();
		HashMap<String, Double> properties = new HashMap<>(selectedShape.getProperties());
		if (selectedShape.getClass().getName().contains("RoundRectangle")) {
			properties.put("ArcWidth", ArcWidth);
			properties.put("ArcHight", ArcHight);
			properties.put("Width", (double) Math.abs(e.getX() - position.x));
			properties.put("Length", (double) Math.abs(e.getY() - position.y));
			selectedShape.setProperties(properties);
		} else {
			if (properties.get("width") != null) {
				properties.put("width", (double) Math.abs(e.getX() - position.x));
				properties.put("hight", (double) Math.abs(e.getY() - position.y));
				selectedShape.setProperties(properties);
			} else if (properties.get("length") != null) {
				int size = Math.max(Math.abs(e.getX() - position.x), Math.abs(e.getY() - position.y));
				properties.put("length", (double) size);
				selectedShape.setProperties(properties);
			} else {
				properties.put("x", (double) e.getX());
				properties.put("y", (double) e.getY());
				properties.put("x1", (double) position.x - (e.getX() - position.x));
				properties.put("y1", (double) e.getY());
				selectedShape.setProperties(properties);
			}
		}
		return selectedShape;
	}

	public HashMap<String, Double> getLength(Shape shape, MouseEvent e) {
		Point position = (Point) shape.getPosition();
		int minX = Math.min(position.x, e.getX());
		int minY = Math.min(position.y, e.getY());
		int maxX = Math.max(position.x, e.getX());
		int maxY = Math.max(position.y, e.getY());
		int size = Math.max(maxX - minX, maxY - minY);
		if (minY < position.y) {
			minY = position.y - size;
		}
		if (minX < position.x) {
			minX = position.x - size;
		}
		HashMap<String, Double> pro = new HashMap<>();
		pro.put("topLeftMinX", (double) minX);
		pro.put("topLeftMinY", (double) minY);
		pro.put("length", (double) size);
		pro.put("length", (double) size);
		return pro;
	}
}
