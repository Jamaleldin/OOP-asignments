package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public abstract class CShape implements Shape {
	private HashMap<String, Double> properties;
	private Point position;
	private Color strokeColor;
	private Color fillColor;

	@Override
	public void setPosition(Object position) {
		this.position = (Point) position;

	}

	@Override
	public Point getPosition() {
		return position;
	}

	@Override
	public void setProperties(Map<String, Double> properties) {
		this.properties = (HashMap<String, Double>) properties;
	}

	@Override
	public Map<String, Double> getProperties() {
		return properties;
	}

	@Override
	public void setColor(Object color) {
		strokeColor = (Color) color;

	}

	@Override
	public Color getColor() {
		return strokeColor;
	}

	@Override
	public void setFillColor(Object color) {
		fillColor = (Color) color;

	}

	@Override
	public Color getFillColor() {
		return fillColor;
	}

	@Override
	public void draw(Object canvas) {
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return null;

	}

}
