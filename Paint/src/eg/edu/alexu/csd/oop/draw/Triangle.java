package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;

public class Triangle extends CShape {
	public Triangle(){
		HashMap<String, Double> pro = new HashMap<>();
		pro.put("x", (double) 0);
		pro.put("y", (double) 0);
		pro.put("x1", (double) 0);
		pro.put("y1", (double) 0);
		setProperties(pro);
		Point position = new Point(-1, -1);
		setPosition(position);
		Color strokeColor = Color.LIGHT_GRAY;
		setColor(strokeColor);
		Color fillColor = Color.BLACK;
		setFillColor(fillColor);
	}
	@Override
	public void draw(Object canvas) {
		Graphics2D g2d = (Graphics2D) canvas;
		g2d.setColor(getFillColor());
		int[] xPoints = new int[] { getPosition().x, getProperties().get("x").intValue(),
				getProperties().get("x1").intValue() };
		int[] yPoints = new int[] { getPosition().y, getProperties().get("y").intValue(),
				getProperties().get("y1").intValue() };
		g2d.fillPolygon(xPoints, yPoints, 3);
		g2d.setColor(getColor());
		g2d.drawPolygon(xPoints, yPoints, 3);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Shape clonedTriangle = new Triangle();
		clonedTriangle.setColor(getColor());
		clonedTriangle.setFillColor(getFillColor());
		clonedTriangle.setPosition(new Point(getPosition()));
		clonedTriangle.setProperties(new HashMap<>(getProperties()));
		return clonedTriangle;

	}
}
