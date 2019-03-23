package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;

public class Rectangle extends CShape {
	public Rectangle(){
		HashMap<String, Double> pro = new HashMap<>();
		pro.put("width", (double) 0);
		pro.put("hight", (double) 0);
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
		g2d.fillRect(getProperties().get("topLeftMinX").intValue(),
				getProperties().get("topLeftMinY").intValue(),
				getProperties().get("width").intValue(),
				getProperties().get("hight").intValue());
		g2d.setColor(getColor());
		g2d.drawRect(getProperties().get("topLeftMinX").intValue(),
				getProperties().get("topLeftMinY").intValue(),
				getProperties().get("width").intValue(),
				getProperties().get("hight").intValue());
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Shape clonedRect = new Rectangle();
		clonedRect.setColor(getColor());
		clonedRect.setFillColor(getFillColor());
		clonedRect.setPosition(new Point(getPosition()));
		clonedRect.setProperties(new HashMap<>(getProperties()));
		return clonedRect;
	}
}
