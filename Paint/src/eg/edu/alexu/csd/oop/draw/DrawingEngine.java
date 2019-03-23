package eg.edu.alexu.csd.oop.draw;

public interface DrawingEngine {

    /* redraw all shapes on the canvas */
    public void refresh(Object canvas);

    public void addShape(Shape shape);
    public void removeShape(Shape shape);
    public void updateShape(Shape oldShape, Shape newShape);

    /* return the created shapes objects */
    public Shape[] getShapes();
    
    /* return the classes (types) of supported shapes already exist and the
     * ones that can be dynamically loaded at runtime (see Part 3) */
    public java.util.List<Class<? extends Shape>> getSupportedShapes();
  
    /* limited to 20 steps. Only consider in undo & redo 
     * these actions: addShape, removeShape, updateShape */
    public void undo();
    public void redo();

    /* use the file extension to determine the type, 
     * or throw runtime exception when unexpected extension */
    public void save(String path);
    public void load(String path);
}