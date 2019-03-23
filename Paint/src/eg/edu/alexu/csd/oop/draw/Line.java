package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;

public class Line extends CShape {
	public Line(){
		HashMap<String, Double> pro = new HashMap<>();
		pro.put("x", (double) 0);
		pro.put("y", (double) 0);
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
		g2d.drawLine(getPosition().x, getPosition().y, getProperties().get("x").intValue(),
				getProperties().get("y").intValue());

	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Shape clonedLine = new Line();
		clonedLine.setColor(getColor());
		clonedLine.setPosition(new Point(getPosition()));
		clonedLine.setProperties(new HashMap<>(getProperties()));
		return clonedLine;
	}

}
