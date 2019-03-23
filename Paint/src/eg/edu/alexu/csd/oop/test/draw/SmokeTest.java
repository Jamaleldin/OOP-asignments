package eg.edu.alexu.csd.oop.test.draw;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;
import java.util.Map;

import eg.edu.alexu.csd.oop.draw.DrawingEngine;
import eg.edu.alexu.csd.oop.draw.Shape;
import eg.edu.alexu.csd.oop.test.DummyShape;
import eg.edu.alexu.csd.oop.test.TestRunner;



public class SmokeTest {
    
    public static Class<?> getSpecifications(){
        return DrawingEngine.class;
    }
    
    @org.junit.Test
    public void testAddShape() {
        DrawingEngine instance = (DrawingEngine)TestRunner.getImplementationInstanceForInterface(DrawingEngine.class);
        instance.addShape(new DummyShape());
    }
    
    @org.junit.Test
    public void testRemoveShape() {
        DrawingEngine instance = (DrawingEngine)TestRunner.getImplementationInstanceForInterface(DrawingEngine.class);
        try {
            instance.removeShape(new DummyShape());
            fail("Engine accepts removing non existing shape");
        } catch (Throwable e) {
        }
    }
    
    @org.junit.Test
    public void testUpdateShape() {
        DrawingEngine instance = (DrawingEngine)TestRunner.getImplementationInstanceForInterface(DrawingEngine.class);
        try {
            instance.updateShape(new DummyShape(), new DummyShape());
            fail("Engine accepts updating non existing shape");
        } catch (Throwable e) {
        }
    }
    
    @org.junit.Test
    public void testUndoRedo() {
        DrawingEngine instance = (DrawingEngine)TestRunner.getImplementationInstanceForInterface(DrawingEngine.class);
        try {
            instance.undo();
            fail("Engine undo without having actions");
        } catch (Throwable e) {
        }
        try {
            instance.redo();
            fail("Engine undo without having actions");
        } catch (Throwable e) {
        }
    }
    
    @org.junit.Test
    public void testGetShapes(){
        DrawingEngine instance = (DrawingEngine)TestRunner.getImplementationInstanceForInterface(DrawingEngine.class);
        instance.addShape(new DummyShape());
        instance.addShape(new DummyShape());
        instance.addShape(new DummyShape());
        assertEquals("Wrong number of returned shapes", 3, instance.getShapes().length);
    }
    
    @org.junit.Test
    public void testGetShapesAfterDelete(){
        DrawingEngine instance = (DrawingEngine)TestRunner.getImplementationInstanceForInterface(DrawingEngine.class);
        DummyShape shape = new DummyShape();
        instance.addShape(shape);
        instance.addShape(new DummyShape());
        instance.addShape(new DummyShape());
        instance.removeShape(shape);
        assertEquals("Wrong number of returned shapes after delete", 2, instance.getShapes().length);
    }
    
    @org.junit.Test
    public void testShapeProperties(){
        DrawingEngine instance = (DrawingEngine)TestRunner.getImplementationInstanceForInterface(DrawingEngine.class);
        List<Class<? extends Shape>> supportedShapes = instance.getSupportedShapes();
        assertNotNull("No supported shapes returned, check getSupportedShapes function!", supportedShapes);
        assertFalse("No supported shapes returned, check getSupportedShapes function!", supportedShapes.isEmpty());
        for(Class<? extends Shape> shapeClass : supportedShapes){
            if (!shapeClass.getName().contains("Dummy"))
                try {
                    Shape shape = shapeClass.newInstance();
                    assertNotNull("Failed to create shape", shape);
                    if (shape.getProperties() == null || shape.getProperties().size() == 0)
                        fail("Your shapes must have at least one property in the map!");
                } catch (Exception e) {
                    TestRunner.fail("Failed to create shape", e);
                }
        }
    }

}