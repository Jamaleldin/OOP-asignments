package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.HashMap;

public class Square extends CShape {
	public Square(){
		HashMap<String, Double> pro = new HashMap<>();
		pro.put("length", (double) 0);
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
		g2d.fillRect(getProperties().get("topLeftMinX").intValue(), getProperties().get("topLeftMinY").intValue(),
				getProperties().get("length").intValue(), getProperties().get("length").intValue());
		g2d.setColor(getColor());
		g2d.drawRect(getProperties().get("topLeftMinX").intValue(), getProperties().get("topLeftMinY").intValue(),
				getProperties().get("length").intValue(), getProperties().get("length").intValue());
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Shape clonedSquare = new Square();
		clonedSquare.setColor(getColor());
		clonedSquare.setFillColor(getFillColor());
		clonedSquare.setPosition(new Point(getPosition()));
		clonedSquare.setProperties(new HashMap<>(getProperties()));
		return clonedSquare;
	}
}
