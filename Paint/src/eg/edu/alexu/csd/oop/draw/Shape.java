package eg.edu.alexu.csd.oop.draw;

public interface Shape{

    public void setPosition(Object position);
    public Object getPosition();

    // update shape specific properties (e.g., radius)
    public void setProperties(java.util.Map<String, Double> properties);
    public java.util.Map<String, Double> getProperties();

    public void setColor(Object color);
    public Object getColor();

    public void setFillColor(Object color);
    public Object getFillColor();

    public void draw(Object canvas); // redraw the shape on the canvas

    public Object clone() throws CloneNotSupportedException; // create a deep clone of the shape
}